package com.thewalkingschoolbus.thewalkingschoolbus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;

import static com.thewalkingschoolbus.thewalkingschoolbus.activities.LoginActivity.AppStates;
import static com.thewalkingschoolbus.thewalkingschoolbus.activities.LoginActivity.REGISTER_EMAIL;
import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.EDIT_USER;

/*
 * UserDetailActivity.java
 * Display the user profile edit here.
 * Allow users to edit profile.
 */
public class UserProfileActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_user_profile);
        setupTextviews();
        setupSaveButton();
        setupCancelButton();
    }

    private void setupCancelButton() {
        Button cancelButton = (Button)findViewById(R.id.button5);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupSaveButton() {
        Button saveButton = (Button)findViewById(R.id.button4);
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
                } else if(!birthMonth.isEmpty() //setup birthMonth restriction
                        && ((Integer.parseInt(birthMonth) > 12 || Integer.parseInt(birthMonth) < 0))){
                    Toast.makeText(getApplicationContext(), RegisterActivity.PLEASE_CORRECT_YOUR_DATE_OF_BIRTH, Toast.LENGTH_SHORT)
                            .show();
                } else if(!birthYear.isEmpty() // //setup birthYear restriction
                        && (Integer.parseInt(birthYear) > 2018 || Integer.parseInt(birthYear) < 1900)) {
                    Toast.makeText(getApplicationContext(), RegisterActivity.PLEASE_CORRECT_YOUR_DATE_OF_BIRTH, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    final User user = new User();
                    user.setId(User.getLoginUser().getId());
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
                    user.setCustomization(User.getLoginUser().getCustomization());

                    new GetUserAsyncTask(EDIT_USER, user,null, null,null ,
                            new OnTaskComplete() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(getApplicationContext(), EDIT_SUCCESSFULLY_MESSAGE, Toast.LENGTH_SHORT)
                                    .show();
                            //set up login user
                            User.setLoginUser((User)user );
                            // store user editing information so that user
                            // can still automatically login after successfully editing
                            storeUserInfoToSharePreferences();
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

    private void storeUserInfoToSharePreferences() {
        SharedPreferences preferences = getSharedPreferences(AppStates, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(REGISTER_EMAIL, User.getLoginUser().getEmail());
        editor.commit();
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

        /* get the user information and pass to the EditTextView
         so that it is easier for user's parents can see the
          orginal information before editing
         */
        nameET.setText(User.getLoginUser().getName());
        emailET.setText(User.getLoginUser().getEmail());
        birthYearET.setText(User.getLoginUser().getBirthYear());
        birthMonthET.setText(User.getLoginUser().getBirthMonth());
        addressET.setText(User.getLoginUser().getAddress());
        cellPhoneET.setText(User.getLoginUser().getCellPhone());
        homePhoneET.setText(User.getLoginUser().getHomePhone());
        gradeET.setText(User.getLoginUser().getGrade());
        teacherNameDT.setText(User.getLoginUser().getTeacherName());;
        emergencyContactInfoET.setText(User.getLoginUser().getEmergencyContactInfo());;
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, UserProfileActivity.class);
    }
}
