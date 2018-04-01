package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Message;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.POST_MESSAGE_TO_GROUP;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.POST_MESSAGE_TO_PARENTS;

public class MessageNewActivity extends AppCompatActivity {

    private GetUserAsyncTask.functionType messageTo;
    private List<String> strArrOfGroups;
    private List<Group> listOfGroups;
    private User loginUser;
    private RadioGroup radioGroup;
    private Message message;
    private boolean isEmergency;

    private RadioButton buttonEmergency;
    private RadioButton buttonMessageAsMember;
    private RadioButton buttonMessageAsLeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_new);

        createRadioButtons();
        setupSendMessageBtn();
    }

    private void createRadioButtons() {
        radioGroup = findViewById(R.id.radioGroupSendAs);

        buttonEmergency = new RadioButton(this);
        buttonEmergency.setText("EMERGENCY");
        radioGroup.addView(buttonEmergency);
        buttonEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonMessageAsMember = new RadioButton(this);
        buttonMessageAsMember.setText("AS MEMBER TO PARENT");
        radioGroup.addView(buttonMessageAsMember);
        buttonMessageAsMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonMessageAsLeader = new RadioButton(this);
        buttonMessageAsLeader.setText("AS LEADER TO GROUP");
        radioGroup.addView(buttonMessageAsLeader);
        buttonMessageAsLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setupSendMessageBtn() {
        Button sendButton = findViewById(R.id.messageSendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForCondition();
                sendMessage();
            }
        });
    }

    private void checkForCondition(){

        isEmergency = false;

        if(buttonEmergency.isChecked()){
            isEmergency = true;
            messageTo = POST_MESSAGE_TO_PARENTS;
        }
        if(buttonMessageAsLeader.isChecked()){
            messageTo = POST_MESSAGE_TO_GROUP;
        }
        if(buttonMessageAsMember.isChecked()){
            messageTo = POST_MESSAGE_TO_PARENTS;
        }
    }

    private void sendMessage() {
        EditText messageBoxEditText = findViewById(R.id.messageBoxEditText);

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
        message = new Message(text, isEmergency);

        if(messageTo == POST_MESSAGE_TO_GROUP){
            updateGroupList();
        } else{
            pushMessage(null);
        }
    }

    private void updateGroupList(){
        new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
               User.setLoginUser((User)result);
               loginUser = (User) result;
               stringPrep();
            }

            @Override
            public void onFailure(Exception e) {

            }
        }).execute();

    }
    private void stringPrep(){

        int i = 0;
        Group tmpGroup;
        listOfGroups = new ArrayList<>();
        strArrOfGroups = new ArrayList<>();

        if(loginUser.getLeadsGroups().isEmpty() == false) {
            for (i = 0; i < loginUser.getLeadsGroups().size(); i++) {
                tmpGroup = loginUser.getLeadsGroups().get(i);
                listOfGroups.add(i, tmpGroup);
                strArrOfGroups.add(i, "Group Id:" + tmpGroup.getId() + " (leader)");
            }
        }else{
            Toast.makeText(MessageNewActivity.this,"NOT LEADING ANY GROUP",Toast.LENGTH_SHORT).show();
            return;
        }

/*
        if(loginUser.getMemberOfGroups().isEmpty() == false) {
            for (int j = 0; j < loginUser.getMemberOfGroups().size(); j++) {
                tmpGroup = loginUser.getMemberOfGroups().get(j);
                listOfGroups.add(i, tmpGroup);
                strArrOfGroups.add(i, "Group Id:" + tmpGroup.getId());
                i++;
            }
        }
*/

        alertDialog(strArrOfGroups.toArray(new String[0]));
    }




    private void alertDialog(String[] strArr){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send Message To Group");
        builder.setItems(strArr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                pushMessage(listOfGroups.get(item));
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    private void pushMessage(Group group) {
        new GetUserAsyncTask(messageTo, User.getLoginUser(),null, group, message, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(MessageNewActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MessageNewActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
