package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.*;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class MonitoringFragment extends android.app.Fragment {
    public static final int DELETE_MORNITORING_REQUEST_CODE = 100;
    private static final String TAG = "MonitoringFragment";
    private static final int REQUEST_CODE_GET_EMAIL = 42;
    private View view;
    List<String> monitoringList;
    User []users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_monitoring, container, false);
        Log.d(TAG, "Starting.");

        updateListView();
        setupAddMonitoringBtn();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateListView();
    }

    private void updateListView() {


        new GetUserAsyncTask(USR_MONITORING_LIST, MainActivity.loginUser,null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result == null){
                    Toast.makeText(getActivity().getApplicationContext(),LOGIN_FAIL_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    monitoringList = new ArrayList<>();
                    Toast.makeText(getActivity().getApplicationContext(),SUCCESSFUL_LOGIN_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                    users = (User[]) result;
                    for(User user: users){
                        System.out.println(user);
                        monitoringList.add(user.getName() + "    "+ user.getEmail() );
                    }
                    // build adapter
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.monitoring_entry, monitoringList);

                    // configure the list view
                    ListView list = view.findViewById(R.id.listViewMonitoring);
                    list.setAdapter(adapter);

                    // update clicks
                    registerClickCallback();
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();



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
                //MainMenuActivity.monitoringUsers.remove(position);
                //updateListView();
                MonitoringDetailActivity.userEmail = users[position].getEmail();
                Intent intent = MonitoringDetailActivity.makeIntent(getActivity());
                startActivityForResult(intent, DELETE_MORNITORING_REQUEST_CODE);
                return true;
            }
        });
    }

    private void setupAddMonitoringBtn() {
        FloatingActionButton btn = view.findViewById(R.id.btnAddMonitoring);
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
//                        MainMenuActivity.monitoringUsers.add(newMonitoringUser);
//                        updateListView();
//                        Toast.makeText(getActivity(), "Added!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Canceled.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Canceled.", Toast.LENGTH_LONG).show();
                }
                break;
            case DELETE_MORNITORING_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    new GetUserAsyncTask(DELETE_MONITORING, loginUser, MonitoringDetailActivity.deleteUser, null, null, new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            if(result == null){
                                Toast.makeText(getActivity().getApplicationContext(),LOGIN_FAIL_MESSAGE, Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),SUCCESSFUL_LOGIN_MESSAGE, Toast.LENGTH_SHORT)
                                        .show();

                            }
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).execute();
                }
                break;
        }
    }
}
