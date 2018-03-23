package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.CREATE_MONITORING;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_EMAIL;

public class AddMonitoredByActivity extends AppCompatActivity {

    private User addMonitoredUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitored_by);
        setupAddButton();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddMonitoredByActivity.class);
    }

    private void setupAddButton() {
        Button button = findViewById(R.id.addMonitoredByBut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.enterdEmailField);
                if(email.getText().toString().equals("")){
                    Toast.makeText(AddMonitoredByActivity.this,"please enter user email" , Toast.LENGTH_SHORT).show();
                }else{
                    addMonitoringUser(email.getText().toString());
                }
            }
        });
    }

    private void addMonitoringUser(String email){
        addMonitoredUser = new User();
        addMonitoredUser.setEmail(email);
        new GetUserAsyncTask(GET_USER_BY_EMAIL, addMonitoredUser, null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                addMonitoredUser = (User) result;
                createMonitoring();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddMonitoredByActivity.this,"unable to find this email" , Toast.LENGTH_SHORT).show();
                Toast.makeText(AddMonitoredByActivity.this,"Error :" + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void createMonitoring(){
        new GetUserAsyncTask(CREATE_MONITORING, addMonitoredUser, User.getLoginUser(),null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(AddMonitoredByActivity.this,"user added" , Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AddMonitoredByActivity.this,"unable to add user" , Toast.LENGTH_SHORT).show();
                Toast.makeText(AddMonitoredByActivity.this,"Error :" + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
