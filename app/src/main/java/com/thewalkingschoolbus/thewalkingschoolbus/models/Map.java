//package com.thewalkingschoolbus.thewalkingschoolbus.models;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.thewalkingschoolbus.thewalkingschoolbus.MainMenuActivity;
//import com.thewalkingschoolbus.thewalkingschoolbus.R;
//
//public class Map {
//
//    private GoogleMap map;
//
//    private Context context;
//    private GoogleMap map;
//
//
//
//    public static void getLocationPermission() {
//    /*
//     * Request location permission, so that we can get the location of the device.
//     * The result of the permission request is handled by a callback, onRequestPermissionsResult.
//     */
//        Log.d(TAG, "getLocationPermission: getting location permissions");
//
//        if (ContextCompat.checkSelfPermission(MainMenuActivity.getContextOfApplication(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            locationPermissionGranted = true;
//            //initMap();
//        } else {
//            ActivityCompat.requestPermissions((Activity) MainMenuActivity.getContextOfApplication(),
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult: called.");
//
//        locationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    locationPermissionGranted = true;
//                }
//                initMap();
//            }
//        }
//    }
//
//    private void initMap() {
//        Log.d(TAG, "initMap: initializing map");
//
//        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
//                .findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                Log.d(TAG, "onMapReady: map is ready");
//
//                map = googleMap;
//
//                if (locationPermissionGranted) {
//                    getDeviceLocation();
//                    if (ActivityCompat.checkSelfPermission(context,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                            && ActivityCompat.checkSelfPermission(context,
//                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                }
//                map.setMyLocationEnabled(true);
//                map.getUiSettings().setMyLocationButtonEnabled(true);
//            }
//        });
//    }
//
//
//
//}
