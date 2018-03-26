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
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.*;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

/*
* SOURCES - Based on following tutorials:
* - Modern Profile UI Design in Android Studio
*    https://www.youtube.com/watch?v=2pirZvqXza0
* */

public class ProfileFragment extends android.app.Fragment {

    private static final String TAG = "ProfileFragment";
    private View view;

    private TextView profileNametv, profileEmailtv,
            birthYeartv, birthMonthtv, profileAddresstv,
            profileCellphonetv, homePhonetv, gradetv,
            teacherNametv, emergencyContactInfotv;
    private String loginName, registerEmail,
            birthYear, birthMonth, address, cellPhone, homePhone,
            grade, teacherName, emergencyContactInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        setupTextViews();
        setUpEditProfileBtn();

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

    @Override
    public void onResume() {
        super.onResume();
        setupTextViews();
    }

    private void setupTextViews() {
        profileNametv = (TextView)view.findViewById(R.id.profileNameid);
        profileEmailtv = (TextView)view.findViewById(R.id.profileEmailid);
        birthYeartv = (TextView)view.findViewById(R.id.profileBrithdayYearid);
        birthMonthtv = (TextView)view.findViewById(R.id.profileBirthdayMonthid);
        profileAddresstv = (TextView)view.findViewById(R.id.profileaddressid);
        profileCellphonetv = (TextView)view.findViewById(R.id.profileCellphoneNumberid);
        homePhonetv = (TextView)view.findViewById(R.id.profilehomPhonNumberid);
        gradetv = (TextView)view.findViewById(R.id.profileGradeid);
        teacherNametv = (TextView)view.findViewById(R.id.profileteachernameid);
        emergencyContactInfotv = (TextView)view.findViewById(R.id.profileemergenceyid);


        new GetUserAsyncTask(GET_USER_BY_EMAIL, User.getLoginUser(),null, null, null,new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result == null){
                } else {
                    User.setLoginUser((User)result);
                    if(User.getLoginUser().getName() !=  null){
                        profileNametv.setText("" +  User.getLoginUser().getName());
                    }
                    if(User.getLoginUser().getEmail() != null){
                        profileEmailtv.setText("" +  User.getLoginUser().getEmail());
                    }
                    if(User.getLoginUser().getBirthMonth() !=  null){
                        birthMonthtv.setText("" +  User.getLoginUser().getBirthMonth());
                    }
                    if(User.getLoginUser().getBirthYear() !=  null){
                        birthYeartv.setText("" +  User.getLoginUser().getBirthYear());
                    }

                    if(User.getLoginUser().getAddress() !=  null){
                        profileAddresstv.setText("" +  User.getLoginUser().getAddress());
                    }

                    if(User.getLoginUser().getCellPhone() != null){
                        profileCellphonetv.setText("" + User.getLoginUser().getCellPhone());
                    }
                    if(User.getLoginUser().getHomePhone() != null){
                        homePhonetv.setText("" + User.getLoginUser().getHomePhone());
                    }
                    if(User.getLoginUser().getGrade() != null){
                        gradetv.setText("" + User.getLoginUser().getGrade());
                    }
                    if(User.getLoginUser().getTeacherName() != null){
                        teacherNametv.setText("" + User.getLoginUser().getTeacherName());
                    }
                    if(User.getLoginUser().getEmergencyContactInfo() != null){
                        emergencyContactInfotv.setText("" + User.getLoginUser().getEmergencyContactInfo());
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();





    }

    private void getUserLastState() {

    }

    private void setUpEditProfileBtn(){
        Button editBtn = view.findViewById(R.id.editinfobtnid);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UserProfileActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }
}
