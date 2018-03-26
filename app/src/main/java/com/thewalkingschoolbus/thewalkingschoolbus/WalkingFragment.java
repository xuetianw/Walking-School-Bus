package com.thewalkingschoolbus.thewalkingschoolbus;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thewalkingschoolbus.thewalkingschoolbus.Interface.OnTaskComplete;
import com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask;
import com.thewalkingschoolbus.thewalkingschoolbus.map_modules.MapUtil;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.GpsLocation;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.UploadLocationService;
import com.thewalkingschoolbus.thewalkingschoolbus.Models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.thewalkingschoolbus.thewalkingschoolbus.api_binding.GetUserAsyncTask.functionType.POST_GPS_LOCATION;

public class WalkingFragment extends android.app.Fragment {

    private static final String TAG = "WalkingFragment";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        view = inflater.inflate(R.layout.fragment_walking, container, false);
        setupBtn();
        MapUtil.getLocationPermission();
        return view;
    }

    private void setupBtn() {
        Button startBtn = view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWalk();
            }
        });
        Button arrivedBtn = view.findViewById(R.id.arrivedBtn);
        arrivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrivedWalk();
            }
        });
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelWalk();
            }
        });
        Button panicBtn = view.findViewById(R.id.panicBtn);
        panicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panicWalk();
            }
        });
    }

    private void startWalk() {
        Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();
        updateStatusText("Now walking...");
        startUploadLocationService();
    }

    private void arrivedWalk() {
        Toast.makeText(getActivity(), "Arrived", Toast.LENGTH_SHORT).show();
        updateStatusText("Arrived");
        cancelUploadLocationService();
    }

    private void cancelWalk() {
        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
        updateStatusText("Walk canceled");
        cancelUploadLocationService();
    }

    private void panicWalk() {
        Toast.makeText(getActivity(), "You should call 911", Toast.LENGTH_SHORT).show();
        updateStatusText("PANIC PANIC PANIC PANIC PANIC");
    }

    private void startUploadLocationService() {
        Intent intent = new Intent(getActivity(), UploadLocationService.class);
        getActivity().stopService(intent);
        getActivity().startService(intent);
    }

    private void cancelUploadLocationService() {
        Intent intent = new Intent(getActivity(), UploadLocationService.class);
        getActivity().stopService(intent);
    }

    private void updateStatusText(String statusText) {
        TextView text = view.findViewById(R.id.walkStatusTxt);
        text.setText(statusText);
    }

    // DATE

    public static Date stringToDate(String dateInString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        try {
            return format.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToStringSimple(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'LAST UPDATED - DATE: 'yyyy-MM-dd', TIME:'HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        return dateFormat.format(date);
    }

    // STATIC

    public static void uploadCurrentCoordinate() {
        Location currentLocation = MapUtil.getDeviceLocation();

        if (currentLocation == null) {
            Log.d(TAG, "#### current location == null! (note: first upload is always null)");
            return;
        }

        Double lat = currentLocation.getLatitude();
        Double lng = currentLocation.getLongitude();
        String timestamp = dateToString(new Date());

        Log.d(TAG, "#### LAT / LNG: " + lat + " / " + lng);
        Log.d(TAG, "#### TIMESTAMP: " + dateToString(new Date()));

        User user = User.getLoginUser();
        user.setLastGpsLocation(new GpsLocation(lat.toString(), lng.toString(), timestamp));

        new GetUserAsyncTask(POST_GPS_LOCATION, user, null, null,null, new OnTaskComplete() {
            @Override
            public void onSuccess(Object result) {
                //User user = (User) result;
                Log.d(TAG, "#### Successfully updated current location. " + result);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "#### Error: "+e.getMessage());
            }
        }).execute();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, WalkingFragment.class);
    }
}
