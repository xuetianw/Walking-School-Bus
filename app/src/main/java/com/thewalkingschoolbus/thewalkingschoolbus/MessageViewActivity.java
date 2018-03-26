package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

public class MessageViewActivity extends AppCompatActivity {

    private static final String GIVEN_MESSAGE_ID = "ca.sfu.servingsizecalculator.CalculateActivity - messageId";
    private static final String GIVEN_MESSAGE_TIMESTAMP = "ca.sfu.servingsizecalculator.CalculateActivity - messageTimestamp";
    private static final String GIVEN_MESSAGE_TEXT = "ca.sfu.servingsizecalculator.CalculateActivity - messageText";
    private static final String GIVEN_MESSAGE_FROM_NAME = "ca.sfu.servingsizecalculator.CalculateActivity - messageFromName";
    private static final String GIVEN_MESSAGE_FROM_EMAIL = "ca.sfu.servingsizecalculator.CalculateActivity - messageFromEmail";
    private static final String GIVEN_MESSAGE_EMERGENCY = "ca.sfu.servingsizecalculator.CalculateActivity - messageEmergency";

    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        extractMessageFromIntent();
        setFrom();
        setTimestamp();
        setText();
    }

    private void setFrom() {
        TextView textViewFrom = findViewById(R.id.messageViewFrom);
        textViewFrom.setText(message.getFromUser().getName() + " (" + message.getFromUser().getEmail() + ")");
    }

    private void setTimestamp() {
        TextView textViewFrom = findViewById(R.id.messageViewTimestamp);
        textViewFrom.setText(message.getTimestamp()); // TODO: check correctness
    }

    private void setText() {
        TextView textViewFrom = findViewById(R.id.messageViewText);
        textViewFrom.setText(message.getText()); // TODO: check correctness
    }

    public static Intent makeIntent(Context context, Message message) {
        Intent intent = new Intent(context, MessageViewActivity.class);
        intent.putExtra(GIVEN_MESSAGE_ID, message.getId());
        intent.putExtra(GIVEN_MESSAGE_TIMESTAMP, message.getTimestamp());
        intent.putExtra(GIVEN_MESSAGE_TEXT, message.getText());
        intent.putExtra(GIVEN_MESSAGE_FROM_NAME, message.getFromUser().getName());
        intent.putExtra(GIVEN_MESSAGE_FROM_EMAIL, message.getFromUser().getEmail());
        intent.putExtra(GIVEN_MESSAGE_EMERGENCY, message.isEmergency());
        return intent;
    }

    private void extractMessageFromIntent() {
        Intent intent = getIntent();
        Message message = new Message();
        message.setId(intent.getStringExtra(GIVEN_MESSAGE_ID));
        message.setTimestamp(intent.getStringExtra(GIVEN_MESSAGE_TIMESTAMP));
        message.setText(intent.getStringExtra(GIVEN_MESSAGE_TEXT));
        User fromUser = new User();
        fromUser.setName(intent.getStringExtra(GIVEN_MESSAGE_FROM_NAME));
        fromUser.setEmail(intent.getStringExtra(GIVEN_MESSAGE_FROM_EMAIL));
        message.setFromUser(fromUser);
        message.setEmergency(intent.getBooleanExtra(GIVEN_MESSAGE_EMERGENCY, false));
        this.message = message;
    }
}
