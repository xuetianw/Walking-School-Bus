package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.CREATE_MONITORING;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_EMAIL;

public class AddMonitoringActivity extends AppCompatActivity {

    private User addMonitoringUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitoring);

        setupAddButton();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddMonitoringActivity.class);
    }

    private void setupAddButton() {
        Button button = findViewById(R.id.btnAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.fieldEmail);
                if(email.getText().toString().equals("")){
                    Toast.makeText(AddMonitoringActivity.this,"please enter user email" , Toast.LENGTH_SHORT).show();
                }else{
                    addMonitoringUser(email.getText().toString());
                }
            }
        });
    }

    private void addMonitoringUser(String email){
        addMonitoringUser = new User();
        addMonitoringUser.setEmail(email);
        new GetUserAsyncTask(GET_USER_BY_EMAIL,addMonitoringUser , null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result != null){
                    addMonitoringUser = (User) result;
                    createMonitoring();
                }else{
                    Toast.makeText(AddMonitoringActivity.this,"unable to find this email" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddMonitoringActivity.this,"unable to find this email" , Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void createMonitoring(){
        new GetUserAsyncTask(CREATE_MONITORING, User.getLoginUser(), addMonitoringUser, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result != null){
                    Toast.makeText(AddMonitoringActivity.this,"user added" , Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(AddMonitoringActivity.this,"unable to add user" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddMonitoringActivity.this,"unable to add user" , Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}

 /*
    private void returnData() {

        // Extract email.
        EditText fieldEmail = findViewById(R.id.fieldEmail);
        String email = fieldEmail.getText().toString().trim();

        // Pass data back.
        Intent intent = new Intent();
        intent.putExtra(RESULT_EMAIL, email);

        // Set result.
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    */

   /*

    private boolean errorCheckInput() {
        EditText field = findViewById(R.id.fieldEmail);
        if (field.length() == 0) {
            toastErrorMessage("Email cannot be empty.");
            return false;
        }
        if (!field.getText().toString().contains("@")) {
            toastErrorMessage("Invalid email address.");
            return false;
        }

        toastErrorMessage("Email does not exist.");
        return false;
    }
    */

    /*
    public static User getUserFromIntent(Intent data) {
        // TODO: RETURN USER BASED ON CORRECT DATABASE
        String email = data.getStringExtra(RESULT_EMAIL).trim();
//        for (User user:MainMenuActivity.registeredUsers) {
//            if (user.getEmail().trim().equalsIgnoreCase(email)) {
//                return user;
//            }
//        }
        return null;
    }
    */