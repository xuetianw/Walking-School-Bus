package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.GpsLocation;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.service.UploadLocationStopService;
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.MapUtil;
import com.thewalkingschoolbus.thewalkingschoolbus.service.UploadLocationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_ONE_GROUP;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.POST_GPS_LOCATION;

public class WalkingFragment extends android.app.Fragment implements AdapterView.OnItemSelectedListener {

    private static int DEFAULT_ARRIVAL_RADIUS_METERS = 50;

    private static final String TAG = "WalkingFragment";
    private static View view;
    private static boolean isWalking = false;
    private static List<Group> totalGroupList;
    private static int i;
    private static LatLng currentDestination;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_walking, container, false);

        setupBtn();
        setupDropdownGroupSelection();
        MapUtil.getLocationPermission();

        return view;
    }

    private void setupBtn() {
        Button startBtn = view.findViewById(R.id.startWalkingBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWalk();
            }
        });
        Button arrivedBtn = view.findViewById(R.id.stopWalkingBtn);
        arrivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWalk();
            }
        });
        Button panicBtn = view.findViewById(R.id.panicBtn);
        panicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panicWalk();
            }
        });
    }

    private void startWalk() {
        if (isWalking) {
            Toast.makeText(getActivity(), "Already walking", Toast.LENGTH_SHORT).show();
        } else {
            isWalking = true;
            Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();
            updateStatusText("Now walking...");
            updateLocationFirstTime();
            startUploadLocationService();
        }
    }

    private void stopWalk() {
        if (isWalking) {
            isWalking = false;
            Toast.makeText(getActivity(), "Stop", Toast.LENGTH_SHORT).show();
            updateStatusText("Walk stopped.");
            cancelUploadLocationService();
        } else {
            Toast.makeText(getActivity(), "Not walking", Toast.LENGTH_SHORT).show();
        }
    }

    private void panicWalk() {

    }

    private void startUploadLocationService() {
        Intent intent = new Intent(getActivity(), UploadLocationStopService.class);
        getActivity().stopService(intent);

        intent = new Intent(getActivity(), UploadLocationService.class);
        getActivity().stopService(intent);
        getActivity().startService(intent);
    }

    private void cancelUploadLocationService() {
        Intent intent = new Intent(getActivity(), UploadLocationService.class);
        getActivity().stopService(intent);

        intent = new Intent(getActivity(), UploadLocationStopService.class);
        getActivity().stopService(intent);
        getActivity().startService(intent);
    }

    private void updateStatusText(String statusText) {
        TextView text = view.findViewById(R.id.walkStatusTxt);
        text.setText(statusText);
    }

    // DROPDOWN GROUP SELECTION

    private void setupDropdownGroupSelection() {
        new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                User userDetailed = (User) result;

                // Get all groups
                totalGroupList = new ArrayList<>();
                if (!userDetailed.getMemberOfGroups().isEmpty()) {
                    totalGroupList = userDetailed.getMemberOfGroups();
                }
                if (!userDetailed.getLeadsGroups().isEmpty()) {
                    totalGroupList.addAll(userDetailed.getLeadsGroups());
                }

                if (totalGroupList.isEmpty()) {
                    Toast.makeText(getActivity(), "Not in any group", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (final Group group : totalGroupList) {
                    new GetUserAsyncTask(GET_ONE_GROUP, null, null, group,null, new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            totalGroupList.set(totalGroupList.indexOf(group), (Group) result);
                            updateDropdown();
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Log.d(TAG, "Error: "+e.getMessage());
                        }
                    }).execute();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Error: "+e.getMessage());
            }
        }).execute();
    }

    private void updateDropdown() {
        Spinner dropdown = view.findViewById(R.id.spinnerGroupList);
        dropdown.setVisibility(View.VISIBLE);

        List<String> items = new ArrayList<>();
        for (Group group : totalGroupList) {
            items.add("ID: " + group.getId() + " NAME: " + group.getGroupDescription());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // Build current destination
        Group groupSelected = totalGroupList.get(position);
        Log.d(TAG, "#### ITEM SELECTED!" + groupSelected.getGroupDescription());
        currentDestination = new LatLng(groupSelected.getRouteLatArray()[1], groupSelected.getRouteLngArray()[1]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // DATE

    public static Date stringToDate(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        try {
            return format.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToStringSimple(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'DATE: 'yyyy-MM-dd', TIME:'HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        return dateFormat.format(date);
    }

    // STATIC

    public static void uploadCurrentCoordinate(Location currentLocation) {

        checkArrival(currentLocation);

        User user = User.getLoginUser();

        if (currentLocation == null) {
            Log.d(TAG, "#### current location == null! (note: first upload may be null)");
            user.setLastGpsLocation(new GpsLocation(null, null, null));
        } else {
            Double lat = currentLocation.getLatitude();
            Double lng = currentLocation.getLongitude();
            String timestamp = dateToString(new Date());

            Log.d(TAG, "#### LAT / LNG: " + lat + " / " + lng);
            Log.d(TAG, "#### TIMESTAMP: " + timestamp);

            user.setLastGpsLocation(new GpsLocation(lat.toString(), lng.toString(), timestamp));
        }

        new GetUserAsyncTask(POST_GPS_LOCATION, user, null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                //User user = (User) result;
                Log.d(TAG, "#### Successfully updated current location. " + result);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "#### Error: "+e.getMessage());
            }
        }).execute();
    }

    private static void checkArrival(Location currentLocation) {

        Log.d(TAG, "#### checkArrival");
        Log.d(TAG, "#### currentDestination LatLng " + currentDestination.latitude + ", " + currentDestination.longitude);

        if (currentLocation == null || currentDestination == null) {
            Log.d(TAG, "#### currentLocation == null || currentDestination == null");
            return;
        }

        float[] results = new float[1];

        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
                currentDestination.latitude, currentDestination.longitude,
                results);
        if (results[0] < DEFAULT_ARRIVAL_RADIUS_METERS) {
            arrived();
        } else {
            Log.d(TAG, "#### not arrived!");
        }
    }

    private static void arrived() {
        Log.d(TAG, "#### arrived");
        Context context = MainMenuActivity.getContextOfApplication();
        if (view != null) {
            isWalking = false;
            Toast.makeText(context, "Arrived", Toast.LENGTH_SHORT).show();
            TextView text = view.findViewById(R.id.walkStatusTxt);
            text.setText("Arrived!");

            Intent intent = new Intent(context, UploadLocationService.class);
            context.stopService(intent);

            intent = new Intent(context, UploadLocationStopService.class);
            context.stopService(intent);
            context.startService(intent);
        } else {
            Log.d(TAG, "#### view != null");
        }
    }

    private void updateLocationFirstTime() {
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        uploadCurrentCoordinate(location);
                        Log.d(TAG, "####: First Update");
                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, WalkingFragment.class);
    }
}
