package com.limap.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.limap.BaseController;
import com.limap.BuildConfig;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.SetterEditProfile;
import com.limap.Model.SetterLogin;
import com.limap.Model.SetterResponse;
import com.limap.Pref.Pref;
import com.limap.R;

import java.text.DateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileEditActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editTextprofileid,editTextname,editTextaddress,editTextcity,editTexttaluka,editTextdistrict,editTextstate,editTextlocation,editTextServingVillage,editTextspeciality,editTextexperience;
    private CheckBox isdoctor;
    private LinearLayout layoutdoctor;
    private Button btnSubmit;
    private TextView agreement;

    // location last updated time
    private String mLastUpdateTime;
    private Double lat = 0.0;
    private Double longi = 0.0;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar     =   findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        editTextprofileid = (EditText) findViewById(R.id.editTextprofileid);
        editTextname = (EditText) findViewById(R.id.editTextname);
        editTextaddress = (EditText) findViewById(R.id.editTextaddress);
        editTextcity = (EditText) findViewById(R.id.editTextcity);
        editTexttaluka = (EditText) findViewById(R.id.editTexttaluka);
        editTextdistrict = (EditText) findViewById(R.id.editTextdistrict);
        editTextstate = (EditText) findViewById(R.id.editTextstate);
        editTextlocation = (EditText) findViewById(R.id.editTextlocation);
        editTextServingVillage = (EditText) findViewById(R.id.editTextServingVillage);
        editTextspeciality = (EditText) findViewById(R.id.editTextspeciality);
        editTextexperience = (EditText) findViewById(R.id.editTextexperience);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        isdoctor = (CheckBox) findViewById(R.id.isdoctor);
        layoutdoctor = (LinearLayout) findViewById(R.id.layoutdoctor);
        agreement = (TextView) findViewById(R.id.agreement);

        editTextprofileid.setText(Pref.getInstance(getApplicationContext()).getMobileNo());
        editTextprofileid.setEnabled(false);
        isdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isdoctor.isChecked()) {
                    layoutdoctor.setVisibility(View.VISIBLE);
                }else {
                    layoutdoctor.setVisibility(View.GONE);
                }
            }
        });

        agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisclaimer();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextname.getText().toString().equals(""))
                {
                    showAlert("Please enter your name");
                }
                else if(editTextaddress.getText().toString().equals(""))
                {
                    showAlert("Please enter your address");
                }
                else if(editTextcity.getText().toString().equals(""))
                {
                    showAlert("Please enter your city");
                }
                else if(editTexttaluka.getText().toString().equals(""))
                {
                    showAlert("Please enter your taluka");
                }
                else if(editTextdistrict.getText().toString().equals(""))
                {
                    showAlert("Please enter your district");
                } else if(editTextstate.getText().toString().equals(""))
                {
                    showAlert("Please enter your state");
                }
                else
                {

                        postProfile();

                }
            }
        });
        init();
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        dexter();
        startLocationUpdates();
    }
    private void postProfile()
    {
        if (BaseController.isNetworkAvailable(getApplicationContext())) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            final String sprofileid = editTextprofileid.getText().toString();
            final String sname = editTextname.getText().toString();
            final String saddress = editTextaddress.getText().toString();
            final String scity = editTextcity.getText().toString();
            final String staluka = editTexttaluka.getText().toString();
            final String sdistrict = editTextdistrict.getText().toString();
            final String sState = editTextstate.getText().toString();
            final boolean bisdoctor = isdoctor.isChecked();
            final String sspeciality = editTextspeciality.getText().toString();
            final String iexperience = editTextexperience.getText().toString();
            final String servicelocation = editTextlocation.getText().toString();
            final String servingVillage = editTextServingVillage.getText().toString();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // add your other interceptors …
            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            APIService service = retrofit.create(APIService.class);
            Call<SetterResponse> call = service.saveUserProfile(Pref.getInstance(getApplicationContext()).getUserId(), sprofileid, sname, saddress, scity, staluka, sdistrict,sState, bisdoctor, sspeciality, iexperience, servicelocation, servingVillage,lat,longi);
            call.enqueue(new Callback<SetterResponse>() {
                @Override
                public void onResponse(Call<SetterResponse> call, Response<SetterResponse> response) {
                    progressDialog.dismiss();
                    if (response.body().getError().equals(true)) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent target = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(target);
                    } else {
                        Toast.makeText(getApplicationContext(), "Record not saved.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SetterResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Connect to network and refresh", Toast.LENGTH_SHORT).show();
        }
    }

    private void readProfile()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<SetterEditProfile> call = service.userProfile(Pref.getInstance(getApplicationContext()).getUserId());
        call.enqueue(new Callback<SetterEditProfile>()
        {
            @Override
            public void onResponse(Call<SetterEditProfile> call, Response<SetterEditProfile> response)
            {
                progressDialog.dismiss();
                if (response.body().getError().equals(true) )
                {
                    editTextprofileid.setText(Pref.getInstance(getApplicationContext()).getMobileNo());
                    editTextprofileid.setEnabled(false);
                    editTextname.setText(response.body().getName());
                    editTextaddress.setText(response.body().getAddress());
                    editTextcity.setText(response.body().getCity());
                    editTexttaluka.setText(response.body().getTaluka());
                    editTextdistrict.setText(response.body().getDistrict());
                    editTextstate.setText(response.body().getState());
                    isdoctor.setChecked(Boolean.parseBoolean(response.body().getBisdoctor()));

                    if(isdoctor.isChecked()) {

                        layoutdoctor.setVisibility(View.VISIBLE);


                    }else {

                        layoutdoctor.setVisibility(View.GONE);
                    }

                    editTextspeciality.setText(response.body().getSpeciality());
                    editTextexperience.setText(response.body().getExperience());
                    editTextlocation.setText(response.body().getServiceLoc());
                    editTextServingVillage.setText(response.body().getServiceVillage());

                }
                else
                {
                   // Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<SetterEditProfile> call, Throwable t)
            {
                progressDialog.dismiss();
              //  Toast.makeText(getApplicationContext(),"Invalid contact number", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showAlert(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#8472BB'>"+message+"!</font>")).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }


    @Override
    protected void onResume() {
        super.onResume();
        readProfile();
    }

    public void showDisclaimer()
    {
        String s1   = "How to make a safe and correct transaction (Must read ):";
        String s2   = "1.\t Never talk to someone if they are talking about adding advance money online, without seeing your animal.";
        String s3   = "2.\t Such customer will send you a bar code or QR code and scan it. Will ask you to do it, do not do it at all.";
        String s4   = "3.\t The right buyer will always come to see your animal first, only then you will give money.";
        String s5   = "4.\t Block such people immediately or do not pick up the phone.";
        String s6   = "LIMAP is the medium for contact the buyer and seller. LIMAP does not take any responsibility in the transaction of money made by you.";
        String s7   = "•\t Provider- LIMAP";
        String s8   = "•\t Copyright- LIMAP";
        Spanned strMessage = Html.fromHtml(s1+  "<br><br>" +s2+  "<br><br>" +s3+  "<br><br>" +s4+  "<br><br>" +s5+"<br><br>" +s6+"<br><br>" +s7+"<br>" +s8);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(strMessage).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
    }
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }
            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }
            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }
        updateLocationUI();
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            lat = mCurrentLocation.getLatitude();
            longi = mCurrentLocation.getLongitude();
            // editTextFirmName.setText("Lat: " + mCurrentLocation.getLatitude() + ", " +"Lng: " + mCurrentLocation.getLongitude()  );

            // giving a blink animation on TextView
            // editTextFirmName.setAlpha(0);
            //  editTextFirmName.animate().alpha(1).setDuration(300);

            // location last updated time
            //    editTextContactName.setText("Last updated on: " + mLastUpdateTime);
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("LOC", "All location settings are satisfied.");
                        //    Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("LOCATION", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(ProfileEditActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("LOCATION", "Pending Intent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                                Log.e("LOCATION", errorMessage);
                                Toast.makeText(ProfileEditActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                        updateLocationUI();
                    }
                });
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //  Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void dexter() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }
}
