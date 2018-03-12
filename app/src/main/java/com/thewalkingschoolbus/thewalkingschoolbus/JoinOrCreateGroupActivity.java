package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.ADD_MEMBER_TO_GROUP;

public class JoinOrCreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_or_create_group);
        setUpJoinGroupBut();
    }
    private void setUpJoinGroupBut(){
        Button button = findViewById(R.id.joinGroupBut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroupByIdHandling();
            }
        });
    }

    private void joinGroupByIdHandling(){
        EditText groupIdEditText = findViewById(R.id.groupIdField);
        if(groupIdEditText.toString().equals("")){
            TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.errorMessage);
            inputLayout.setError("Group Id is required"); // show error
        }else{
            new GetUserAsyncTask(ADD_MEMBER_TO_GROUP, null, null, null, null, new OnTaskComplete() {
                @Override
                public void onSuccess(Object result) {
                    Toast.makeText(JoinOrCreateGroupActivity.this, "joined group", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Exception e) {
                }
            });
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, JoinOrCreateGroupActivity.class);
    }
}
