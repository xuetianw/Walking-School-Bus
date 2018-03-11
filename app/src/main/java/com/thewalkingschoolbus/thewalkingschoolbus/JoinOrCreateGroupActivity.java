package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class JoinOrCreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_or_create_group);
    }
    private void setUpJoinGroupBut(){
        TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.errorMessage);
        inputLayout.setError("Group Id is required"); // show error
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, JoinOrCreateGroupActivity.class);
    }
}
