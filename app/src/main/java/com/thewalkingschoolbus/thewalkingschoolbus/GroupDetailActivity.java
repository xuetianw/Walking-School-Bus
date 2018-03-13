package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_MEMBERS_OF_GROUP;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_ONE_GROUP;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.REMOVE_MEMBER_OF_GROUP;

public class GroupDetailActivity extends AppCompatActivity {
    private static final String GROUP_ID ="GROUP_ID";
    private boolean asyncTaskReadyFlag = false;
    private Group mSelectedGroup;
    private User[] mMembers;
    private int lengthOfMemberList;
    private int positionOfUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        updateLoginUser();
        extractDataAndShowDetail();
        setUpLeaveGroupBut();
    }

    public static Intent makeIntent(Context context, Group mGroup, User user) {
        Intent intent = new Intent(context, GroupDetailActivity.class);
        intent.putExtra(GROUP_ID,mGroup.getId());
        return intent;
    }

    private void updateLoginUser(){
        new GetUserAsyncTask(GET_USER_BY_ID, User.getLoginUser(), null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result!=null){
                    User.setLoginUser((User)result);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }).execute();
    }

    private void extractDataAndShowDetail(){
        Intent intent = getIntent();
        String groupId = intent.getStringExtra(GROUP_ID);
        Log.i("TAG","mSelectedGroup ID:"+groupId);
        mSelectedGroup = new Group();
        mSelectedGroup.setId(groupId);
        new GetUserAsyncTask(GET_ONE_GROUP, null, null, mSelectedGroup, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result != null) {
                    mSelectedGroup = (Group) result;
                    showGroupDetail();
                }else{
                    //Toast.makeText(GroupDetailActivity.this, "fuck", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Exception e) {
                //Toast.makeText(GroupDetailActivity.this, "fuck twice", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void showGroupDetail() {
        TextView groupIdTextView = findViewById(R.id.groupIdField);
        TextView groupDesTextView = findViewById(R.id.groupDesField);
        TextView leaderIdTextView = findViewById(R.id.leaderIdField);
        TextView leaderNameTextView = findViewById(R.id.leaderNameField);
        groupIdTextView.setText(mSelectedGroup.getId());
        groupDesTextView.setText(mSelectedGroup.getGroupDescription());
        leaderIdTextView.setText(mSelectedGroup.getLeader().getId());
        leaderNameTextView.setText(mSelectedGroup.getLeader().getName());
        getMembersOfGroup();
    }

    private void getMembersOfGroup(){
        new GetUserAsyncTask(GET_MEMBERS_OF_GROUP, null, null, mSelectedGroup, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result == null){
                    Toast.makeText(GroupDetailActivity.this, "unable to get members of the mSelectedGroup", Toast.LENGTH_LONG).show();
                }else{
                    mMembers = (User[]) result;
                    stringsPrep();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(GroupDetailActivity.this, "Exception", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void stringsPrep(){
        String[] mMemberDisplay;
        if(mMembers == null){
            mMemberDisplay = new String[0];
            lengthOfMemberList = 0;
        }else {
            mMemberDisplay = new String[mMembers.length];
            lengthOfMemberList = mMembers.length;
        }
        List<User> listMonitoring = User.getLoginUser().getMonitorsUsers();
        for(int i = 0; i < lengthOfMemberList;i++){
            String str = "";
            for(int j = 0; j < listMonitoring.size();j++){
                if(mMembers[i].getId().equals(listMonitoring.get(j).getId())){
                    str = "Name: "+mMembers[i].getName() +" "+"Email: "+mMembers[i].getEmail() + " "+"(monitoring)";
                    break;
                }else{
                    str = "Name: "+mMembers[i].getName() +" "+"Email: "+mMembers[i].getEmail();
                }
            }
            mMemberDisplay[i]=str;
        }

        populateListView(mMemberDisplay);
    }

    private void populateListView(String[] mGroupDisplay){
        // create list of item
        String[] myItems = mGroupDisplay;
        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(GroupDetailActivity.this, R.layout.group_entry,myItems);
        // configure the list view
        ListView list = findViewById(R.id.memberListView);
        list.setAdapter(adapter);

        //registerClickCallback
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.memberListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                positionOfUser = position;
                List<User> listMonitoring = User.getLoginUser().getMonitorsUsers();

                for(int j = 0; j < listMonitoring.size();j++){
                    if(mMembers[position].getId().equals(listMonitoring.get(j).getId())){
                        alertDialog();
                        break;
                    }else{
                        Toast.makeText(GroupDetailActivity.this, "you can not remove " +
                                "user you are not monitoring from group",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void alertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(GroupDetailActivity.this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Do u want to remove this user from the group");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeUserFromGroup();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void removeUserFromGroup(){
        new GetUserAsyncTask(REMOVE_MEMBER_OF_GROUP, mMembers[positionOfUser], null, mSelectedGroup, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result != null){
                    Toast.makeText(GroupDetailActivity.this, "successfully removed",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(GroupDetailActivity.this, "unsuccessfully removed",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(GroupDetailActivity.this, "unsuccessfully removed",Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void setUpLeaveGroupBut(){
        Button but = findViewById(R.id.leaveGroupBut);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.getLoginUser().getId().equals(mSelectedGroup.getId())){
                    Toast.makeText(GroupDetailActivity.this, "cant leave group as leader",Toast.LENGTH_LONG).show();
                }else{
                    leaveGroup();
                }
            }
        });
    }
    private void leaveGroup(){
        new GetUserAsyncTask(REMOVE_MEMBER_OF_GROUP, User.getLoginUser(), null, mSelectedGroup, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result!= null){
                    Toast.makeText(GroupDetailActivity.this, "successfully leave group",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(GroupDetailActivity.this, "unsuccessfully leave group",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(GroupDetailActivity.this, "Execption",Toast.LENGTH_LONG).show();

            }
        }).execute();
    }
}

