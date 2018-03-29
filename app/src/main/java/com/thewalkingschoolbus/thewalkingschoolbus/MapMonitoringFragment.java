package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.MapUtil;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.GpsLocation;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_GPS_LOCATION;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_ONE_GROUP;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_UNREAD_MESSAGES_FOR_USER;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;

public class MapMonitoringFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MapMonitoringFragment";
    private View view;

    private static Thread thread;
    private GoogleMap map;
    private List<User> activeMonitoringUsers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_map_monitoring, container, false);

        setupRefreshButton();
        initializeMap();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNewMessageContinuously(10);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (thread != null) {
            thread.interrupt();
        }
    }

    private void setupRefreshButton() {
        Button button = view.findViewById(R.id.refreshMapMonitoring);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMap();
            }
        });
    }

    private void initializeMap() {
        Log.d(TAG, "initMap: initializing map");

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mapMonitoring);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: map is ready");

                map = googleMap;
                updateMap();
            }
        });
    }

    private void updateMap() {
        Toast.makeText(getActivity(), "Updating map", Toast.LENGTH_SHORT).show();
        map.clear();
        activeMonitoringUsers = new ArrayList<>();
        updateMapGetLoginUserDetail();
    }

    private void updateMapGetLoginUserDetail() {
        new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                User userDetailed = (User) result;
                List<User> monitoringUsers = userDetailed.getMonitorsUsers();
                if (monitoringUsers.isEmpty()) {
                    Toast.makeText(getActivity(), "Not monitoring anyone", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (final User user : monitoringUsers) {
                    Log.d("@@@@", "user ID " + user.getId());
                    Log.d("@@@@", "user NAME " + user.getName());
                    updateMapGetCurrentUserLocation(user, false);
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Error: "+e.getMessage());
            }
        }).execute();
    }

    private void updateMapGetCurrentUserLocation(final User user, final boolean isLeader) {
        new GetUserAsyncTask(GET_GPS_LOCATION, user, null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                final GpsLocation gpsLocation = (GpsLocation) result;
                Log.d("@@@@", "GET_GPS_LOCATION LAT " + gpsLocation.getLat());
                Log.d("@@@@", "GET_GPS_LOCATION LNG " + gpsLocation.getLng());
                Log.d("@@@@", "GET_GPS_LOCATION DATE " + gpsLocation.getTimestamp());

                if (gpsLocation.getTimestamp() != null) {
                    updateMapGetCurrentUserDetail(user, gpsLocation, isLeader);
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Error: "+e.getMessage());
            }
        }).execute();
    }

    private void updateMapGetCurrentUserDetail(final User user, final GpsLocation gpsLocation, final boolean isLeader) {
        new GetUserAsyncTask(GET_USER_BY_ID, user, null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                User userDetailed = (User) result;

                userDetailed.setLastGpsLocation(gpsLocation);
                activeMonitoringUsers.add(userDetailed);

                Log.d("@@@@", "userDetailed ID " + userDetailed.getId());
                Log.d("@@@@", "userDetailed NAME " + userDetailed.getName());
                Log.d("@@@@", "userDetailed GPS LAT " + userDetailed.getLastGpsLocation().getLat());
                Log.d("@@@@", "userDetailed GPS LNG " + userDetailed.getLastGpsLocation().getLng());
                Log.d("@@@@", "userDetailed GPS DATE " + userDetailed.getLastGpsLocation().getTimestamp());

                LatLng userLatLng = new LatLng(Double.parseDouble(gpsLocation.getLat()), Double.parseDouble(gpsLocation.getLng()));
                String simpleDate = WalkingFragment.dateToStringSimple(WalkingFragment.stringToDate(gpsLocation.getTimestamp()));
                if (isLeader) {
                    MapUtil.setMarker(map, userLatLng, "GROUP LEADER: " + userDetailed.getName(), "UPDATED: " + simpleDate, 210);
                } else { // Get this user's group leaders and mark them on the map as well
                    MapUtil.setMarker(map, userLatLng, "MONITORING: " + userDetailed.getName(), "UPDATED: " + simpleDate, 110);
                    for (Group group : userDetailed.getMemberOfGroups()) {
                        updateMapGetCurrentUserLeaders(group);
                    }
                }

                updateDropdown();
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Error: "+e.getMessage());
            }
        }).execute();
    }

    private void updateMapGetCurrentUserLeaders(final Group group) {
        new GetUserAsyncTask(GET_ONE_GROUP, null, null, group,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                final Group group = (Group) result;
                updateMapGetCurrentUserLocation(group.getLeader(), true);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Error: "+e.getMessage());
            }
        }).execute();
    }

    private void updateDropdown() {
        Spinner dropdown = view.findViewById(R.id.spinnerSelectMonitoring);
        dropdown.setVisibility(View.VISIBLE);

        List<String> items = new ArrayList<>();
        for (User user : activeMonitoringUsers) {
            items.add(user.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        User userSelected = activeMonitoringUsers.get(position);
        LatLng userLatLng = new LatLng(Double.parseDouble(userSelected.getLastGpsLocation().getLat()), Double.parseDouble((userSelected.getLastGpsLocation().getLng())));
        MapUtil.moveCamera(map, userLatLng, MapUtil.getDefaultZoom());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateNewMessageContinuously(final int seconds) {
        // Initial update
        updateNewMessageCount();

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000 * seconds);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateNewMessageCount();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
    }

    private void updateNewMessageCount() {
        new GetUserAsyncTask(GET_UNREAD_MESSAGES_FOR_USER, User.getLoginUser(), null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                Message[] messages = (Message[]) result;
                TextView textView = view.findViewById(R.id.newMessageCount);
                textView.setText(messages.length + " NEW");

                Log.d(TAG, "#### Successfully updated message count. " + messages.length);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "#### Error: "+e.getMessage());
            }
        }).execute();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MapMonitoringFragment.class);
    }
}