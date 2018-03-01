package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.model.User;

import java.util.ArrayList;
import java.util.List;

public class Monitoring2Fragment extends Fragment {

    private static final String TAG = "Tab2Fragment";
    private static final int REQUEST_CODE_GET_EMAIL = 42;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.monitoring1_fragment, container, false);
        Log.d(TAG, "Starting.");

        updateListView();
        setupAddMonitoring();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateListView();
    }

    private void updateListView() {
        // create list of items
        List<String> monitoringList = new ArrayList<>();
        for (User user:MonitoringActivity.monitoredByUsers) {
            monitoringList.add(user.getName() + " (" + user.getEmail() + ")");
        }

        // build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.monitoring_entry, monitoringList);

        // configure the list view
        ListView list = view.findViewById(R.id.listViewMonitoring);
        list.setAdapter(adapter);

        // update clicks
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = view.findViewById(R.id.listViewMonitoring);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You clicked #" + (position + 1) + ", which is string: " + textView.getText().toString();
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                MonitoringActivity.monitoredByUsers.remove(position);
                updateListView();
                return true;
            }
        });
    }

    private void setupAddMonitoring() {
        Button btn = view.findViewById(R.id.btnAddMonitoring);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddMonitoringActivity();
            }
        });
    }

    private void startAddMonitoringActivity() {
        Intent intent = AddMonitoringActivity.makeIntent(getActivity());
        startActivityForResult(intent, REQUEST_CODE_GET_EMAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_GET_EMAIL:
                if (resultCode == Activity.RESULT_OK) {
                    // TODO: ADD EMAIL TO MONITORING LIST
                    User newMonitoringUser = AddMonitoringActivity.getUserFromIntent(data);
                    if (newMonitoringUser != null) {
                        MonitoringActivity.monitoredByUsers.add(newMonitoringUser);
                        updateListView();
                        Toast.makeText(getContext(), "Added!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Canceled.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Canceled.", Toast.LENGTH_LONG).show();
                }
        }
    }
}
