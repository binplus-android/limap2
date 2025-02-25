package com.limap.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.limap.Adapter.CategoryAddRVAdapter;
import com.limap.Adapter.DoctorRVAdapter;
import com.limap.BaseController;
import com.limap.BuildConfig;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.SetterAllPostDetails;
import com.limap.Model.SetterDoctorList;
import com.limap.Pref.Pref;
import com.limap.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

public class DoctorListActivity extends AppCompatActivity {
    private final String TAG=DoctorListActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeContainer;;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewlayoutManager;
    private ArrayList<SetterDoctorList> listing;
    private DoctorRVAdapter recyclerAdapter;

    //public JSONArray jsonArray;

    private boolean loadMore = true;
    private int index = 0;

    private int cnt=0;

    String category;
    private BottomNavigationView navigation1;
    // location last updated time
    private String mLastUpdateTime;
    private Double lat=0.0;
    private Double longi=0.0;
    private  String pincode="";
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
    private Boolean mRequestingLocationUpdates=false;
    Toolbar toolbar;
    int permsRequestCode = 200 ;
    String[] perms = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.CALL_PHONE"};

    // for scrolls
    boolean continue_request;
    int page = 0;
    private int currentVisibleItemCount;
    private int currentFirstVisibleItem;
    private int totalItem;
    LinearLayoutManager manager ;
    List<SetterDoctorList> datumList1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);
        toolbar     =   findViewById(R.id.toolbar);
        toolbar.setTitle("Doctors in My Area");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your code
                finish();
            }
        });
        Bundle b = getIntent().getExtras();
        datumList1=new ArrayList<>();
        category = "";
        if (b != null) {

            category = b.getString("category");
            String output = category.substring(0, 1).toUpperCase() + category.substring(1);
            toolbar.setTitle(output);
            setTitle(category);
        }

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

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        navigation1 = (BottomNavigationView) findViewById(R.id.navigation);
        navigation1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        break;
                    case R.id.navigation_favorite:
                        Intent b = new Intent(DoctorListActivity.this, MyFavoriteActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigation_sell:
                        Intent c = new Intent(DoctorListActivity.this, CheckPostActivity.class);
                        startActivity(c);
                        break;
                    case R.id.navigation_adds:
                        Intent a = new Intent(DoctorListActivity.this,MyPostAdvertisementActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigation_account:
                        Intent d = new Intent(DoctorListActivity.this,CheckAccountActivity.class);
                        startActivity(d);
                        break;
                }
                return false;
            }
        });
        //   navigation1.setSelectedItemId(R.id.navigation_account);
        navigation1.getMenu().findItem(R.id.navigation_home).setChecked(false);

        //list of history
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //if recycler size is fixed
        //RecyclerView Layout
        manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdapter = new DoctorRVAdapter(datumList1, getApplicationContext());
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
        index = 0;


        init();
//        if (!checkPermission()) {
//            requestPermissions(perms, permsRequestCode);
//        } else {
            checkLocation();
//        }

       readAddsWithPaging(page);
    }

    private void readAddsWithPaging(int pg) {
        cnt++;
      //  final ProgressDialog progressDialog = new ProgressDialog(this);
      //  progressDialog.setMessage("Wait...");
      //  progressDialog.show();
        try {
            if (BaseController.isNetworkAvailable(getApplicationContext())) {
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
                Call<List<SetterDoctorList>> call = service.doctorList(lat,longi,String.valueOf(page));
                call.enqueue(new Callback<List<SetterDoctorList>>()
                {
                    @Override
                    public void onResponse(Call<List<SetterDoctorList>> call, Response<List<SetterDoctorList>> response)
                    {
                     //   progressDialog.dismiss();

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

                        }
                    }
                    @Override
                    public void onFailure(Call<List<SetterDoctorList>> call, Throwable t)
                    {
                        //progressDialog.dismiss();
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
            new AlertDialog.Builder(DoctorListActivity.this)
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
            Geocoder geocoder = new Geocoder(DoctorListActivity.this, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, longi, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();
            Log.e("map_data_splash", "getMapAddress: " + "" + address + "\n" + city + "\n" + state + "\n" + zip + "\n" + country);
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
                            new AlertDialog.Builder(DoctorListActivity.this)
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
    protected void onPause()
    {
        super.onPause();
    
            // pausing location updates
            stopLocationUpdates();
        
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
}
