package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.MapUtil;
import com.thewalkingschoolbus.thewalkingschoolbus.models.UploadLocationService;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;

public class MapMonitoringActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "MapUtil";
    private GoogleMap map;
    private List<User> activeMonitoringUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_monitoring);

        initializeMap();
    }

    private void initializeMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapMonitoring)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                updateMap();
                updateDropdown();
            }
        });
    }

    private void updateMap() {
        map.clear();
        List<User> monitoringUsers = User.getLoginUser().getMonitorsUsers(); // TODO: the login user does not update itself
        Log.d("@@@@", "monitoringUsers.size " + monitoringUsers.size());
        activeMonitoringUsers = new ArrayList<>();
        for (User user : monitoringUsers) {
            Log.d("@@@@", "user ID " + user.getId());
            Log.d("@@@@", "user NAME " + user.getName());
            new GetUserAsyncTask(GET_USER_BY_ID, user, null, null,null, new OnTaskComplete() {
                @Override
                public void onSuccess(Object result) {
                    User userDetailed = (User) result;
                    Log.d("@@@@", "userDetailed ID " + userDetailed.getId());
                    Log.d("@@@@", "userDetailed NAME " + userDetailed.getName());
                    Log.d("@@@@", "userDetailed CELL PHONE " + userDetailed.getCellPhone());
                    Log.d("@@@@", "userDetailed GPS LAT " + userDetailed.getLastGpsLocation().getLat());
                    Log.d("@@@@", "userDetailed GPS LNG " + userDetailed.getLastGpsLocation().getLng());
                    Log.d("@@@@", "userDetailed GPS DATE " + userDetailed.getLastGpsLocation().getTimestamp());
                    if (userDetailed.getLastGpsLocation().getTimestamp() != null) {
                        activeMonitoringUsers.add(userDetailed);
                        LatLng userLatLng = new LatLng(Double.parseDouble(userDetailed.getLastGpsLocation().getLat()), Double.parseDouble(userDetailed.getLastGpsLocation().getLng()));
                        MapUtil.setMarker(map, userLatLng, userDetailed.getName(), userDetailed.getLastGpsLocation().getTimestamp());
                        updateDropdown();
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "Error: "+e.getMessage());
                }
            }).execute();
        }
//        if (monitoringUsers.isEmpty()) {
//            Toast.makeText(this, "Not monitoring anyone", Toast.LENGTH_SHORT).show();
//        } else if (activeMonitoringUsers.isEmpty()) {
//            Toast.makeText(this, "No one is walking", Toast.LENGTH_SHORT).show();
//        }
    }

    private void updateDropdown() {
        Spinner dropdown = findViewById(R.id.spinnerSelectMonitoring);

        List<String> items = new ArrayList<>();
        for (User user : activeMonitoringUsers) {
            items.add(user.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
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

    public static Intent makeIntent(Context context) {
        return new Intent(context, MapMonitoringActivity.class);
    }
}
