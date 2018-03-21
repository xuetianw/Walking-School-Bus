package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_EMAIL;

public class MonitoredbyDetailActivity extends AppCompatActivity {

    public static String userEmail;
    static User deleteUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoredby_detail);

        deleteUser.setEmail(userEmail);
        updateUI();
        setupStopBeingMonitoredBtn();
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, MonitoredbyDetailActivity.class);
    }

    private void updateUI() {
        new GetUserAsyncTask(GET_USER_BY_EMAIL, deleteUser,null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                deleteUser = (User) result;
                TextView name = (TextView)findViewById(R.id.textView10);
                TextView displayName = (TextView)findViewById(R.id.textView12);
                TextView email = (TextView)findViewById(R.id.textView11);
                TextView displayEmail = (TextView)findViewById(R.id.textView13);
                name.setText("Name: ");
                displayName.setText(""+ deleteUser.getName());
                deleteUser.getName();
                email.setText("Email: ");
                displayEmail.setText(""+ deleteUser.getEmail());
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to retrieve user.", Toast.LENGTH_SHORT)
                        .show();
                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void setupStopBeingMonitoredBtn() {
        Button btn = (Button) findViewById(R.id.steopBeingMoniID);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
