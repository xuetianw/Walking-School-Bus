package com.thewalkingschoolbus.thewalkingschoolbus.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;

import com.thewalkingschoolbus.thewalkingschoolbus.WalkingFragment;

public class UploadLocationService extends Service {

    private Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setAlarm();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setAlarm() {
        updateLocation();
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WalkingFragment.uploadCurrentCoordinate(currentLocation);
                context.unregisterReceiver(this); // this == BroadcastReceiver, not Activity
                setAlarm();
            }
        };

        this.registerReceiver(receiver, new IntentFilter("com.blah.blah.somemessage"));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.blah.blah.somemessage"), 0);
        AlarmManager manager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));

        // set alarm to fire 10 sec (1000*10) from now (SystemClock.elapsedRealtime())
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 10, pendingIntent);
    }

    private void updateLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        currentLocation = location;
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
    }
}

