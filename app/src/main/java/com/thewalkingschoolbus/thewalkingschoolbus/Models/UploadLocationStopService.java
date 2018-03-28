package com.thewalkingschoolbus.thewalkingschoolbus.Models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.thewalkingschoolbus.thewalkingschoolbus.WalkingFragment;

public class UploadLocationStopService extends Service {

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
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent _) {
                WalkingFragment.uploadCurrentCoordinate(null);
                context.unregisterReceiver(this); // this == BroadcastReceiver, not Activity
                Log.d("@@@@@@@@@@@@@@@@", "onReceive: STOP");
            }
        };

        this.registerReceiver(receiver, new IntentFilter("com.blah.blah.somemessage"));

        PendingIntent pintent = PendingIntent.getBroadcast(this, 0, new Intent("com.blah.blah.somemessage"), 0);
        AlarmManager manager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));

        // set alarm to fire 30 sec (1000*30) from now (SystemClock.elapsedRealtime())
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000 , pintent); // 10 minutes
    }
}
