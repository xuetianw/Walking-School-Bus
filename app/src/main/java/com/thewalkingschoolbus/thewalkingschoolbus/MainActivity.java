package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * MainActivity
 * Description here.
 */
public class MainActivity extends AppCompatActivity {

    public static final String REGISTER_EMAIL = "registerEmail";
    public static final String LOGIN_NAME = "loginName";
    public static final String LOGIN_PASSWORD = "loginPassword";
    public static final String FIELD_NOT_EMPTY_MESSAGE = "none of the field can be empty";
    public static final String ACCOUNT_HAS_BEEN_REGISTERED_MESSAGE = "this account has been registered";
    public static final String REGISTER_SUCCESSFULLY_MESSAGE = "register succesfully";
    public static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "that account does not exist";
    public static final String PASSWORD_AND_NAME_NOT_CORRECT_MESSAGE = "password and name both are not correct";
    public static final String PASSWORD_NOT_CORRECT_MESSAGE = "password is not correct";
    public static final String LOGIN_NAME_NOT_CORRECT_MESSAGE = "login name is not correct";
    public static final String SUCCESSFUL_LOGIN_MESSAGE = "login successfully";
    EditText nameET;
    EditText emailET;
    EditText passwordET;
    String loginName;
    String loginPassword;
    String registerEmail;
    public static final String AppStates = "UUERLOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupTextviews();
        setupLoginButton();
        setupRegisterButton();
        getUserLastState(getApplicationContext());
    }

    private void getUserLastState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppStates, MODE_PRIVATE);
        registerEmail = preferences.getString(MainActivity.REGISTER_EMAIL, null);
        loginName = preferences.getString(MainActivity.LOGIN_NAME, null);
        loginPassword = preferences.getString(MainActivity.LOGIN_PASSWORD, null);
        if(registerEmail != null && loginName != null) {
            Intent intent = MonitoringActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        }
    }


    private void setupTextviews() {
        nameET = (EditText)findViewById(R.id.nameid);
        emailET = (EditText)findViewById(R.id.emailid);
        passwordET = (EditText) findViewById(R.id.passwordid);
    }




    private void setupRegisterButton() {
        Button registerButton = (Button) findViewById(R.id.registerid);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginName = nameET.getText().toString();
                registerEmail = emailET.getText().toString();
                loginPassword = passwordET.getText().toString();
                Intent intent = RegisterActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }





    // Temporary - delete after proper login is written.
    private void setupLoginButton() {
        Button loginButton = (Button) findViewById(R.id.loginid);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginName = nameET.getText().toString();
                registerEmail = emailET.getText().toString();
                loginPassword = passwordET.getText().toString();
                if(loginName.isEmpty() || registerEmail.isEmpty()|| loginPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),FIELD_NOT_EMPTY_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (ifLoginNameAndPasswordCorrect(registerEmail, loginName, loginPassword)){
                        Intent intent = MonitoringActivity.makeIntent(MainActivity.this);
                        startActivity(intent);
                        storeUserInfoToSharePreferences();
                    }
                }
            }
        });
    }

    private boolean ifLoginNameAndPasswordCorrect(String registerEmail, String loginName, String loginPassword) {
        return true;
    }

    private void storeUserInfoToSharePreferences() {
        SharedPreferences preferences = getSharedPreferences(AppStates, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(REGISTER_EMAIL, registerEmail );
        editor.putString(LOGIN_NAME, loginName );
        editor.putString(LOGIN_PASSWORD, loginPassword );
        editor.commit();

    }

}
