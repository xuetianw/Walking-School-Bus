package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class MonitoringDetailActivity extends AppCompatActivity {
    private TextView profileNametv, profileEmailtv,
            birthYeartv, birthMonthtv, profileAddresstv,
            profileCellphonetv, homePhonetv, gradetv,
            teacherNametv, emergencyContactInfotv;

    public static String userEmail;
    static User monitoredUser = new User();
    ArrayList <Group> groupArrayList =  new ArrayList<>();
    public static ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_detail);

        monitoredUser.setEmail(userEmail);
        updateUI();
        setupStopMonitoringBtn();
        registerClickCallBack();
        setupEditChildBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        monitoredUser.setEmail(userEmail);
        updateUI();
    }

    private void setupEditChildBtn() {
        Button button = (Button)findViewById(R.id.editChildbtnid);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = EditChildDetailActivity.makeIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void updateUI() {
        new GetUserAsyncTask(GET_USER_BY_EMAIL, monitoredUser,null, null, null,new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                monitoredUser = (User) result;


                profileNametv = (TextView)findViewById(R.id.profileNameid);
                profileEmailtv = (TextView)findViewById(R.id.profileEmailid);
                birthYeartv = (TextView)findViewById(R.id.profileBrithdayYearid);
                birthMonthtv = (TextView)findViewById(R.id.profileBirthdayMonthid);
                profileAddresstv = (TextView)findViewById(R.id.profileaddressid);
                profileCellphonetv = (TextView)findViewById(R.id.profileCellphoneNumberid);
                homePhonetv = (TextView)findViewById(R.id.profilehomPhonNumberid);
                gradetv = (TextView)findViewById(R.id.profileGradeid);
                teacherNametv = (TextView)findViewById(R.id.profileteachernameid);
                emergencyContactInfotv = (TextView)findViewById(R.id.profileemergenceyid);

                if(monitoredUser.getName() !=  null){
                    profileNametv.setText("" +  monitoredUser.getName());
                }
                if(monitoredUser.getEmail() != null){
                    profileEmailtv.setText("" +  monitoredUser.getEmail());
                }
                if(monitoredUser.getBirthMonth() !=  null){
                    birthMonthtv.setText("" +  monitoredUser.getBirthMonth());
                }
                if(monitoredUser.getBirthYear() !=  null){
                    birthYeartv.setText("" +  monitoredUser.getBirthYear());
                }

                if(monitoredUser.getAddress() !=  null){
                    profileAddresstv.setText("" +  monitoredUser.getAddress());
                }

                if(monitoredUser.getCellPhone() != null){
                    profileCellphonetv.setText("" + monitoredUser.getCellPhone());
                }
                if(monitoredUser.getHomePhone() != null){
                    homePhonetv.setText("" + monitoredUser.getHomePhone());
                }
                if(monitoredUser.getGrade() != null){
                    gradetv.setText("" + monitoredUser.getGrade());
                }
                if(monitoredUser.getTeacherName() != null){
                    teacherNametv.setText("" + monitoredUser.getTeacherName());
                }
                if(monitoredUser.getEmergencyContactInfo() != null){
                    emergencyContactInfotv.setText("" + monitoredUser.getEmergencyContactInfo());
                }
                groupArrayList = (ArrayList<Group>) monitoredUser.getMemberOfGroups();
                arrayList = new ArrayList();
                for(Group group: groupArrayList) {
                    arrayList.add("id: " + group.getId() + " "
                            + "Group Name: " + group.getGroupDescription());
                }
                populateListView();
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to retrieve user.", Toast.LENGTH_SHORT)
                        .show();
                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void populateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.group_entry, arrayList);
        ListView list = (ListView) findViewById(R.id.listview5);
        list.setAdapter(adapter);
    }


    private void setupStopMonitoringBtn() {
        Button btn = (Button) findViewById(R.id.stopMonitID);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MonitoringDetailActivity.class);
    }

    private void registerClickCallBack() {
        ListView list = (ListView) findViewById(R.id.listview5);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paret, View viewClicked, int position, long id) {
                Intent intent = GroupDetailActivity.makeIntent(getApplicationContext()
                        , groupArrayList.get(position), monitoredUser);
                startActivity(intent);
            }
        });
    }
}
