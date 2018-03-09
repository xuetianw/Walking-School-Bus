package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.model.User;

public class AddMonitoringActivity extends AppCompatActivity {

    private static final String RESULT_EMAIL = "com.thewalkingschoolbus.AddMonitoringActivity - email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monitoring);

        setupAddButton();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddMonitoringActivity.class);
    }

    public static User getUserFromIntent(Intent data) {
        // TODO: RETURN USER BASED ON CORRECT DATABASE
        String email = data.getStringExtra(RESULT_EMAIL).trim();
        for (User user:MainMenuActivity.registeredUsers) {
            if (user.getEmail().trim().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    private void setupAddButton() {
        Button button = findViewById(R.id.btnAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorCheckInput()) {
                    returnData();
                }
            }
        });
    }

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
        // TODO: CHECK IF EMAIL IS IN DATABASE, OR IF EMAIL IS ALREADY IN MONITORING LIST
        for (User user:MainMenuActivity.registeredUsers) {
            if (user.getEmail().trim().equalsIgnoreCase(field.getText().toString().trim())) {
                return true;
            }
        }
        toastErrorMessage("Email does not exist.");
        return false;
    }

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

    private void toastErrorMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }
}
