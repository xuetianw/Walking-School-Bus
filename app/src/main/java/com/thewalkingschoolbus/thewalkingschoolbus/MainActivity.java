package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
    DBAdapter myDb;
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

        openDB();

        setupTextviews();
        setupLoginButton();
        setupRegisterButton();
        setUpDisplayButton();
        setUpCleanButton();
        getUserLastState(getApplicationContext());
    }

    private void getUserLastState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AppStates, MODE_PRIVATE);
        registerEmail = preferences.getString(MainActivity.REGISTER_EMAIL, null);
        loginName = preferences.getString(MainActivity.LOGIN_NAME, null);
        loginPassword = preferences.getString(MainActivity.LOGIN_PASSWORD, null);
        if(registerEmail != null && loginName != null && registerEmail != null){
            if(isEmailInDb(registerEmail)){
                login();
            }
        }

    }

    private void login() {
        int rid = findRecordIDFromemail(registerEmail);
        Cursor cursor = myDb.getRow(rid);
        String password = getPassword(cursor);
        String name = getLoginName(cursor);
        checkLoginNameAndPasswordCorrect(name, password);
    }

    private void setUpCleanButton() {
        Button cleanBtn = (Button) findViewById(R.id.cleanID);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb.deleteAll();
            }
        });
    }

    private void setUpDisplayButton() {
        Button displayBTN = (Button) findViewById(R.id.displayid);
        displayBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRecordSet();
            }
        });

    }

    private void setupTextviews() {
        nameET = (EditText)findViewById(R.id.nameid);
        emailET = (EditText)findViewById(R.id.emailid);
        passwordET = (EditText) findViewById(R.id.passwordid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }


    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    private void setupRegisterButton() {
        Button registerButton = (Button) findViewById(R.id.registerid);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginName = nameET.getText().toString();
                registerEmail = emailET.getText().toString();
                loginPassword = passwordET.getText().toString();
                if(loginName.isEmpty()|| registerEmail.isEmpty()|| loginPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"none of the field can be empty", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (isEmailInDb(registerEmail)){
                        Toast.makeText(getApplicationContext(),"this account has been registered", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        onClick_AddRecord(loginName, registerEmail, loginPassword);
                    }
                }

            }
        });
    }

    private boolean isEmailInDb(String registerEmail) {
        Cursor cursor = myDb.getAllRows();
        if (cursor.moveToFirst()){
            do{
                String email = cursor.getString(DBAdapter.COL_EMAIL);
                if(email.equals(registerEmail)){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }


    private int findRecordIDFromemail(String registerEmail) {
        Cursor cursor = myDb.getAllRows();
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String email = cursor.getString(DBAdapter.COL_EMAIL);
                if(email.equals(registerEmail)){
                    return id;
                }
            } while (cursor.moveToNext());
        }
        return -1;
    }

    private void onClick_AddRecord(String loginName, String loginenail, String loginPassword) {
        long newId = myDb.insertRow(loginName, loginenail, loginPassword);
        Toast.makeText(getApplicationContext(),"register succesfully", Toast.LENGTH_SHORT)
                .show();
    }

    // Temporary - delete after proper login is written.
    private void setupLoginButton() {
        Button loginButton = (Button) findViewById(R.id.logoutid);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginName = nameET.getText().toString();
                registerEmail = emailET.getText().toString();
                loginPassword = passwordET.getText().toString();
                if(loginName.isEmpty() || registerEmail.isEmpty()|| loginPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"none of the field can be empty", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (!isEmailInDb(registerEmail)){
                        Toast.makeText(getApplicationContext(),"that account does not exist", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        int rid = findRecordIDFromemail(registerEmail);
                        Cursor cursor = myDb.getRow(rid);
                        String password = getPassword(cursor);
                        String name = getLoginName(cursor);
                        checkLoginNameAndPasswordCorrect(name, password);
                        storeUserInfoToSharePreferences();
                    }
                }
            }
        });
    }

    private void storeUserInfoToSharePreferences() {
        SharedPreferences preferences = getSharedPreferences(AppStates, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(REGISTER_EMAIL, registerEmail );
        editor.putString(LOGIN_NAME, loginName );
        editor.putString(LOGIN_PASSWORD, loginPassword );
        editor.commit();

    }

    private void checkLoginNameAndPasswordCorrect(String name, String password) {

        if(!password.equals(loginPassword) && !name.equals(loginName)){
            Toast.makeText(getApplicationContext(),"password and name both are not correct", Toast.LENGTH_SHORT)
                    .show();
        } else if(!password.equals(loginPassword)){
            Toast.makeText(getApplicationContext(),"password is not correct", Toast.LENGTH_SHORT)
                    .show();
        } else if(!name.equals(loginName)){
            Toast.makeText(getApplicationContext(),"login name is not correct", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(),"login successfully", Toast.LENGTH_SHORT)
                    .show();

            Intent intent = MonitoringActivity.makeIntent(MainActivity.this);
            startActivity(intent);
        }
    }

    private String getPassword(Cursor cursor) {
       return cursor.getString(DBAdapter.COL_PASSWORD);
    }
    private String getLoginName(Cursor cursor) {
        return cursor.getString(DBAdapter.COL_NAME);
    }



    // Display an entire recordset to the screen.
    private void displayRecordSet() {
        Cursor cursor = myDb.getAllRows();
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                String email = cursor.getString(DBAdapter.COL_EMAIL);
                String password = cursor.getString(DBAdapter.COL_PASSWORD);

                // Append data to the message:
                message += "id=" + id
                        +", name=" + name
                        +", email=" + email
                        +", password=" + password
                        +"\n";
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();

        System.out.println(message);
        System.out.println("1111111111111111111111111111111111111111111111111111111111111111");
    }
}
