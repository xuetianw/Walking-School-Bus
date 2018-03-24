package com.thewalkingschoolbus.thewalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thewalkingschoolbus.thewalkingschoolbus.R;

public class UserDetailActivity extends AppCompatActivity {

    private static final String USER_ID = "USER_ID";
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        extractData();

    }
    public void extractData(){
        Intent intent = getIntent();

    }

    public static Intent makeIntent(Context context,String userID) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(USER_ID,userID);
        return intent;
    }
}
