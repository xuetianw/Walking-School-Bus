package com.thewalkingschoolbus.thewalkingschoolbus.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.thewalkingschoolbus.thewalkingschoolbus.WalkingFragment;

public class UploadLocationService extends Service
{
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        SetAlarm();
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        SetAlarm();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void SetAlarm()
    {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive( Context context, Intent _ )
            {
                WalkingFragment.uploadCurrentCoordinate();
                context.unregisterReceiver( this ); // this == BroadcastReceiver, not Activity
                SetAlarm();
            }
        };

        this.registerReceiver( receiver, new IntentFilter("com.blah.blah.somemessage") );

        PendingIntent pintent = PendingIntent.getBroadcast( this, 0, new Intent("com.blah.blah.somemessage"), 0 );
        AlarmManager manager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));

        // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
        manager.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*5, pintent );
    }
}

