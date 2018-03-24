package com.thewalkingschoolbus.thewalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.List;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.GET_USER_BY_ID;

public class UserDetailActivity extends AppCompatActivity {

    private static final String USER_ID = "USER_ID";
    private static final String GROUP_ID = "GROUP_ID";
    private String userId;
    private String groupId;
    private User user;
    private List<User> monitoredByArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        extractData();
        setUpRemoveFromGroupBut();
        getUserInfoAndUpdateUI();


    }
    private void extractData(){
        Intent intent = getIntent();
        userId = intent.getStringExtra(USER_ID);
        groupId = intent.getStringExtra(GROUP_ID);
    }

    private void setUpRemoveFromGroupBut(){

    }

    private void getUserInfoAndUpdateUI(){
        user = new User();
        user.setId(userId);
        new GetUserAsyncTask(GET_USER_BY_ID, user, null, null, null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                user = (User) result;
                setUserInfo();
                populateMonitoredByList();
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

    private void populateMonitoredByList(){

        String[] myListMonitoredByStrArr = stringsPrep();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserDetailActivity.this, R.layout.user_entry,myListMonitoredByStrArr);
        // configure the list view
        ListView listView = findViewById(R.id.monitoredByListView);
        listView.setAdapter(adapter);

        //registerClickCallback
        registerClickCallback();
    }

    private String[] stringsPrep(){
        String[] arr;
        int size;
        monitoredByArr = user.getMonitoredByUsers();

        if(monitoredByArr == null || monitoredByArr.size() <= 0){
            arr = new String[0];
            size = 0;
        }else{
            arr = new String[monitoredByArr.size()];
            size = monitoredByArr.size();
        }

        for(int i = 0; i < size; i++){
            arr[i] = "Name: "+monitoredByArr.get(i).getName() +" "+"Email: "+monitoredByArr.get(i).getEmail();
        }

     return arr;
    }

    private void registerClickCallback(){
        ListView listView = findViewById(R.id.monitoredByListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = UserDetailActivity.makeIntent(UserDetailActivity.this,monitoredByArr.get(i).getId(),groupId);
                startActivity(intent);
            }
        });
    }



    public static Intent makeIntent(Context context,String userId,String groupId) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(USER_ID,userId);
        intent.putExtra(GROUP_ID,groupId);
        return intent;
    }
}
