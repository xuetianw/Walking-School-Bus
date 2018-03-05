package com.thewalkingschoolbus.thewalkingschoolbus;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thewalkingschoolbus.thewalkingschoolbus.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView navigationView;

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK); // Set title text color to black.
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
        toggle.getDrawerArrowDrawable().setColor(Color.BLACK); // Set hamburger color to black.
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // OPEN DEFAULT FRAGMENT //
        openDefaultFragment();

        // SET UP TEST //
        setupTest();
    }

    private void openDefaultFragment() {
        navigationView.setCheckedItem(R.id.nav_fragment_profile);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new ProfileFragment())
                .commit();
        // Brief delay to prevent new title from being over written by default title.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle("Profile");
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

    // Code used to generate overflow button.
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

        if (id == R.id.nav_fragment_profile) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new ProfileFragment())
                    .commit();
            toolbar.setTitle("Profile");
        } else if (id == R.id.nav_fragment_group) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new GroupFragment())
                    .commit();
            toolbar.setTitle("Group");
        } else if (id == R.id.nav_fragment_map) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new MapFragment())
                    .commit();
            toolbar.setTitle("Map");
        } else if (id == R.id.nav_fragment_monitoring) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new MonitoringFragment())
                    .commit();
            toolbar.setTitle("Monitoring");
        } else if (id == R.id.nav_fragment_monitored_by) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new MonitoredByFragment())
                    .commit();
            toolbar.setTitle("Monitored By");
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // TEST - MOCK DATABASE - DELETE AFTER DATABASE MANAGER IS WRITTEN
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
}
