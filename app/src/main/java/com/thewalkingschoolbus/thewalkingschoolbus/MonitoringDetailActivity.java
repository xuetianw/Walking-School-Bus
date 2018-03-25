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

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.Group;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import java.util.ArrayList;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

public class MonitoringDetailActivity extends AppCompatActivity {
    TextView displayName, displayEmail, displayPhonenumber;
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
                displayName = (TextView)findViewById(R.id.textView7);
                displayEmail = (TextView)findViewById(R.id.textView24);
                displayPhonenumber = (TextView)findViewById(R.id.textView21) ;
                displayName.setText(""+ monitoredUser.getName());
                monitoredUser.getName();
                displayEmail.setText(""+ monitoredUser.getEmail());
                if(monitoredUser.getCellPhone() != null){
                    displayPhonenumber.setText(""+ monitoredUser.getCellPhone());
                } else {
                    displayPhonenumber.setText("");
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
