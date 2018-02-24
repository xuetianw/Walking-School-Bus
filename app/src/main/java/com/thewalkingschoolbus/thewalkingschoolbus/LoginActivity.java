package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * LoginActivity
 * Description here.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupLoginButton();
    }

    // Temporary - delete after proper login is written.
    private void setupLoginButton() {
        findViewById(R.id.loginButtonid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MonitoringActivity.makeIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
    }
}
