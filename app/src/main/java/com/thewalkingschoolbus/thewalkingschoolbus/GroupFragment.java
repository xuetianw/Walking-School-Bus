package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.thewalkingschoolbus.thewalkingschoolbus.R;

public class GroupFragment extends android.app.Fragment {

    private static final String TAG = "GroupFragment";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_group, container, false);
        populateListView();
        setUpAddButton();
        return view;

        /*
        * How to add content in fragment:
        *
        * Fragments function identical to regular activities, except it does not extend from AppCompatActivity.
        * Hence, some things such as findViewByID or executing context related code works differently.
        *
        * FindViewBYId Example
        * instead   of: Button btn = findViewById(R.id.example);
        *           do: Button btn = view.findViewById(R.id.example);
        *
        * Context Example
        * Instead   of: Toast.makeText(this, "example", Toast.LENGTH_SHORT).show()
        *           do: Toast.makeText(getActivity(), "example.", Toast.LENGTH_SHORT).show()
        *
        * If this is unclear, look at example code in MonitoringFragment.
        */
    }
    private void populateListView(){
        // create list of item
        String[] myItems = {"lol 1","lol 2"};
        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.group_entry,myItems);
        // configure the list view
        ListView list = view.findViewById(R.id.groupListViewId);
        list.setAdapter(adapter);

        //registerClickCallback
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = view.findViewById(R.id.groupListViewId);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You clicked #" + position +"string is " +textView.getText().toString();
            }
        });
    }

    private void setUpAddButton(){
        FloatingActionButton btn = view.findViewById(R.id.addGroupBut);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = JoinOrCreateGroupActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }
}
