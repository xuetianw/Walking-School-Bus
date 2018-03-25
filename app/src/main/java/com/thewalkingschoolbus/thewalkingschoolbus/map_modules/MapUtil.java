package com.thewalkingschoolbus.thewalkingschoolbus.map_modules;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.thewalkingschoolbus.thewalkingschoolbus.MainMenuActivity;
import com.thewalkingschoolbus.thewalkingschoolbus.MapFragment;
import com.thewalkingschoolbus.thewalkingschoolbus.R;

public class MapUtil {

    // CUSTOM VALUES
    private static final float DEFAULT_ZOOM = 14;                       // Larger means more zoomed in
    private static final int MAXIMUM_ROUTE_DISTANCE_METERS = 10000000;  // This distance is approx. Vancouver to Chilliwack
    private static int DEFAULT_SEARCH_RADIUS_METERS = 1000;

    private static final String TAG = "MapUtil";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234;
    private static Boolean locationPermissionGranted = false;
    private static Location currentLocation = null;
    private Context context;

    public static float getDefaultZoom() {
        return DEFAULT_ZOOM;
    }

    public static void moveCamera(GoogleMap map, LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public static Marker setMarker(GoogleMap map, LatLng position, String title, String snippet) {
        Marker marker = map.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green)) // TODO: SET CUSTOM MARKER
                .title(title)
                .snippet(snippet)
                .position(position));
        return marker;
    }

    public MapUtil(Context context, GoogleMap map) {
        this.context = context;
        getLocationPermission();
    }

    public GoogleMap getMap() {
        return map;
    }

    // SET UP //

    public static void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the device.
     * The result of the permission request is handled by a callback, onRequestPermissionsResult.
     */
        Log.d(TAG, "getLocationPermission: getting location permissions");

        if (ContextCompat.checkSelfPermission(MainMenuActivity.getContextOfApplication(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            //initMap();
        } else {
            ActivityCompat.requestPermissions((Activity) MainMenuActivity.getContextOfApplication(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult: called.");
//
//        locationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    locationPermissionGranted = true;
//                }
//                initMap();
//            }
//        }
//    }

    private static GoogleMap map;

    public static GoogleMap initMapView(SupportMapFragment mapFragment) {
        Log.d(TAG, "initMap: initializing map");

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: map is ready");
                map = googleMap;
                if (locationPermissionGranted) {
                    getDeviceLocation();
                    if (ActivityCompat.checkSelfPermission(MainMenuActivity.getContextOfApplication(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainMenuActivity.getContextOfApplication(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });
        return map;
    }

    // MAIN MAP FUNCTIONS - GENERAL //

    public static Location getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainMenuActivity.getContextOfApplication());
        try {
            //if (locationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            //}
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        return currentLocation;
    }

    private void relocateMyLocationButton(View view) {
        // Get the button view
        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        // and next place it, for example, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
    }
}
