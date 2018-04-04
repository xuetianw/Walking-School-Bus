package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.activities.MonitoringDetailActivity;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;

public class LeaderboardFragment extends android.app.Fragment {

    private static final String TAG = "LeaderboardFragment";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        updateListView();
        return view;
    }

    private void updateListView() {
//        monitoringList = new ArrayList<>();
//        users = (User[]) result;
//        if(users.length == 0){
//            Toast.makeText(getActivity(),"Not monitoring anyone", Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(getActivity(),"Monitoring list updated", Toast.LENGTH_SHORT)
//                .show();
//        for(User user: users){
//            monitoringList.add("Name: "+ user.getName() + " "+"Email: "+ user.getEmail() );
//        }
//        // build adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.leaderboard_entry, monitoringList);
//
//        // configure the list view
//        ListView list = view.findViewById(R.id.listViewLeaderboard);
//        list.setAdapter(adapter);
//
//        // update clicks
//        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = view.findViewById(R.id.listViewLeaderboard);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

            }
        });
    }
}