package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ResultCallback;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.EnterGroupNameDialogFragment;
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.DirectionFinder;
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.DirectionFinderListener;
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.Route;
import com.thewalkingschoolbus.thewalkingschoolbus.model.PlaceInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
* SOURCES - Based on following tutorials:
* Google Maps Android API tutorial
*   https://developers.google.com/maps/documentation/android-api/current-place-tutorial
* Youtube tutorial "Get Device Location" by "CodingWithMitch"
*   https://www.youtube.com/watch?v=fPFr0So1LmI
* Youtube tutorial "Basic Google Maps API Android Tutorial + Google Maps Directions API" by "Hiep Mai Thanh"
*   https://www.youtube.com/watch?v=fPFr0So1LmI
* map_modules from source code by "Hiep Mai Thanh"
*   https://github.com/hiepxuan2008/GoogleMapDirectionSimple
*/
public class MapFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MapFragment";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final float DEFAULT_ZOOM = 18;

    private GoogleMap mMap;
    private Boolean mLocationPermissionGranted = false;
    private Boolean mValidRouteEstablished = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private View view;

    private AutoCompleteTextView etOrigin;
    private AutoCompleteTextView etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private PlaceInfo mPlace;
    private Marker mMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_map, container, false);

        if (isServicesOK()) {
            getLocationPermission();
        }
        etOrigin = (AutoCompleteTextView) view.findViewById(R.id.etOrigin);
        etDestination = (AutoCompleteTextView) view.findViewById(R.id.etDestination);

        init();

        Button btnFindPath = (Button) view.findViewById(R.id.btnFindPath);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        Button btnCreateGroup = (Button) view.findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCreateGroupDialog();
            }
        });

        return view;
    }

    private void init(){
        Log.d(TAG, "initializing");


        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient,
                LAT_LNG_BOUNDS, null);


        etOrigin.setOnItemClickListener(mAutocompleteClickListener);
        etDestination.setOnItemClickListener(mAutocompleteClickListener);

        etOrigin.setAdapter(mPlaceAutocompleteAdapter);
        etDestination.setAdapter(mPlaceAutocompleteAdapter);

        etOrigin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    // execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });

        etDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    // execute our method for searching
                    geoLocate2();
                }

                return false;
            }
        });

        hideSoftKeyboard();
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = etOrigin.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException;" + e.getMessage());
        }

        if(list.size() >0 ){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate found a location " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));

        }
    }

    private void geoLocate2() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = etDestination.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException;" + e.getMessage());
        }

        if(list.size() >0 ){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate found a location " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));

        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occurred but we can resolve it
            Log.d(TAG, "isServicesOK: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the device.
     * The result of the permission request is handled by a callback, onRequestPermissionsResult.
     */
        Log.d(TAG, "getLocationPermission: getting location permissions");

        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                initMap();
            }
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: map is ready");

                mMap = googleMap;
                mMap.setPadding(0, 600, 0, 0); // Boundaries for google map buttons
                if (mLocationPermissionGranted) {
                    getDeviceLocation();
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location currentLocation = (Location) task.getResult(); // TODO: BUG: Occasionally, even though task.isSuccessful, current location will return null and crash app.
                            if (currentLocation == null) {
                                Toast.makeText(getActivity(), "Cannot find current location. (error code 1)", Toast.LENGTH_SHORT).show();
                            } else {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM,
                                        "My Location");
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            Toast.makeText(getActivity(), "Cannot find current location. (error code 2)", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        if(!title .equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyboard();

    }
    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        if(placeInfo != null){
            try {
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);


            }catch (NullPointerException e) {
                Log.e(TAG, "moveCamera: NullPointerException " + e.getMessage() );
            }
        }else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();

    }

    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(new DirectionFinderListener() {
                @Override
                public void onDirectionFinderStart() {
                    displayProgressDialog();

                    if (originMarkers != null) {
                        for (Marker marker : originMarkers) {
                            marker.remove();
                        }
                    }

                    if (destinationMarkers != null) {
                        for (Marker marker : destinationMarkers) {
                            marker.remove();
                        }
                    }

                    if (polylinePaths != null) {
                        for (Polyline polyline:polylinePaths ) {
                            polyline.remove();
                        }
                    }
                }

                @Override
                public void onDirectionFinderSuccess(List<Route> routes) {
                    mValidRouteEstablished = true;
                    progressDialog.dismiss();
                    polylinePaths = new ArrayList<>();
                    originMarkers = new ArrayList<>();
                    destinationMarkers = new ArrayList<>();

                    for (Route route : routes) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
                        ((TextView) view.findViewById(R.id.tvDuration)).setText(route.duration.text);
                        ((TextView) view.findViewById(R.id.tvDistance)).setText(route.distance.text);

                        originMarkers.add(mMap.addMarker(new MarkerOptions()
                                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue)) // TODO: SET CUSTOM MARKER
                                .title(route.startAddress)
                                .position(route.startLocation)));
                        destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green)) // TODO: SET CUSTOM MARKER
                                .title(route.endAddress)
                                .position(route.endLocation)));

                        PolylineOptions polylineOptions = new PolylineOptions().
                                geodesic(true).
                                color(getResources().getColor(R.color.logoBlue)).
                                width(10);

                        for (int i = 0; i < route.points.size(); i++)
                            polylineOptions.add(route.points.get(i));

                        polylinePaths.add(mMap.addPolyline(polylineOptions));
                    }
                }
            }, origin, destination).execute();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void displayProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage("Finding direction...");
//        progressDialog.setCancelable(false);
//        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                progressDialog.dismiss();
//            }
//        });
        progressDialog.show();

//        progressDialog = ProgressDialog.show(getActivity(), "Please wait.",
//                "Finding direction..!", true);
    }

    private void displayCreateGroupDialog() {
        if (mValidRouteEstablished) {
            // Create dialog which calls createGroup when user enters valid group name.
            android.support.v4.app.FragmentManager manager = getFragmentManager();
            EnterGroupNameDialogFragment dialog = new EnterGroupNameDialogFragment();
            dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            dialog.show(manager, "MessageDialog");
        } else {
            Toast.makeText(getActivity(), "Please enter valid path.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void createGroup(Context context) {

        // Add to group list using NAME, ORIGIN, DESTINATION
        // Transition to group window
    }

    private void hideSoftKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();
        }
    };
}
