package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

/*
* SOURCES - Based on following tutorials:
* - Modern Profile UI Design in Android Studio
*    https://www.youtube.com/watch?v=2pirZvqXza0
* */

public class ProfileFragment extends android.app.Fragment {

    private static final String TAG = "ProfileFragment";
    private View view;
    TextView profileNametv, profileEmailtv, profileCellphonetv,
            profileAddresstv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpEditProfileBtn();
        setupTextViews();



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

    private void setupTextViews() {
        profileNametv = (TextView)view.findViewById(R.id.profileNameid);
        profileEmailtv = (TextView)view.findViewById(R.id.profile_activity_user_email);
        profileCellphonetv = (TextView)view.findViewById(R.id.profile_activity_user_cellid);
        profileAddresstv = (TextView)view.findViewById(R.id.profile_activity_user_address);
        getUserLastState();


        if(User.getLoginUser().getName() !=  null){
            profileNametv.setText("" +  User.getLoginUser().getName());
        }
        if(User.getLoginUser().getEmail() != null){
            profileEmailtv.setText("" +  User.getLoginUser().getEmail());
        }
        if(User.getLoginUser().getCellPhone() != null){
            profileCellphonetv.setText("" + User.getLoginUser().getCellPhone());
        }
        if(User.getLoginUser().getAddress() != null){
            profileAddresstv.setText("" +User.getLoginUser().getAddress());
        }

    }

    private void getUserLastState() {

    }

    private void setUpEditProfileBtn(){
        Button editBtn = view.findViewById(R.id.edit_profile_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UserProfileActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }
}
