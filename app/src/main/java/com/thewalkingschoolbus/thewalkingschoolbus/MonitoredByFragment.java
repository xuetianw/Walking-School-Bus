package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class MonitoredByFragment extends android.app.Fragment {

    private View view;
    List<String> monitoredList;
    User []users;
    final public static int DELETE_BEING_MONITORED_REQUEST_CODE = 99;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_monitored_by, container, false);


        updateListView();
        setUpAddMonitoredByBut();
        setUpRefresh();
        return view;

    }

    private void updateListView() {
        new GetUserAsyncTask(USER_MONITORING_BY_LIST, User.getLoginUser(),null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result == null){
                    Toast.makeText(getActivity(),"unable to update the list", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    monitoredList = new ArrayList<>();
                    Toast.makeText(getActivity(),"successfully update the list", Toast.LENGTH_SHORT)
                            .show();
                    users = (User[]) result;
                    for(User user: users){
                        System.out.println(user);
                        monitoredList.add("Name: "+user.getName() + " "+"Email: "+ user.getEmail() );
                    }
                    // build adapter
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.monitoring_entry, monitoredList);

                    // configure the list view
                    ListView list = getActivity().findViewById(R.id.listViewMonitored);
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
        ListView list = view.findViewById(R.id.listViewMonitored);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                MonitoredbyDetailActivity.userEmail = users[position].getEmail();
                Intent intent = MonitoredbyDetailActivity.makeIntent(getActivity());
                startActivityForResult(intent, DELETE_BEING_MONITORED_REQUEST_CODE);
            }
        });

    }

    private void setUpAddMonitoredByBut() {
        FloatingActionButton but = view.findViewById(R.id.addMonitoredByBut);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddMonitoredByActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }

    private void setUpRefresh(){
        final SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.swiperefreshForMonitoredBy);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateListView();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DELETE_BEING_MONITORED_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    new GetUserAsyncTask(DELETE_MONITORING, MonitoredbyDetailActivity.deleteUser, User.getLoginUser(), null, null, new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            if(result == null){
                                Toast.makeText(getActivity(),"unable to delete monitoring", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(getActivity(),"delete Monitoring", Toast.LENGTH_SHORT)
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
