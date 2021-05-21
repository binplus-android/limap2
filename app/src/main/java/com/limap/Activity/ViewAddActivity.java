package com.limap.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.limap.Adapter.ViewPagerImageAdapter;
import com.limap.BuildConfig;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.SetterAllPostDetails;
import com.limap.Model.SetterLogin;
import com.limap.Model.SetterResponse;
import com.limap.Pref.Pref;
import com.limap.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewAddActivity extends AppCompatActivity {

    public ViewPager viewPager;
    ImageView pre;
    ImageView next;
    Bundle bundle;
    public JSONObject object;
    TextView title;
    TextView speciality;
    TextView variety;
    TextView age;
    TextView vet;
    TextView milkhistory;
    TextView description;
    ImageView floatingActionButton,imageViewCall,imageViewShare;
    private String post_id,mobile_no;
    File imagePath;
    SetterAllPostDetails setterAllPostDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        title = findViewById(R.id.title);
        speciality = findViewById(R.id.speciality);
        variety = findViewById(R.id.variety);
        age = findViewById(R.id.age);
        vet = findViewById(R.id.vet);
        milkhistory = findViewById(R.id.milkhistory);
        description = findViewById(R.id.description);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        imageViewCall = findViewById(R.id.imageViewCall);
        imageViewShare = findViewById(R.id.imageViewShare);


        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                shareIt();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });

        bundle = getIntent().getExtras();
        Log.d("advertise", bundle.getString("id"));
        post_id =   bundle.getString("id");
        setterAllPostDetails = (SetterAllPostDetails) bundle.getSerializable("model");
        Log.e("setterDetial",setterAllPostDetails.toString());
        title.setText(bundle.getString("category"));
        toolbar.setTitle(bundle.getString("category"));

        if(bundle.getString("speciality").equals(""))
        {
            milkhistory.setText("Pregnancy Status : N/A");
        }
        else {
            speciality.setText(bundle.getString("speciality"));
        }

        variety.setText(bundle.getString("variety"));
        age.setText("Age : " +bundle.getString("age"));

        if(bundle.getString("speciality").equals("Calf"))
        {
            milkhistory.setText("Last Milk History : N/A");
            vet.setText("Lactation Period : N/A");
        }

        if(bundle.getString("category").equals("Ox"))
        {
            milkhistory.setText("Last Milk History : N/A");
            vet.setText("Lactation Period : N/A");
        }
        else
        {
            milkhistory.setText("Last Milk History : " +bundle.getString("milkhistory")+" ltr");
            vet.setText("Lactation Period : " +bundle.getString("vet"));
        }

        description.setText(bundle.getString("description"));
        mobile_no   =   bundle.getString("mobile_no");

        imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile_no));
                if (ActivityCompat.checkSelfPermission(ViewAddActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ViewAddActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 143);
                }
                else {
                    v.getContext().startActivity(intent);
                }
            }
        });
        viewPager = findViewById(R.id.pager);
      /*  bundle.getString("image1"),
                bundle.getString("image2"),
                bundle.getString("image3"),
                bundle.getString("image4"),
                bundle.getString("image5"),
                bundle.getString("video1")*/
        // adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), this);
        final ViewPagerImageAdapter adapter = new ViewPagerImageAdapter(ViewAddActivity.this,
                bundle.getString("image1"),
                bundle.getString("image2"),
                bundle.getString("image3"),
                bundle.getString("image5")
                ,true);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                //actionBar.setSelectedNavigationItem(position);
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavourite();

            }
        });
        if(!Pref.getInstance(getApplicationContext()).getUserId().equals("")) {
            getFavourite();
        }
//        Handler handler = new Handler();
//
//        Runnable update = () -> {
//
//            if (currentPage == NUM_PAGES) {
//                currentPage = 0;
//            }
//            viewPager.setCurrentItem(currentPage++, true);
//
//
//        };
//
//        new Timer().schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                handler.post(update);
//            }
//        }, 2000, 3000);

        pre = findViewById(R.id.pre);
        next = findViewById(R.id.next);

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
            }
        });

        requestStoragePermission();
    }
    public void setFavourite()
    {
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
        Call<SetterResponse> call = service.setFavourite(post_id, Pref.getInstance(getApplicationContext()).getUserId());
        call.enqueue(new Callback<SetterResponse>()
        {
            @Override
            public void onResponse(Call<SetterResponse> call, Response<SetterResponse> response)
            {
                if (response.body().getError().equals(true) )
                {
                    //floatingActionButton.setBackgroundColor(Color.RED);
                    floatingActionButton.setBackgroundResource(R.drawable.shape_like_circle);
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                else
                {
                    floatingActionButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                getFavourite();
            }
            @Override
            public void onFailure(Call<SetterResponse> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),"Invalid", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getFavourite()
    {
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
        Call<SetterResponse> call = service.getFavourite(post_id,Pref.getInstance(getApplicationContext()).getUserId());
        call.enqueue(new Callback<SetterResponse>()
        {
            @Override
            public void onResponse(Call<SetterResponse> call, Response<SetterResponse> response)
            {
                if (response.body().getError().equals(true))
                {
                    //floatingActionButton.setBackgroundColor(Color.RED);
                    floatingActionButton.setBackgroundResource(R.drawable.shape_like_circle);
                }
                else
                {

                }
            }
            @Override
            public void onFailure(Call<SetterResponse> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),"Invalid", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String randString =   getSaltString();
        String imageFileName = "screenshot_" + timeStamp+"_"+randString + ".png";
        imagePath = new File((ViewAddActivity.this).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/"+imageFileName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
    private void shareIt() {

//        Uri uri=null;
//        if(BuildConfig.VERSION_CODE==Build.)
        Uri uri = Uri.fromFile(imagePath);
//        Uri uri = FileProvider.getUriForFile(ViewAddActivity.this, BuildConfig.APPLICATION_ID + ".provider",imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "https://play.google.com/store/apps/details?id=com.limap";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "LIMAP");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    private void requestStoragePermission()
    {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener()
                {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(
                        new PermissionRequestErrorListener() {
                            @Override
                            public void onError(DexterError error) {
                                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                .onSameThread()
                .check();
    }
    private void showSettingsDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }
    // navigating user to app settings
    private void openSettings()
    {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
