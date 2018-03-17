package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thewalkingschoolbus.thewalkingschoolbus.Models.MapFragmentState;

import static com.thewalkingschoolbus.thewalkingschoolbus.MainActivity.*;


public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String USER_LOGSTATUS = "USER_LOGSTATUS";
    private Toolbar toolbar;
    private NavigationView navigationView;


    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        boolean feedback = getloginStatus();
        if (!feedback){
            startActivity(MainActivity.makeIntent(getApplicationContext()));
            finish();
        } else {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            // OPEN DEFAULT FRAGMENT //
            openDefaultFragment();
        }


        // SET UP TEST //
        //setupTest();
    }

    private boolean getloginStatus() {
        SharedPreferences preferences = getApplication().getSharedPreferences(AppStates, MODE_PRIVATE);
        return preferences.getBoolean(USER_LOGSTATUS, false);
    }

    private void openDefaultFragment() {
        navigationView.setCheckedItem(R.id.nav_fragment_monitoring);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new MonitoringFragment())
                .commit();
        // Brief delay to prevent new title from being overwritten by default title.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle("Monitoring");
            }
        }, 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.action_logout) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_fragment_monitoring) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new MonitoringFragment())
                    .commit();
            toolbar.setTitle("Monitoring");
        } else if (id == R.id.nav_fragment_monitored_by) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new MonitoredByFragment())
                    .commit();
            toolbar.setTitle("Monitored By");
        } else if (id == R.id.nav_fragment_group) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new GroupFragment())
                    .commit();
            toolbar.setTitle("Groups");
        } else if (id == R.id.nav_fragment_map_create_group) {
            openMapFragment(MapFragmentState.CREATE_GROUP);
        } else if (id == R.id.nav_fragment_map_join_group) {
            openMapFragment(MapFragmentState.JOIN_GROUP);
        } else if (id == R.id.nav_lougout) {
            storeLogoutInfoToSharePreferences();
            Intent intent = MainActivity.makeIntent(getApplicationContext());
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openMapFragment(MapFragmentState state) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        args.putInt("state", state.ordinal());
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(args);

        //        MapFragmentState firstState = MapFragmentState.values()[0];
        //        int sameState = firstState.ordinal();

        fragmentTransaction.replace(R.id.content_frame, mapFragment);
        fragmentTransaction.commit();

        switch (state) {
            case JOIN_GROUP:
                toolbar.setTitle("Join Group");
                break;
            case CREATE_GROUP:
                toolbar.setTitle("Create Group");
                break;
            default:
                toolbar.setTitle("Map (Debug Mode)");
                break;
        }
    }

    private void storeLogoutInfoToSharePreferences() {
        SharedPreferences preferences = getSharedPreferences(AppStates, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(USER_LOGSTATUS, false);

        editor.commit();
    }

//
//    // TEST - MOCK DATABASE - DELETE AFTER DATABASE MANAGER IS WRITTEN
//    public static List<User> registeredUsers;
//    public static List<User> monitoringUsers;
//    public static List<User> monitoredByUsers;
//    public static List<Group> existingGroups;
//    private void setupTest() {
//        registeredUsers = new ArrayList<>();
//        registeredUsers.add(new User("0", "John", "john@email.com"));
//        registeredUsers.add(new User("1", "Jane", "jane@email.com"));
//
//        monitoringUsers = new ArrayList<>();
//        monitoringUsers.add(new User("2", "Josh", "josh@email.com"));
//        monitoringUsers.add(new User("3", "Fred", "fred@email.com"));
//
//        monitoredByUsers = new ArrayList<>();
//        monitoredByUsers.add(new User("4", "Jacky", "jacky@email.com"));
//        monitoredByUsers.add(new User("5", "Benny", "benny@email.com"));
//
//        existingGroups = new ArrayList<>();
//        Group group1 = new Group();
//        group1.setId("SFU to JOYCE STATION");
//        group1.setRouteLatArray(new double[]{49.2781,49.2384,0});
//        group1.setRouteLngArray(new double[]{-122.9199,-123.0318,0});
//
//        Group group2 = new Group();
//        group2.setId("UBC to GASTON PARK");
//        group2.setRouteLatArray(new double[]{49.2606,49.2359,0});
//        group2.setRouteLngArray(new double[]{-123.2459,-123.0309,0});
//
//        Group group3 = new Group();
//        group3.setId("JOYCE STATION to CENTRAL PARK");
//        group3.setRouteLatArray(new double[]{49.2384,49.2276,0});
//        group3.setRouteLngArray(new double[]{-123.0318,-123.0179,0});
//
//        existingGroups.add(group1);
//        existingGroups.add(group2);
//        existingGroups.add(group3);
//    }
}
