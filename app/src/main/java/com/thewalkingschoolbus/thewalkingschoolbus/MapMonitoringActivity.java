package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MapMonitoringActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_monitoring);

        updateDropdown();
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
