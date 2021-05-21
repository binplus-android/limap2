package com.limap.Pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TEJ on 28-Apr-16.
 */
public class Pref {
    private static final String PREF_NAME  = "LIMAP";
    private static final String USER_ID    = "USER_ID";
    private static final String MOBILE_NO  = "MOBILE_NO";
    private static final String LANGUAGE   = "LANGUAGE";
    private static final String CODE       = "CODE";
    private static final String LATITUDE   = "LATITUDE";
    private static final String LONGITUDE  = "LONGITUDE";
    private static final String PINCODE    = "PINCODE";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static Pref instance;
    private static final String IsCALlPERMISSION = "iscallpermission";

    private Pref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
    }

    public static final Pref getInstance(Context context) {
        if (instance == null) {
            instance = new Pref(context);
        }
        return instance;
    }
    public void setUserId(String id) {
        editor.putString(USER_ID, id).commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }
    public void setMobileNo(String id) {
        editor.putString(MOBILE_NO, id).commit();
    }

    public String getMobileNo() {
        return sharedPreferences.getString(MOBILE_NO, "");
    }
    public void setLANGUAGE(String id) {
        editor.putString(LANGUAGE, id).commit();
    }

    public String getLANGUAGE() {
        return sharedPreferences.getString(LANGUAGE, "");
    }

    public void setCODE(String id) {
        editor.putString(CODE, id).commit();
    }

    public String getCODE() {
        return sharedPreferences.getString(CODE, "");
    }


    public  void setLocation(String lat , String longi ,String pin)
    {
        editor.putString(LATITUDE, lat);
        editor.putString(LONGITUDE, longi);
        editor.putString(PINCODE, pin);
                editor.commit();
    }

    public  String getLATITUDE() {
        return sharedPreferences.getString(LATITUDE, "");
    }

    public  String getLONGITUDE() {
        return sharedPreferences.getString(LONGITUDE, "");
    }

    public  String getPINCODE() {
        return sharedPreferences.getString(PINCODE, "");
    }
}
