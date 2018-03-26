package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.Message;

public class MessageViewActivity extends AppCompatActivity {

    private static final String GIVEN_MESSAGE_ID = "ca.sfu.servingsizecalculator.CalculateActivity - messageId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);
    }

    public static Intent makeIntent(Context context, Message message) {
        Intent intent = new Intent(context, MessageViewActivity.class);
        intent.putExtra(GIVEN_MESSAGE_ID, message.getId());
        return intent;
    }

    private int extractMessageIdFromIntent() {
        Intent intent = getIntent();
        return Integer.getInteger(intent.getStringExtra(GIVEN_MESSAGE_ID).trim());
    }
}
