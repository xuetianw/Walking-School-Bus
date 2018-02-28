package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Monitoring1Fragment extends Fragment {

    private static final String TAG = "Tab1Fragment";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.monitoring1_fragment, container, false);

        updateListView();

        return view;
    }

    private void updateListView() {
        // create list of items
        String[] monitoringList = {"a", "b", "c"};

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

//                Intent intent = CalculateActivity.makeIntent(PotListActivity.this, potCollectionInstance.getPot(position));
//                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You held #" + (position + 1) + ", which is string: " + textView.getText().toString();
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

//                Intent intent = AddPotActivity.makeIntentEdit(PotListActivity.this, potCollectionInstance.getPot(position), position);
//                startActivityForResult(intent, REQUEST_CODE_GETMESSAGE);

                return true;
            }
        });
    }
}
