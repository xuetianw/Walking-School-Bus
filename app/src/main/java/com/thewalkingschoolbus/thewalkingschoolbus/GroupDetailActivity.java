package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_ONE_GROUP;

public class GroupDetailActivity extends AppCompatActivity {
    private static final String GROUP_ID ="GROUP_ID";
    private Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        extractData();
        showGroupDetail();
    }

    public static Intent makeIntent(Context context, Group mGroup, User user) {
        Intent intent = new Intent(context, GroupDetailActivity.class);
        intent.putExtra(GROUP_ID,mGroup.getId());
        return intent;
    }

    private void extractData(){
        Intent intent = getIntent();
        String groupId = intent.getStringExtra(GROUP_ID);
        group = new Group();
        group.setId(groupId);
        new GetUserAsyncTask(GET_ONE_GROUP, null, null, group, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                group = (Group)result;
            }

            @Override
            public void onFailure(Exception e) {

            }
        }).execute();
    }

    private void showGroupDetail() {

    }
}
