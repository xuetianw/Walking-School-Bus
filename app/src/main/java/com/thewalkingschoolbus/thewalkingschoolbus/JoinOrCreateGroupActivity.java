package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static android.content.ContentValues.TAG;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.ADD_MEMBER_TO_GROUP;

public class JoinOrCreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_or_create_group);
        setUpJoinGroupBut();
        setUpJoinNearByGroupBut();
        setCreateGroupBut();
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, JoinOrCreateGroupActivity.class);
        return intent;
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
            Log.i(TAG, "nothing entered" );

            return;
        }else{
            Group group =  new Group();
            group.setId(groupIdEditText.getText().toString());
            Log.i(TAG, "number entered" );

            new GetUserAsyncTask(ADD_MEMBER_TO_GROUP, User.getLoginUser(), null, group,null, new OnTaskComplete() {
                @Override
                public void onSuccess(Object result) {
                    Toast.makeText(JoinOrCreateGroupActivity.this, "Joined group", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(JoinOrCreateGroupActivity.this, "Unable to join group", Toast.LENGTH_LONG).show();
                    Toast.makeText(JoinOrCreateGroupActivity.this,"Error :" + e.getMessage() , Toast.LENGTH_SHORT).show();

                }
            }).execute();
        }
    }

    private void setUpJoinNearByGroupBut(){
        Button but = (Button) findViewById(R.id.joinNearbyGroupBut);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to map fragment
            }
        });
    }

    private void setCreateGroupBut(){
        Button but = (Button) findViewById(R.id.createGroupBut);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to map fragment
            }
        });
    }
}
