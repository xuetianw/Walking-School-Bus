package com.thewalkingschoolbus.thewalkingschoolbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MessageNewActivity extends AppCompatActivity {

    private boolean messageAsMember = false;
    private boolean messageAsLeader = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_new);

        createRadioButtons();
    }

    private void createRadioButtons() {
        // Create buttons for mines option
        RadioGroup radioGroup = findViewById(R.id.radioGroupTo);

        RadioButton buttonMessageAsMember = new RadioButton(this);
        buttonMessageAsMember.setText("AS MEMBER");
        radioGroup.addView(buttonMessageAsMember);

        buttonMessageAsMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageAsMember = true;
                messageAsLeader = false;
            }
        });

        RadioButton buttonMessageAsLeader = new RadioButton(this);
        buttonMessageAsLeader.setText("AS LEADER");
        radioGroup.addView(buttonMessageAsLeader);

        buttonMessageAsLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageAsMember = false;
                messageAsLeader = true;
            }
        });
    }
}
