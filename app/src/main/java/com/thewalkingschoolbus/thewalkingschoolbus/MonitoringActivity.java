package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * MonitoringActivity
 * Description here.
 */
public class MonitoringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        setupLogoutBtn();
    }

    private void setupLogoutBtn() {
        Button logouButton = (Button) findViewById(R.id.loginid);
        logouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanSharedPreferences();
                finish();
            }
        });
    }

    private void cleanSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(MainActivity.AppStates, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(MainActivity.REGISTER_EMAIL, null );
        editor.putString(MainActivity.LOGIN_NAME, null );
        editor.putString(MainActivity.LOGIN_PASSWORD, null );
        editor.commit();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MonitoringActivity.class);
    }
}
   