package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.LOGIN_FAIL_MESSAGE;
import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.SUCCESSFUL_LOGIN_MESSAGE;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class MonitoredByFragment extends android.app.Fragment {

    private View view;
    List<String> monitoredList;
    User []users;
    public static int DELETE_BEING_MONITORED_REQUEST_CODE = 99;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_monitored_by, container, false);


        updateListView();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        updateListView();
    }

    private void updateListView() {
        new GetUserAsyncTask(USER_MONITORING_BY_LIST, MainActivity.loginUser,null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result == null){
                    Toast.makeText(getActivity().getApplicationContext(),LOGIN_FAIL_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    monitoredList = new ArrayList<>();
                    Toast.makeText(getActivity().getApplicationContext(),SUCCESSFUL_LOGIN_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                    users = (User[]) result;
                    for(User user: users){
                        System.out.println(user);
                        monitoredList.add(user.getName() + "    "+ user.getEmail() );
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
                MonitoredbyDetailActivity.userEmail = users[position].getEmail();
                Intent intent = MonitoredbyDetailActivity.makeIntent(getActivity());
                startActivityForResult(intent, DELETE_BEING_MONITORED_REQUEST_CODE);
                return true;
            }
        });
    }
}
