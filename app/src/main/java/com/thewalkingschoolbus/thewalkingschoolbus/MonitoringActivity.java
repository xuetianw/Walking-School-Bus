package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.thewalkingschoolbus.thewalkingschoolbus.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * MonitoringActivity
 * Description here.
 */
public class MonitoringActivity extends AppCompatActivity {

    private static final String TAG = "MonitoringActivity";

    // TEST - MOCK DATABASE
    public static List<User> registeredUsers;
    public static List<User> monitoringUsers;
    public static List<User> monitoredByUsers;
    private void setupTest() {
        registeredUsers = new ArrayList<>();
        registeredUsers.add(new User(0, "John", "john@email.com"));
        registeredUsers.add(new User(1, "Jane", "jane@email.com"));

        monitoringUsers = new ArrayList<>();
        monitoringUsers.add(new User(2, "Josh", "josh@email.com"));
        monitoringUsers.add(new User(3, "Fred", "fred@email.com"));

        monitoredByUsers = new ArrayList<>();
        monitoredByUsers.add(new User(4, "Jacky", "jacky@email.com"));
        monitoredByUsers.add(new User(5, "Benny", "benny@email.com"));
    }
    // TEST

    /**
     * SectionsPageAdapter
     * Used to build tabs.
     * From "Android Tab Tutorial" by "CodingWithMitch"
     */
    private class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        Log.d(TAG, "Starting.");

        setupTest();
        setupLogoutBtn();

        // Set up the viewPager with the sections adapter.
        ViewPager viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tablayout = findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Monitoring1Fragment(), "MONITORING");
        adapter.addFragment(new Monitoring2Fragment(), "MONITORED BY");
        viewPager.setAdapter(adapter);
    }

    private void setupLogoutBtn() {
        Button logoutButton = (Button) findViewById(R.id.logoutid);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanSharedPreferences();
                finish();
            }
        });
    }

    private void cleanSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(MainActivity.AppStates, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(MainActivity.REGISTER_EMAIL, null );
        editor.putString(MainActivity.LOGIN_NAME, null );
        editor.putString(MainActivity.LOGIN_PASSWORD, null );
        editor.commit();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MonitoringActivity.class);
    }
}
