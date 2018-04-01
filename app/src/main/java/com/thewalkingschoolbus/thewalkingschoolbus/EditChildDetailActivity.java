package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.RegisterActivity.PLEASE_CORRECT_YOUR_DATE_OF_BIRTH;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.EDIT_USER;

public class EditChildDetailActivity extends AppCompatActivity {


    public static final String EDIT_SUCCESSFULLY_MESSAGE = "edit successfully";
    private EditText nameET, emailET,
            birthYearET, birthMonthET, addressET,
            cellPhoneET, homePhoneET, gradeET,
            teacherNameDT, emergencyContactInfoET;
    private String loginName, registerEmail, registerPassword,
            birthYear, birthMonth, address, cellPhone, homePhone,
            grade, teacherName, emergencyContactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child_detail);
        setupTextviews();

        setupSavebutton();
        setupCancelButton();
    }

    private void setupCancelButton() {
        Button saveButton = (Button)findViewById(R.id.childinfocancelbtnid);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, EditChildDetailActivity.class);
        return intent;
    }


    private void setupSavebutton() {
        Button saveButton = (Button)findViewById(R.id.childinfosaveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginName = nameET.getText().toString();
                registerEmail = emailET.getText().toString();
                birthYear = birthYearET.getText().toString();
                birthMonth = birthMonthET.getText().toString();
                address = addressET.getText().toString();
                cellPhone = cellPhoneET.getText().toString();
                homePhone = homePhoneET.getText().toString();
                grade = gradeET.getText().toString();
                teacherName = teacherNameDT.getText().toString();
                emergencyContactInfo = emergencyContactInfoET.getText().toString();

                if(loginName.isEmpty() || registerEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(), LoginActivity.USERNAME_EMAIL_AND_PASSWORD_REQUIRED_EMPTY_MESSAGE, Toast.LENGTH_SHORT)
                            .show();
                } else if(!birthMonth.isEmpty()
                        && ((Integer.parseInt(birthMonth) > 12 || Integer.parseInt(birthMonth) < 0))){
                    Toast.makeText(getApplicationContext(), PLEASE_CORRECT_YOUR_DATE_OF_BIRTH, Toast.LENGTH_SHORT)
                            .show();
                } else if(!birthYear.isEmpty()
                        && (Integer.parseInt(birthYear) > 2018 || Integer.parseInt(birthYear) < 1900)) {
                    Toast.makeText(getApplicationContext(), PLEASE_CORRECT_YOUR_DATE_OF_BIRTH, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    final User user = new User();
                    user.setId(MonitoringDetailActivity.monitoredUser.getId());
                    user.setEmail(registerEmail);
                    user.setName(loginName);
                    user.setPassword(registerPassword);
                    user.setBirthYear(birthYear);
                    user.setBirthMonth(birthMonth);
                    user.setAddress(address);
                    user.setCellPhone(cellPhone);
                    user.setHomePhone(homePhone);
                    user.setGrade(grade);
                    user.setTeacherName(teacherName);
                    user.setEmergencyContactInfo(emergencyContactInfo);

                    new GetUserAsyncTask(EDIT_USER, user,null, null,null ,new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(getApplicationContext(), EDIT_SUCCESSFULLY_MESSAGE, Toast.LENGTH_SHORT)
                                    .show();
                            System.out.println(result);
                            if(result.toString().equals("SUCCESSFUL")){
                                MonitoringDetailActivity.userEmail = registerEmail;
                            }
                            finish();
                        }
                        @Override
                        public void onFailure(Exception e) {
                            /*
                            Toast.makeText(getApplicationContext(),"edit failed", Toast.LENGTH_SHORT)
                                    .show();*/
                            Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).execute();
                }
            }
        });
    }



    private void setupTextviews() {
        nameET = (EditText)findViewById(R.id.enter_fullnameid);
        emailET = (EditText)findViewById(R.id.enter_emailid);
        birthYearET = (EditText) findViewById(R.id.birthdayYearid);
        birthMonthET = (EditText) findViewById(R.id.enter_birth_month);
        addressET = (EditText) findViewById(R.id.enter_address);
        cellPhoneET = (EditText) findViewById(R.id.enter_phoneid);
        homePhoneET = (EditText) findViewById(R.id.enter_homephone_number);
        gradeET = (EditText) findViewById(R.id.enter_current_grade);
        teacherNameDT = (EditText) findViewById(R.id.enter_teacher_name);
        emergencyContactInfoET = (EditText) findViewById(R.id.emergency_contact_info);

        nameET.setText(MonitoringDetailActivity.monitoredUser.getName());
        emailET.setText(MonitoringDetailActivity.monitoredUser.getEmail());
        birthYearET.setText(MonitoringDetailActivity.monitoredUser.getBirthYear());
        birthMonthET.setText(MonitoringDetailActivity.monitoredUser.getBirthMonth());
        addressET.setText(MonitoringDetailActivity.monitoredUser.getAddress());
        cellPhoneET.setText(MonitoringDetailActivity.monitoredUser.getCellPhone());
        homePhoneET.setText(MonitoringDetailActivity.monitoredUser.getHomePhone());
        gradeET.setText(MonitoringDetailActivity.monitoredUser.getGrade());
        teacherNameDT.setText(MonitoringDetailActivity.monitoredUser.getTeacherName());;
        emergencyContactInfoET.setText(MonitoringDetailActivity.monitoredUser.getEmergencyContactInfo());;





    }
}
