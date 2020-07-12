package com.thewalkingschoolbus.thewalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.REMOVE_MEMBER_OF_GROUP;

/*
 * UserDetailActivity.java
 * Used to show user detail.
 */
public class UserDetailActivity extends AppCompatActivity {

    private static final String USER_ID = "USER_ID";
    private static final String GROUP_ID = "GROUP_ID";
    private String userId;
    private String groupId;
    private User user;
    private User[] monitoredByArr;

    private int loopCount = 0;
    private boolean populateListReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        extractData();
        //getUserInfoAndUpdateUI();
        setUpRemoveFromGroupBut();
        setUpRefresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfoAndUpdateUI();
    }

    private void extractData(){
        Intent intent = getIntent();
        userId = intent.getStringExtra(USER_ID);
        groupId = intent.getStringExtra(GROUP_ID);
    }

    private void setUpRemoveFromGroupBut(){
        if(groupId == null){
            Toast.makeText(UserDetailActivity.this,"unknown group",Toast.LENGTH_SHORT).show();
            return;
        }
        Button but = findViewById(R.id.removeUserFromGroupBut);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> monitorsUsers = User.getLoginUser().getMonitorsUsers();
                User[] monitorsUsersArr = monitorsUsers.toArray(new User[0]);
                boolean isMonitoring = false;
                for (int i = 0; i < monitorsUsersArr.length; i++) {
                    if (user.getId().equals(monitorsUsersArr[i].getId())) {
                        isMonitoring = true;
                        break;
                    }
                }
                if (isMonitoring) {
                    removeUserFromGroup();
                } else {
                    Toast.makeText(UserDetailActivity.this, "not monitoring this user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void removeUserFromGroup(){
        Group group = new Group();
        group.setId(groupId);
        new GetUserAsyncTask(REMOVE_MEMBER_OF_GROUP, user, null, group, null,
                new OnTaskComplete() {
                    @Override
                    public void onSuccess(Object result) {
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(UserDetailActivity.this,"error :"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }).execute();
    }

    private void getUserInfoAndUpdateUI(){
        user = new User();
        user.setId(userId);
        new GetUserAsyncTask(GET_USER_BY_ID, user, null, null, null,
                new OnTaskComplete() {
                    @Override
                    public void onSuccess(Object result) {
                        user = (User) result;
                        setUserInfo();
                        setUpRemoveFromGroupBut();
                        List<User> monitoredByList = user.getMonitoredByUsers();
                        monitoredByArr = monitoredByList.toArray(new User[0]);
                        if(monitoredByArr.length != 0){
                            getUserWithDetail();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(UserDetailActivity.this,"Error :" + e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }).execute();
    }

    private void setUserInfo(){
        TextView userIdTV = findViewById(R.id.userIdField);
        TextView userNameTV = findViewById(R.id.userNameField);
        TextView userCellPhoneTV = findViewById(R.id.cellPhoneField);
        TextView userHomePhoneTV = findViewById(R.id.homePhoneField);
        TextView teacherTV = findViewById(R.id.teacherField);
        TextView emergencyTV = findViewById(R.id.emergencyField);

        userIdTV.setText(user.getId());
        userNameTV.setText(user.getName());
        userCellPhoneTV.setText(user.getCellPhone());
        userHomePhoneTV.setText(user.getHomePhone());
        teacherTV.setText(user.getTeacherName());
        emergencyTV.setText(user.getEmergencyContactInfo());
    }

    private void populateMonitoredByList(String[] myListMonitoredByStrArr){

        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserDetailActivity.this, R.layout.user_entry,myListMonitoredByStrArr);
        // configure the list view
        ListView listView = findViewById(R.id.userDetailListView);
        listView.setAdapter(adapter);

        //registerClickCallback
        registerClickCallback();
    }

    private void stringsPrep(){
        String[] arrStr;
        if(monitoredByArr == null || monitoredByArr.length == 0){
            arrStr = new String[0];
        } else{
            arrStr = new String[monitoredByArr.length];
        }
        for(int i = 0; i < monitoredByArr.length; i++){
            arrStr[i] = "ID: "+ monitoredByArr[i].getId() +" "+"Name: "+ monitoredByArr[i].getName();
        }

        populateMonitoredByList(arrStr);
    }

    private void getUserWithDetail() {
        // Set up recursion
        populateListReady = false;
        loopCount = 0;
        getUserWithDetailLoop();
    }

    private void getUserWithDetailLoop() {
        if (loopCount >= monitoredByArr.length-1) {
            populateListReady = true;
        }
        new GetUserAsyncTask(GET_USER_BY_ID, monitoredByArr[loopCount], null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                monitoredByArr[loopCount]=(User)result;

                if (populateListReady) {
                    stringsPrep();
                } else {
                    loopCount++;
                    getUserWithDetailLoop();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(UserDetailActivity.this,"Error :" + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

    private void registerClickCallback(){
        ListView listView = findViewById(R.id.userDetailListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = UserDetailActivity.makeIntent(UserDetailActivity.this, monitoredByArr[i].getId(),groupId);
                startActivity(intent);
            }
        });
    }

    private void setUpRefresh(){
        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.swiperefreshUserDetail);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getUserInfoAndUpdateUI();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

    }

    public static Intent makeIntent(Context context,String userId,String groupId) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(USER_ID,userId);
        intent.putExtra(GROUP_ID,groupId);
        return intent;
    }
}
