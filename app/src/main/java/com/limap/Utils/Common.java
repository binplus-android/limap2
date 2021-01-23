package com.limap.Utils;

import android.location.Location;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 12,January,2021
 */
class Common {

    public static float getKmFromLatLong(float lat1, float lng1, float lat2, float lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters/1000;
    }
}
