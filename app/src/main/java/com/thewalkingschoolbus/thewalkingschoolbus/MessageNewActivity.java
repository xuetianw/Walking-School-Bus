package com.thewalkingschoolbus.thewalkingschoolbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_MESSAGES_FOR_USER;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.POST_MESSAGE_TO_GROUP;

public class MessageNewActivity extends AppCompatActivity {

    private GetUserAsyncTask.functionType messageTo;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_new);

        createRadioButtons();
        setupSendMessageBtn();
    }

    private void createRadioButtons() {
        RadioGroup radioGroup = findViewById(R.id.radioGroupSendAs);

        RadioButton buttonEmergency = new RadioButton(this);
        buttonEmergency.setText("EMERGENCY");
        radioGroup.addView(buttonEmergency);
        buttonEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageTo = null;
            }
        });

        RadioButton buttonMessageAsMember = new RadioButton(this);
        buttonMessageAsMember.setText("AS MEMBER TO PARENT");
        radioGroup.addView(buttonMessageAsMember);
        buttonMessageAsMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageTo = GetUserAsyncTask.functionType.POST_MESSAGE_TO_PARENTS;
            }
        });

        RadioButton buttonMessageAsLeader = new RadioButton(this);
        buttonMessageAsLeader.setText("AS LEADER TO GROUP");
        radioGroup.addView(buttonMessageAsLeader);
        buttonMessageAsLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageTo = GetUserAsyncTask.functionType.POST_MESSAGE_TO_GROUP;
            }
        });
    }

    private void setupSendMessageBtn() {
        Button sendButton = findViewById(R.id.messageSendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        EditText messageBoxEditText = findViewById(R.id.messageBoxEditText);

        // Send message emergency
        if (messageTo == null) {
            String text = messageBoxEditText.getText().toString().trim();
            Message message = new Message(text, true);
            pushMessage(GetUserAsyncTask.functionType.POST_MESSAGE_TO_PARENTS, message);
            pushMessage(GetUserAsyncTask.functionType.POST_MESSAGE_TO_GROUP, message);
            return;
        }

        // Check for send conditions
        if (radioGroup.getCheckedRadioButtonId() == -1) { // No radio button checked
            Toast.makeText(this, "Select send as", Toast.LENGTH_SHORT).show();
            return;
        }
        if (messageBoxEditText.getText().toString().isEmpty()) { // No message entered
            Toast.makeText(this, "Enter message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send message
        String text = messageBoxEditText.getText().toString().trim();
        Message message = new Message(text, false);
        pushMessage(messageTo, message);
    }

    private void pushMessage(GetUserAsyncTask.functionType messageTo, Message message) {
        new GetUserAsyncTask(messageTo, null,null, null, message, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(MessageNewActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MessageNewActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
