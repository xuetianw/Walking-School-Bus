package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.*;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class RegisterActivity extends AppCompatActivity {
    EditText nameET;
    EditText emailET;
    EditText passwordET;
    String loginName;
    String registerPassword;
    String registerEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupTextviews();
        setupRegisterButton();
    }

    private void setupRegisterButton() {
        Button registerBtn = (Button)findViewById(R.id.register2id);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginName = nameET.getText().toString();
                registerEmail = emailET.getText().toString();
                registerPassword = passwordET.getText().toString();

                if(loginName.isEmpty() || registerEmail.isEmpty()|| registerPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),MainActivity.FIELD_NOT_EMPTY_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                }

                User user = new User();
                user.setEmail(registerEmail);
                user.setName(loginName);
                user.setPassword(registerPassword);

                new GetUserAsyncTask(CREATE_USER, user,null, null,null ,new OnTaskComplete() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(getApplicationContext(),REGISTER_SUCCESSFULLY_MESSAGE, Toast.LENGTH_SHORT)
                                    .show();
                        //Intent intent = MonitoringActivity.makeIntent(MainActivity.this);
                        //startActivity(intent);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(),REGISTER_FAIL_MESSAGE, Toast.LENGTH_SHORT)
                                .show();
                        Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).execute();

            }
        });
    }

    private void setupTextviews() {
        nameET = (EditText)findViewById(R.id.registernameid);
        emailET = (EditText)findViewById(R.id.registeremailid);
        passwordET = (EditText) findViewById(R.id.registerpasswordid);
    }

    public static Intent makeIntent(MainActivity mainActivity) {
        return new Intent(mainActivity, RegisterActivity.class);
    }
}
