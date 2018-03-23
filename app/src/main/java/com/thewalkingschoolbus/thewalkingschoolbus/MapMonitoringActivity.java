package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.MapUtil;
import com.thewalkingschoolbus.thewalkingschoolbus.models.UploadLocationService;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import java.util.List;

public class MapMonitoringActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GoogleMap map;

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
        List<User> monitoringUsers = User.getLoginUser().getMonitorsUsers();
        for (User user : monitoringUsers) {
            if (user.getLastGpsLocation() != null) {
                LatLng userLatLng = new LatLng(Double.parseDouble(user.getLastGpsLocation().getLat()), Double.parseDouble(user.getLastGpsLocation().getLng()));
                MapUtil.setMarker(map, userLatLng, user.getName(), user.getLastGpsLocation().getTimestamp());
            }
        }
    }

    private void updateDropdown() {
        Spinner dropdown = findViewById(R.id.spinnerSelectMonitoring);

        String[] items = {"A", "B", "C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                Toast.makeText(this, "A", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "B", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "C", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "wut u doin, fool", Toast.LENGTH_SHORT).show();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MapMonitoringActivity.class);
    }
}
