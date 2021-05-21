package com.limap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.limap.Adapter.HomeAdapter;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private final String TAG=MainActivity.class.getSimpleName();
    private Boolean exit = false;
    RelativeLayout rel_sell ;
    private SwipeRefreshLayout swipeContainer;
    Locale myLocale;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ArrayList<SetterAllPostDetails> listing;
    HomeAdapter recyclerAdapter;
    ProgressBar progressBar;

    //public JSONArray jsonArray;
    private LinearLayout cow,buffalo,ox,doctor;
    private boolean loadMore = true;
    private int index = 0;

    // location last updated time
    private String mLastUpdateTime;
    private Double lat = 0.0;
    private Double longi = 0.0;
    private String pincode = "";
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
    // for scrolls
    boolean continue_request;
    int page = 0;
    private int currentVisibleItemCount;
    private int currentFirstVisibleItem;
    private int totalItem;
    LinearLayoutManager manager ;

    private boolean shouldRefreshOnResume = false;
    private boolean isFirst = true;
    List<SetterAllPostDetails> datumList1;
    int permsRequestCode = 200 ;
    String[] perms = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.CALL_PHONE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = Pref.getInstance(getApplicationContext()).getLANGUAGE();
        lang_code = Pref.getInstance(getApplicationContext()).getCODE();

        setAppLocale(lang_code);
        setContentView(R.layout.activity_main);
        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.pbar);
        //setting the title
        toolbar.setTitle(getString(R.string.app_name));
        datumList1=new ArrayList<>();
        lat = Double.valueOf(Pref.getInstance(MainActivity.this).getLATITUDE());
        longi = Double.valueOf(Pref.getInstance(MainActivity.this).getLONGITUDE());
        init();
//        if (!checkPermission()) {
//            requestPermissions(perms, permsRequestCode);
//        } else {
            checkLocation();
//        }

        if(lat!=0.0 &&  longi!=0.0){
            index = 0;
            page=0;
            readAddsWithPaging(page);
        }else{
            Handler handler=new Handler();
            Runnable r=new Runnable() {
                @Override
                public void run() {
                    page=0;
                    readAddsWithPaging(page);

                }
            };
            handler.postDelayed(r,2000);
        }
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
                page=0;
                datumList1.clear();
                readAddsWithPaging(page);

            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //if recycler size is fixed
        //RecyclerView Layout
        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdapter = new HomeAdapter( getApplicationContext(),datumList1);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    continue_request=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentFirstVisibleItem = manager.findFirstVisibleItemPosition();
                currentVisibleItemCount = manager.getChildCount();
                totalItem = manager.getItemCount();

                if (continue_request && (currentFirstVisibleItem + currentVisibleItemCount == totalItem)) {
                    continue_request = false;
                    page = page + 1;
                    Log.e(TAG, "onScrolled: "+page );
                    readAddsWithPaging(page);

                }
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
        rel_sell = findViewById(R.id.rel_sell);
        //list of history

//        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
//            @Override
//            public void onScrolledToEnd() {
//
//                if (loadMore) {
//
//                    index += 10;
//
//                    readAdds();
//
//                    loadMore = false;
//
//                }
//            }
//        });

        //   jsonArray = null;


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
        rel_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(MainActivity.this, CheckPostActivity.class);
                startActivity(b);
            }
        });

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

        private void readAddsWithPaging(int page) {
        progressBar.setVisibility(View.VISIBLE);

            cnt++;

            try {

                if (BaseController.isNetworkAvailable(getApplicationContext())) {
    //                swipeContainer.setRefreshing(true);

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
                    Log.e(TAG, "readAdds: "+lat+" :: "+longi +" ::"+page );
//                    Double.parseDouble("19.7754276"),Double.parseDouble("74.0348058")
                    Call<List<SetterAllPostDetails>> call = service.homePostAll(lat,longi,String.valueOf(page));
                    call.enqueue(new Callback<List<SetterAllPostDetails>>()
                    {
                        @Override
                        public void onResponse(Call<List<SetterAllPostDetails>> call, Response<List<SetterAllPostDetails>> response)
                        {

                            continue_request=false;
                            datumList1.addAll(response.body());
                            if(datumList1.size()>0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerAdapter.notifyDataSetChanged();
                                if(swipeContainer.isRefreshing()){
                                    swipeContainer.setRefreshing(false);
                                }

                            }
                            else
                            {
    //                            if(cnt<=5) {
    //                                page=0;
    //                                readAddsWithPaging(page);
    //                            }
    //                            else
    //                            {
    //                                swipeContainer.setRefreshing(false);
    //                                //  progressDialog.dismiss();
    //                                recyclerView.setVisibility(View.GONE);
    //                            }
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
            progressBar.setVisibility(View.GONE);
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


    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            lat = mCurrentLocation.getLatitude();
            longi = mCurrentLocation.getLongitude();
            List<Address> addresses = getMapAddress();
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                pincode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

            }
            Log.e("location", "lat--" + lat + "--long---" + longi + "---pin---" + pincode);
            Pref.getInstance(getApplicationContext()).setLocation(String.valueOf(lat), String.valueOf(longi), pincode);
            stopLocationUpdates();
     

        }
    }

    private void checkLocation() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                    .setMessage("Location is not Enabled")
                    .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            updateLocationUI();
        }
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
    private List<Address> getMapAddress() {
        List<Address> addresses = new ArrayList<>();
        String zip = "";
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, longi, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();
            Log.e("map_data_main", "getMapAddress: " + "" + address + "\n" + city + "\n" + state + "\n" + zip + "\n" + country);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addresses;

    }



    private boolean checkPermission() {
        int r = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int r1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);


        return r== PackageManager.PERMISSION_GRANTED && r1 == PackageManager.PERMISSION_GRANTED ;
    }


    @Override
    public void onRequestPermissionsResult(final int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean locationAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean c_locAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;



                if (locationAccepted && c_locAccepted )
                {
//                   startLocationUpdates();
                    checkPermission();
                }
                else
                {
//                    module.showErrorSnackBar(Splash_activity.this,"Accept Permissions to Continue");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                                    .setMessage("You need to allow access to both the permissions")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(perms,
                                                        permsRequestCode);
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();

                            return;
                        }
                    }
                }
                break;

        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermission()) {
            requestPermissions(perms, permsRequestCode);
        } else {
            checkLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPermission()) {
            requestPermissions(perms, permsRequestCode);
        } else {
            checkLocation();
        }
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
