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
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;

import java.util.ArrayList;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class LeaderboardFragment extends android.app.Fragment {

    private static final String TAG = "LeaderboardFragment";
    private View view;
    ArrayList <User> userList = new ArrayList<>();
    User []users;
    public static ArrayList<String> arrayList;

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
        new GetUserAsyncTask(LIST_USERS, null,null, null,
                null,new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                users = (User[]) result;

                for(User user: users){
                    userList.add(user);
                }
                // sort the userList by the total points earned
                java.util.Collections.sort(userList);
                for(User user:userList){
                    System.out.println(user.getTotalPointsEarned());
                }

                arrayList = new ArrayList();
                for(int i = 0; i < userList.size(); i++) {
                    arrayList.add("Ranking: " + i + " totalPointsEarned: " + userList.get(i).getTotalPointsEarned()
                            + " Name: "+ userList.get(i).getName());
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.leaderboard_entry, arrayList);
                ListView list = view.findViewById(R.id.listViewLeaderboard);
                list.setAdapter(adapter);

            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

}