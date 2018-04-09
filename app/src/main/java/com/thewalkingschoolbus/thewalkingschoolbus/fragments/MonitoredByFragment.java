package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.activities.AddMonitoredByActivity;
import com.thewalkingschoolbus.thewalkingschoolbus.activities.MonitoredbyDetailActivity;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class MonitoredByFragment extends Fragment {

    private static final String TAG = "MonitoredByFragment";
    final public static int DELETE_MONITORED_BY_REQUEST_CODE = 99;
    private View view;
    List<String> monitoredList;
    User []users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_monitored_by, container, false);

        //updateListView();
        setUpAddMonitoredByBut();
        setUpRefresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        new GetUserAsyncTask(USER_MONITORING_BY_LIST, User.getLoginUser(),null, null, null,new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                monitoredList = new ArrayList<>();
                users = (User[]) result;
                if(users.length == 0){
                    Toast.makeText(getActivity(),"Not monitored by anyone", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(),"Monitored by list updated", Toast.LENGTH_SHORT)
                        .show();
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
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Unable to update the list", Toast.LENGTH_SHORT)
                        .show();
                Toast.makeText(getActivity().getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                startActivityForResult(intent, DELETE_MONITORED_BY_REQUEST_CODE);
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
            case DELETE_MONITORED_BY_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    new GetUserAsyncTask(DELETE_MONITORING, MonitoredbyDetailActivity.deleteUser, User.getLoginUser(), null,null, new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {

                            Toast.makeText(getActivity(),"delete Monitoring", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity(),"unable to delete monitoring", Toast.LENGTH_SHORT)
                                    .show();
                            Toast.makeText(getActivity().getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).execute();
                }
                break;
        }
    }
}
