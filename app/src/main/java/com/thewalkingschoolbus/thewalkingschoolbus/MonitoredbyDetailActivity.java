package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.LOGIN_FAIL_MESSAGE;
import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.SUCCESSFUL_LOGIN_MESSAGE;
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
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MonitoredbyDetailActivity.class);
    }

    private void updateUI() {
        new GetUserAsyncTask(GET_USER_BY_EMAIL, deleteUser,null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result == null){
                    Toast.makeText(getApplicationContext(),LOGIN_FAIL_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(),SUCCESSFUL_LOGIN_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                    deleteUser = (User) result;
                    TextView name = (TextView)findViewById(R.id.textView10);
                    TextView displayName = (TextView)findViewById(R.id.textView12);
                    TextView email = (TextView)findViewById(R.id.textView11);
                    TextView displayEmail = (TextView)findViewById(R.id.textView13);
                    name.setText("Name: ");
                    displayName.setText(""+ deleteUser.getName());
                    deleteUser.getName();
                    email.setText("Email");
                    displayEmail.setText(""+ deleteUser.getEmail());

                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }
}
