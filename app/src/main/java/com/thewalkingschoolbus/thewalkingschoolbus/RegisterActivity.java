package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public static Intent makeIntent(MainActivity mainActivity) {
        return new Intent(mainActivity, RegisterActivity.class);
    }
}
