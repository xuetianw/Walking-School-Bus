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
    public static final String FIELD_NOT_EMPTY_MESSAGE = "none of the field can be empty";
    public static final String ACCOUNT_HAS_BEEN_REGISTERED_MESSAGE = "this account has been registered";
    public static final String REGISTER_SUCCESSFULLY_MESSAGE = "register succesfully";
    public static final String ACCOUNT_DOES_NOT_EXIST_MESSAGE = "that account does not exist";
    public static final String PASSWORD_AND_NAME_NOT_CORRECT_MESSAGE = "password and name both are not correct";
    public static final String PASSWORD_NOT_CORRECT_MESSAGE = "password is not correct";
    public static final String LOGIN_NAME_NOT_CORRECT_MESSAGE = "login name is not correct";
    public static final String SUCCESSFUL_LOGIN_MESSAGE = "login successfully";
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
        if(registerEmail != null && loginName != null &&
                ifLoginNameAndPasswordCorrect(registerEmail,loginName,loginPassword )) {
            proceedLogin();
        }
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
                    Toast.makeText(getApplicationContext(), FIELD_NOT_EMPTY_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (isEmailInDb(registerEmail)){
                        Toast.makeText(getApplicationContext(), ACCOUNT_HAS_BEEN_REGISTERED_MESSAGE, Toast.LENGTH_SHORT)
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


    private int findRecordIDFromEmail(String registerEmail) {
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

    private void onClick_AddRecord(String loginName, String loginEnail, String loginPassword) {
        long newId = myDb.insertRow(loginName, loginEnail, loginPassword);
        Toast.makeText(getApplicationContext(), REGISTER_SUCCESSFULLY_MESSAGE, Toast.LENGTH_SHORT)
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
                    Toast.makeText(getApplicationContext(),FIELD_NOT_EMPTY_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (!isEmailInDb(registerEmail)){
                        Toast.makeText(getApplicationContext(), ACCOUNT_DOES_NOT_EXIST_MESSAGE, Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        if (ifLoginNameAndPasswordCorrect(registerEmail, loginName, loginPassword)){
                            proceedLogin();
                            storeUserInfoToSharePreferences();
                        }
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

    private boolean ifLoginNameAndPasswordCorrect(String registerEmail, String loginName, String loginPassword) {
        int rid = findRecordIDFromEmail(registerEmail);
        Cursor cursor = myDb.getRow(rid);
        String password = getPassword(cursor);
        String name = getLoginName(cursor);

        if(!password.equals(loginPassword) && !name.equals(loginName)){
            Toast.makeText(getApplicationContext(), PASSWORD_AND_NAME_NOT_CORRECT_MESSAGE, Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if(!password.equals(loginPassword)){
            Toast.makeText(getApplicationContext(), PASSWORD_NOT_CORRECT_MESSAGE, Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if(!name.equals(loginName)){
            Toast.makeText(getApplicationContext(), LOGIN_NAME_NOT_CORRECT_MESSAGE, Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else {
            Toast.makeText(getApplicationContext(), SUCCESSFUL_LOGIN_MESSAGE, Toast.LENGTH_SHORT)
                    .show();
            return true;
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

    private void proceedLogin() {
        Intent intent = MainMenuActivity.makeIntent(MainActivity.this);
        startActivity(intent);
    }
}
