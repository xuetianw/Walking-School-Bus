package com.thewalkingschoolbus.thewalkingschoolbus.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.R;
import com.thewalkingschoolbus.thewalkingschoolbus.activities.UserProfileActivity;
import com.thewalkingschoolbus.thewalkingschoolbus.interfaces.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.models.User;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Avatar;
import com.thewalkingschoolbus.thewalkingschoolbus.models.collections.Title;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.*;

/*
 * ProfileFragment.java
 * Populate the profile UI fragment.
 * Display all user profile information, as well as customized avatar and title.
 * User may edit profile information from the edit profile button.
 *
 * SOURCES - Based on following tutorials:
 * Modern Profile UI Design in Android Studio
 * https://www.youtube.com/watch?v=2pirZvqXza0
 */
public class ProfileFragment extends android.app.Fragment {

    private static final String TAG = "ProfileFragment";
    private View view;

    private ImageView profileImage;
    private TextView profileNametv, profileEmailtv,
            birthYeartv, birthMonthtv, profileAddresstv,
            profileCellphonetv, homePhonetv, gradetv,
            teacherNametv, emergencyContactInfotv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        setupTextViews();
        setUpEditProfileBtn();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTextViews();
    }

    private void setupTextViews() {
        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        profileNametv = (TextView)view.findViewById(R.id.profileNameid);
        profileEmailtv = (TextView)view.findViewById(R.id.profileEmailid);
        birthYeartv = (TextView)view.findViewById(R.id.profileBrithdayYearid);
        birthMonthtv = (TextView)view.findViewById(R.id.profileBirthdayMonthid);
        profileAddresstv = (TextView)view.findViewById(R.id.profileaddressid);
        profileCellphonetv = (TextView)view.findViewById(R.id.profileCellphoneNumberid);
        homePhonetv = (TextView)view.findViewById(R.id.profilehomPhonNumberid);
        gradetv = (TextView)view.findViewById(R.id.profileGradeid);
        teacherNametv = (TextView)view.findViewById(R.id.profileteachernameid);
        emergencyContactInfotv = (TextView)view.findViewById(R.id.profileemergenceyid);

        new GetUserAsyncTask(GET_USER_BY_EMAIL, User.getLoginUser(),null, null, null,new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                if(result == null){
                } else {
                    // set up textView display message
                    User.setLoginUser((User)result);
                    if(User.getLoginUser().getName() !=  null){
                        profileNametv.setText("" +  User.getLoginUser().getName());
                    }
                    if(User.getLoginUser().getEmail() != null){
                        profileEmailtv.setText("" +  User.getLoginUser().getEmail());
                    }
                    if(User.getLoginUser().getBirthMonth() !=  null){
                        birthMonthtv.setText("" +  User.getLoginUser().getBirthMonth());
                    }
                    if(User.getLoginUser().getBirthYear() !=  null){
                        birthYeartv.setText("" +  User.getLoginUser().getBirthYear());
                    }
                    if(User.getLoginUser().getAddress() !=  null){
                        profileAddresstv.setText("" +  User.getLoginUser().getAddress());
                    }
                    if(User.getLoginUser().getCellPhone() != null){
                        profileCellphonetv.setText("" + User.getLoginUser().getCellPhone());
                    }
                    if(User.getLoginUser().getHomePhone() != null){
                        homePhonetv.setText("" + User.getLoginUser().getHomePhone());
                    }
                    if(User.getLoginUser().getGrade() != null){
                        gradetv.setText("" + User.getLoginUser().getGrade());
                    }
                    if(User.getLoginUser().getTeacherName() != null){
                        teacherNametv.setText("" + User.getLoginUser().getTeacherName());
                    }
                    if(User.getLoginUser().getEmergencyContactInfo() != null){
                        emergencyContactInfotv.setText("" + User.getLoginUser().getEmergencyContactInfo());
                    }
                    if (User.getLoginUser().getCustomization() != null) {
                        if (User.getLoginUser().getCustomization().getAvatarEquipped() != -1) {
                            profileImage.setImageResource(CollectionFragment.getImageId(getActivity(), Avatar.avatars[User.getLoginUser().getCustomization().getAvatarEquipped()].getName()));
                        }
                        if (User.getLoginUser().getCustomization().getTitleEquipped() != -1) {
                            profileNametv.setText(profileNametv.getText() + " - " + Title.titles[User.getLoginUser().getCustomization().getTitleEquipped()].getTitle());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    private void setUpEditProfileBtn(){
        Button editBtn = view.findViewById(R.id.editinfobtnid);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = UserProfileActivity.makeIntent(getActivity());
                startActivity(intent);
            }
        });
    }
}
