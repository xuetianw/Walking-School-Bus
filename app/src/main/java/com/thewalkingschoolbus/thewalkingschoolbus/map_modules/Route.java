package com.thewalkingschoolbus.thewalkingschoolbus.map_modules;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String destinationAddress;
    public LatLng destinationLocation;
    public String originAddress;
    public LatLng originLocation;

    public List<LatLng> points;
}