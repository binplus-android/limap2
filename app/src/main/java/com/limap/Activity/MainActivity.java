package com.limap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itparsa.circlenavigation.CircleItem;
import com.itparsa.circlenavigation.CircleNavigationView;
import com.itparsa.circlenavigation.CircleOnClickListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.limap.Adapter.HomeRVAdapter;
import com.limap.BaseController;
import com.limap.BuildConfig;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.LocaleHelper;
import com.limap.Model.SetterAllPostDetails;

import com.limap.Pref.Pref;
import com.limap.R;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Boolean exit = false;

    private SwipeRefreshLayout swipeContainer;
    Locale myLocale;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ArrayList<SetterAllPostDetails> listing;
    HomeRVAdapter recyclerAdapter;

    //public JSONArray jsonArray;
    private LinearLayout cow,buffalo,ox,doctor;
    private boolean loadMore = true;
    private int index = 0;

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
   // CircleNavigationView mCircleNavigationView;
    private int cnt=0;

    Context context;
    Resources resources;
    private String lang;
    private String lang_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Pref.getInstance(getApplicationContext()).getLANGUAGE();
        lang_code = Pref.getInstance(getApplicationContext()).getCODE();

        setAppLocale(lang_code);
        setContentView(R.layout.activity_main);
        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setting the title
        toolbar.setTitle(getString(R.string.app_name));
       // if(!Pref.getInstance(getApplicationContext()).getLANGUAGE().equals("")) {

      //  context = LocaleHelper.setLocale(MainActivity.this, lang_code);


        //placing toolbar in place of actionbar
       // setSupportActionBar(toolbar);
        init();
        requestStoragePermission();
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        dexter();
        startLocationUpdates();
      /*  if(Pref.getInstance(getApplicationContext()).getLANGUAGE().equals("ENGLISH"))
        {
            context = LocaleHelper.setLocale(MainActivity.this, "en");
            resources = context.getResources();

          //  text1.setText(resources.getString(R.string.language));
        }
        //if user select prefered language as Hindi then
        if(Pref.getInstance(getApplicationContext()).getLANGUAGE().equals("Hindi"))
        {
            context = LocaleHelper.setLocale(MainActivity.this, "hi");
            resources = context.getResources();

           // text1.setText(resources.getString(R.string.language));
        }*/

        swipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                index = 0;
                //  jsonArray = null;
                readAdds();
                loadMore = false;

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        cow = findViewById(R.id.cow);
        buffalo = findViewById(R.id.buffalo);
        ox=findViewById(R.id.ox);
        doctor=findViewById(R.id.doctor);
        //list of history
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //if recycler size is fixed
        //RecyclerView Layout
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onScrolledToEnd() {

                if (loadMore) {

                    index += 10;

                    readAdds();

                    loadMore = false;

                }
            }
        });

        //   jsonArray = null;
        index = 0;
        readAdds();

       /* final CircleNavigationView mCircleNavigationView;
        mCircleNavigationView = (CircleNavigationView) findViewById(R.id.navigation);
        mCircleNavigationView.initWithSaveInstanceState(savedInstanceState);
        mCircleNavigationView.setCentreButtonSelectable(true);

        String homestring = getResources().getString(R.string.title_home);
        String favstring = getResources().getString(R.string.title_favorite);
        String addstring = getResources().getString(R.string.title_adds);
        String accstring = getResources().getString(R.string.title_account);

        mCircleNavigationView.addCircleItem(
                new CircleItem(homestring, R.drawable.ic_home_black_24dp, getResources().getColor(R.color.colorAccent)));
        mCircleNavigationView.addCircleItem(new CircleItem(favstring, R.drawable.ic_baseline_favorite_24, getResources().getColor(R.color.colorAccent)));
        mCircleNavigationView.addCircleItem(new CircleItem(addstring, R.drawable.ic_baseline_pets_24, getResources().getColor(R.color.colorAccent)));
        mCircleNavigationView.addCircleItem(new CircleItem(accstring, R.drawable.ic_baseline_account_circle_24, getResources().getColor(R.color.colorAccent)));
//        mCircleNavigationView.setCenterButtonSelectedIcon(R.drawable.ic_chat);
        mCircleNavigationView.setCenterButtonResourceBackground(R.drawable.ic_baseline_add_circle_24);

        mCircleNavigationView.shouldShowFullBadgeText(true);
        mCircleNavigationView.setCentreButtonIconColorFilterEnabled(false);
        mCircleNavigationView.setCircleOnClickListener(new CircleOnClickListener() {
            @Override
            public void onCentreButtonClick() {
              //  mCircleNavigationView.showBadgeAtIndex(2, 80, getResources().getColor(R.color.colorAccent)
                //        , 16, getResources().getColor(R.color.colorAccent));

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
/*
                if (itemIndex == 2)
                    mCircleNavigationView.hideBadgeAtIndex(2);

                if (itemIndex == 0)
                    mCircleNavigationView.showBadgeAtIndexWithoutText(3, 8, getResources().getColor(R.color.colorAccent));

                if (itemIndex == 3)
                    mCircleNavigationView.hideBadgeAtIndex(3);

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });
//        mCircleNavigationView.shouldShowFullBadgeText(false);


//        mCircleNavigationView.setCentreButtonIcon(R.drawable.ic_home);
//        mCircleNavigationView.setActiveCentreButtonIconColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
//        mCircleNavigationView.setActiveCentreButtonBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));

*/

  BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_favorite:
                         Intent a = new Intent(MainActivity.this,MyFavoriteActivity.class);
                         startActivity(a);
                        break;
                    case R.id.navigation_sell:
                        Intent b = new Intent(MainActivity.this, CheckPostActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigation_adds:
                          Intent c = new Intent(MainActivity.this,MyPostAdvertisementActivity.class);
                          startActivity(c);
                        break;
                    case R.id.navigation_account:
                        Intent d = new Intent(MainActivity.this, CheckAccountActivity.class);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });

        cow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("category","cow");
                startActivity(intent);
            }
        });
        buffalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("category","buffalo");
                startActivity(intent);
            }
        });
        ox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("category","ox");
                startActivity(intent);
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DoctorListActivity.class);
                intent.putExtra("category","Doctor List");
                startActivity(intent);
            }
        });
    }

    private void readAdds() {

        cnt++;
      //  final ProgressDialog progressDialog = new ProgressDialog(this);
      //  progressDialog.setMessage("Wait...");
     //   progressDialog.show();
        try {

            if (BaseController.isNetworkAvailable(getApplicationContext())) {
                swipeContainer.setRefreshing(true);

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                //   OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                // add your other interceptors …
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.connectTimeout(3000, TimeUnit.SECONDS);
                httpClient.readTimeout(3000, TimeUnit.SECONDS);
                httpClient.writeTimeout(3000, TimeUnit.SECONDS);
                // add logging as last interceptor
                httpClient.addInterceptor(logging);  // <-- this is the important line

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(APIUrl.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                APIService service = retrofit.create(APIService.class);
                Call<List<SetterAllPostDetails>> call = service.homePostAll(lat,longi);
                call.enqueue(new Callback<List<SetterAllPostDetails>>()
                {
                    @Override
                    public void onResponse(Call<List<SetterAllPostDetails>> call, Response<List<SetterAllPostDetails>> response)
                    {
                     //   progressDialog.dismiss();
                        init();
                        startLocationUpdates();
//                        Log.e("responseeee", "onResponse: "+response.toString() );
                       for(int i=0;i<response.body().size();i++){
                           Log.e("HOme_DAta", "onResponse: "+response.body().get(i).getDistance() );
                       }

                        List<SetterAllPostDetails> datumList1 = response.body();
                        if(datumList1.size()>0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerAdapter = new HomeRVAdapter(datumList1, getApplicationContext());
                            RecyclerView.LayoutManager recyce = new LinearLayoutManager(getApplicationContext());

                            recyclerView.setLayoutManager(recyce);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();

                            swipeContainer.setRefreshing(false);
                        }
                        else
                        {
                            if(cnt<=5) {
                                readAdds();
                            }
                            else
                            {
                                swipeContainer.setRefreshing(false);
                              //  progressDialog.dismiss();
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<SetterAllPostDetails>> call, Throwable t)
                    {
                       // progressDialog.dismiss();
                     //   Toast.makeText(getApplicationContext(),"Invalid contact number", Toast.LENGTH_LONG).show();
                    }
                });

            } else {

                swipeContainer.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                //emptyview.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Connect to network and refresh", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            swipeContainer.setRefreshing(false);
            Log.e("ERORR", "" + e);
        }
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
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("LOCATION", "Pending Intent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                                Log.e("LOCATION", errorMessage);
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //        Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    // navigating user to app settings
    private void openSettings1() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showSettingsDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                openSettings1();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish(); // finish activity
            System.exit(0);
            return;
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    private void setLanguage(String language,String code) {

        myLocale = new Locale(code);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuSearch:
               // Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }*/
}
