package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

/**
 * Created by Vaanyi Igiri on 2018-03-19.
 */

public class UserProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
//        String email = User.getLoginUser().getEmail();
//        TextView tv = findViewById(R.id.profile_activity_user_email);
//        tv.setText(email);

    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, UserProfileActivity.class);
    }
}