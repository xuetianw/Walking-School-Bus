package com.thewalkingschoolbus.thewalkingschoolbus;

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

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class MonitoringFragment extends Fragment {

    private static final String TAG = "MonitoringFragment";
    public static final int DELETE_MONITORING_REQUEST_CODE = 100;
    private View view;
    List<String> monitoringList;
    User []users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (container != null) {
//            container.removeAllViews();
//        }
        view = inflater.inflate(R.layout.fragment_monitoring, container, false);

        //updateListView();
        setupAddMonitoringBtn();
        setUpRefresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        new GetUserAsyncTask(USR_MONITORING_LIST, User.getLoginUser(),null, null,new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                monitoringList = new ArrayList<>();
                users = (User[]) result;
                if(users.length == 0){
                    Toast.makeText(getActivity(),"Not monitoring anyone", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(),"Monitoring list updated", Toast.LENGTH_SHORT)
                        .show();
                for(User user: users){
                    monitoringList.add("Name: "+user.getName() + " "+"Email: "+ user.getEmail() );
                }
                // build adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.monitoring_entry, monitoringList);

                // configure the list view
                ListView list = view.findViewById(R.id.listViewMonitoring);
                list.setAdapter(adapter);

                // update clicks
                registerClickCallback();
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(),"Unable to update the list", Toast.LENGTH_SHORT)
                        .show();
                Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void registerClickCallback() {
        ListView list = view.findViewById(R.id.listViewMonitoring);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                MonitoringDetailActivity.userEmail = users[position].getEmail();
                Intent intent = MonitoringDetailActivity.makeIntent(getActivity());
                startActivityForResult(intent, DELETE_MONITORING_REQUEST_CODE);
            }
        });
    }

    private void setupAddMonitoringBtn() {
        FloatingActionButton btn = view.findViewById(R.id.btnAddMonitoring);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddMonitoringActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }

    private void setUpRefresh(){
        final SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.swiperefreshForMonitoring);
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
            case DELETE_MONITORING_REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    new GetUserAsyncTask(DELETE_MONITORING, User.getLoginUser(), MonitoringDetailActivity.deleteUser, null, new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(getActivity().getApplicationContext(),"Removed user", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity().getApplicationContext(),"Failed to remove user", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }).execute();
                }
                break;
        }
    }
}
