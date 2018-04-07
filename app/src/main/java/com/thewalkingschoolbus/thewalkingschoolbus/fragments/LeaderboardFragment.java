package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    TextView winnerName;
    TextView winnderPoints;

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

                ArrayAdapter<String> adapter = new MyListadapter();
                ListView list = view.findViewById(R.id.listViewLeaderboard);
                list.setAdapter(adapter);

            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private class MyListadapter extends ArrayAdapter<String> {
        public MyListadapter(){
            super(getActivity(), R.layout.item_view, arrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View itemView = convertView;
            if ( itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //find the gold to work with

            String string = arrayList.get(position);
            int iconid;
            if(position == 0){
                iconid = R.drawable.gold_medal;
            } else if ( position == 1) {
                iconid = R.drawable.silver_medal;
            } else if ( position == 2 ) {
                iconid = R.drawable.bronze_medal;
            } else {
                iconid = R.drawable.plainmedal;
            }
            ImageView imageView = (ImageView)itemView.findViewById(R.id.item_iconlid);
            imageView.setImageResource(iconid);

            winnerName = (TextView) itemView.findViewById(R.id.nameofgoldmedalwiner);
            winnderPoints = (TextView) itemView.findViewById(R.id.totalpointesid);

            winnerName.setText("Name: " + userList.get(position).getName());

            winnderPoints.setText("Total Points: " + userList.get(position).getTotalPointsEarned());

            return itemView;

        }



    }

}