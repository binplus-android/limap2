package com.limap.Activity;

import android.Manifest;
import android.app.AlertDialog;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.limap.BuildConfig;
import com.limap.Pref.Pref;
import com.limap.R;

import java.text.DateFormat;
import java.util.Date;

public class PostAdvertisementActivity extends AppCompatActivity {
    private final String TAG=PostAdvertisementActivity.class.getSimpleName();
    private Toolbar toolbar;
    Button next;

    String[] variety_cowox;
    String[] variety_buffalo;
    String[] vet_cow_buffalo;
    String[] preg_status;

    ArrayAdapter adaptervariety;
    ArrayAdapter adaptervet;
    ArrayAdapter adapterpreg;

    Spinner variety;
    EditText age;

    Spinner vet;
    Spinner pregStatus;
    EditText milkhistory;

    RadioButton cow;
    RadioButton buffalo;
    RadioButton ox;


    TextView tvspeciality;
    RadioGroup specialitycb;

    RadioGroup category;

    EditText editTextdescription;

    LinearLayout agelayout;
    LinearLayout vetlayout;
    LinearLayout milkhistorylayout;
    LinearLayout preglayout;

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
    private RadioButton gabhan,dubhti,vasaru;
    private String varietyText="", vetText="",pregStatusText="";
    private String cat_text="Cow";
    private String speciality_text ="preg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Post Advertisement");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Pref.getInstance(getApplicationContext()).getUserId().equals("")) {
            Intent target = new Intent(getApplicationContext(), OTPActivity.class);
            startActivity(target);
        }
        tvspeciality = findViewById(R.id.tvspeciality);
        specialitycb = findViewById(R.id.specialitycb);

        variety_cowox = getResources().getStringArray(R.array.array_variety_cowox);
        variety_buffalo = getResources().getStringArray(R.array.array_variety_buffalo);
        vet_cow_buffalo = getResources().getStringArray(R.array.array_vet);
        preg_status = getResources().getStringArray(R.array.array_preg);

        variety = findViewById(R.id.spinnerVariety);
        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_cowox);
        variety.setAdapter(adaptervariety);
        // varietyText = variety.getSelectedItem().toString();

        variety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                varietyText = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        preglayout = findViewById(R.id.preglayout);
        pregStatus = findViewById(R.id.spinnerPregStatus);
        adapterpreg = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, preg_status);
        pregStatus.setAdapter(adapterpreg);
        // varietyText = variety.getSelectedItem().toString();

        pregStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pregStatusText = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gabhan  =   findViewById(R.id.gabhan);
        gabhan.setOnCheckedChangeListener(new Radio_Speciality());
        dubhti  =   findViewById(R.id.dubhti);
        dubhti.setOnCheckedChangeListener(new Radio_Speciality());
        vasaru  =   findViewById(R.id.vasaru);
        vasaru.setOnCheckedChangeListener(new Radio_Speciality());


        agelayout = findViewById(R.id.agelayout);
        age = findViewById(R.id.editTextAge);

        vetlayout = findViewById(R.id.vetlayout);
        vet = findViewById(R.id.spinnerVet);
        adaptervet = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vet_cow_buffalo);
        vet.setAdapter(adaptervet);
        vet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vetText = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //vetText =  vet.getSelectedItem().toString();
        milkhistorylayout = findViewById(R.id.milkhistorylayout);
        milkhistory = findViewById(R.id.editTextmilkhistory);

        editTextdescription = findViewById(R.id.editTextdescription);

        category = findViewById(R.id.category);
        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.cow:
                        setCow();
                        break;
                    case R.id.buffalo:
                        setBuffalo();
                        break;
                    case R.id.ox:
                        setOx();
                        break;
                }

            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(varietyText.equals(""))
                {
                    showAlert("Please select variety");
                }
                else if(age.getText().toString().equals(""))
                {
                    showAlert("Please enter age");
                }
                else {
                    next();
                }
            }
        });

        setCow();
        init();
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        dexter();
        startLocationUpdates();
    }
    class Radio_Speciality implements  CompoundButton.OnCheckedChangeListener
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (gabhan.isChecked())
            {
                speciality_text = "preg";
                if(cat_text.equals("Cow"))
                {
                    setCow();
                }
                else if(cat_text.equals("Buffalo"))
                {
                    setBuffalo();
                }
                else if (cat_text.equals("Ox"))
                {
                    setOx();
                }
            }
            if(vasaru.isChecked())
            {
                speciality_text = "calf";
                if(cat_text.equals("Cow"))
                {
                    setCow();
                }
                else if(cat_text.equals("Buffalo"))
                {
                    setBuffalo();
                }
                else if (cat_text.equals("Ox"))
                {
                    setOx();
                }
            }
            if(dubhti.isChecked()) {
                speciality_text = "milking";
                if(cat_text.equals("Cow"))
                {
                    setCow();
                }
                else if(cat_text.equals("Buffalo"))
                {
                    setBuffalo();
                }
                else if (cat_text.equals("Ox"))
                {
                    setOx();
                }
            }
        }
    }
    private void next() {

        try {
            Bundle bundle = new Bundle();

            switch (category.getCheckedRadioButtonId()) {

                case R.id.cow:

                    bundle.putString("category", "Cow");
                    break;
                case R.id.buffalo:

                    bundle.putString("category", "Buffalo");
                    break;

                case R.id.ox:

                    bundle.putString("category", "Ox");
                    break;
            }

            switch (specialitycb.getCheckedRadioButtonId()) {

                case R.id.gabhan:

                    bundle.putString("speciality", "Pregnant");
                    break;
                case R.id.dubhti:

                    bundle.putString("speciality", "Milking");
                    break;

                case R.id.vasaru:

                    bundle.putString("speciality", "Calf");
                    break;
            }

            bundle.putString("variety", varietyText);
            Log.e(TAG, "next: "+age.getText().toString() );
            bundle.putString("age", age.getText().toString());


            bundle.putString("vet", vetText);
            bundle.putDouble("lat", lat);
            bundle.putDouble("longi", longi);

            if (milkhistory.getText().length() > 0) {

                bundle.putString("milkhistory", milkhistory.getText().toString());
            } else {

                bundle.putString("milkhistory", "0");
            }

            bundle.putString("description", editTextdescription.getText().toString());
            bundle.putString("preg_status", pregStatusText);

            Intent target = new Intent(getApplicationContext(), UploadPhotosActivity.class);
            target.putExtras(bundle);
            startActivity(target);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error:" + e, Toast.LENGTH_LONG).show();
            Log.e("ERROr", "" + e);
        }
    }

    private void setCow() {
        cat_text    =   "Cow";
        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_cowox);
        variety.setAdapter(adaptervariety);

        tvspeciality.setVisibility(View.VISIBLE);
        specialitycb.setVisibility(View.VISIBLE);

        //  agelayout.setHint(getString(R.string.hint_age_cowbuffalo));
        if(speciality_text.equals("preg"))
        {
            milkhistorylayout.setVisibility(View.VISIBLE);
            preglayout.setVisibility(View.VISIBLE);
            vetlayout.setVisibility(View.GONE);
        }
        else if(speciality_text.equals("milking"))
        {
            milkhistorylayout.setVisibility(View.VISIBLE);
            preglayout.setVisibility(View.GONE);
            vetlayout.setVisibility(View.VISIBLE);
        }
        else if(speciality_text.equals("calf"))
        {
            tvspeciality.setVisibility(View.VISIBLE);
            specialitycb.setVisibility(View.VISIBLE);
            vetlayout.setVisibility(View.GONE);
            milkhistorylayout.setVisibility(View.GONE);
            preglayout.setVisibility(View.GONE);
        }
    }

    private void setBuffalo() {
        cat_text    =   "Buffalo";
        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_buffalo);
        variety.setAdapter(adaptervariety);

        tvspeciality.setVisibility(View.VISIBLE);
        specialitycb.setVisibility(View.VISIBLE);

        //  agelayout.setHint(getString(R.string.hint_age_cowbuffalo));
        if(speciality_text.equals("preg"))
        {
            milkhistorylayout.setVisibility(View.VISIBLE);
            preglayout.setVisibility(View.VISIBLE);
            vetlayout.setVisibility(View.GONE);
        }
        else if(speciality_text.equals("milking"))
        {
            milkhistorylayout.setVisibility(View.VISIBLE);
            preglayout.setVisibility(View.GONE);
            vetlayout.setVisibility(View.VISIBLE);
        }
        else if(speciality_text.equals("calf"))
        {
            tvspeciality.setVisibility(View.VISIBLE);
            specialitycb.setVisibility(View.VISIBLE);
            vetlayout.setVisibility(View.GONE);
            milkhistorylayout.setVisibility(View.GONE);
            preglayout.setVisibility(View.GONE);
        }
    }

    private void setOx() {
        cat_text    =   "Ox";
        adaptervariety = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, variety_cowox);
        variety.setAdapter(adaptervariety);

        if(speciality_text.equals("preg"))
        {
            tvspeciality.setVisibility(View.GONE);
            specialitycb.setVisibility(View.GONE);
            vetlayout.setVisibility(View.GONE);
            milkhistorylayout.setVisibility(View.GONE);
            preglayout.setVisibility(View.GONE);
        }
        else if(speciality_text.equals("milking"))
        {
            tvspeciality.setVisibility(View.GONE);
            specialitycb.setVisibility(View.GONE);
            vetlayout.setVisibility(View.GONE);
            milkhistorylayout.setVisibility(View.GONE);
            preglayout.setVisibility(View.GONE);
        }
        else if(speciality_text.equals("calf"))
        {
            tvspeciality.setVisibility(View.VISIBLE);
            specialitycb.setVisibility(View.GONE);
            vetlayout.setVisibility(View.GONE);
            milkhistorylayout.setVisibility(View.GONE);
            preglayout.setVisibility(View.GONE);
        }
        else
        {
            tvspeciality.setVisibility(View.GONE);
            specialitycb.setVisibility(View.GONE);
            vetlayout.setVisibility(View.GONE);
            milkhistorylayout.setVisibility(View.GONE);
            preglayout.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                                    rae.startResolutionForResult(PostAdvertisementActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("LOCATION", "Pending Intent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                                Log.e("LOCATION", errorMessage);
                                Toast.makeText(PostAdvertisementActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }
}
