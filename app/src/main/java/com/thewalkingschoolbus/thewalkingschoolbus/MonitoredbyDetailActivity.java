package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

public class MonitoredbyDetailActivity extends AppCompatActivity {

    public static String userEmail;
    static User deleteUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoredby_detail);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MonitoredbyDetailActivity.class);
    }
}
