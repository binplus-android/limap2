package com.limap.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.limap.BaseController;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.SetterEditProfile;
import com.limap.Pref.Pref;
import com.limap.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileid,name,address,city,taluka,district,pname;
    private Toolbar toolbar;
    private Button editprofile;
    private BottomNavigationView navigation1;
    RelativeLayout rel_sell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar     =   findViewById(R.id.toolbar);
        toolbar.setTitle("My Profile");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });

        profileid = (TextView) findViewById(R.id.profileid);
        name = (TextView) findViewById(R.id.name);
        pname = (TextView) findViewById(R.id.profilename);
        address = (TextView) findViewById(R.id.address);
        city = (TextView) findViewById(R.id.city);
        taluka = (TextView) findViewById(R.id.taluka);
        district = (TextView) findViewById(R.id.district);

        editprofile =   (Button)findViewById(R.id.editprofile);
        rel_sell = findViewById(R.id.rel_sell);
        navigation1 = (BottomNavigationView) findViewById(R.id.navigation);

        navigation1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                         Intent c = new Intent(ProfileActivity.this,MainActivity.class);
                         startActivity(c);
                        break;
                    case R.id.navigation_favorite:
                        Intent a = new Intent(ProfileActivity.this,MyFavoriteActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigation_sell:
                        Intent b = new Intent(ProfileActivity.this, CheckPostActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigation_adds:
                        Intent d = new Intent(ProfileActivity.this,MyPostAdvertisementActivity.class);
                        startActivity(d);
                        break;
                    case R.id.navigation_account:

                        break;
                }
                return false;
            }
        });
     //   navigation1.setSelectedItemId(R.id.navigation_account);
        navigation1.getMenu().findItem(R.id.navigation_account).setChecked(true);
        navigation1.getMenu().performIdentifierAction(R.id.navigation_account, 0);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent target = new Intent(getApplicationContext(), ProfileEditActivity.class);
                startActivity(target);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        rel_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(ProfileActivity.this, CheckPostActivity.class);
                startActivity(b);
            }
        });


        readProfile();



    }

    private void readProfile() {
        if (BaseController.isNetworkAvailable(getApplicationContext())) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
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
            Call<SetterEditProfile> call = service.userProfile(Pref.getInstance(getApplicationContext()).getUserId());
            call.enqueue(new Callback<SetterEditProfile>() {
                @Override
                public void onResponse(Call<SetterEditProfile> call, Response<SetterEditProfile> response) {
                    progressDialog.dismiss();
                    if (response.body().getError().equals(true)) {
                        profileid.setText(" Mobile No : " + Pref.getInstance(getApplicationContext()).getMobileNo());
                        name.setText("Name : " +response.body().getName());
                        pname.setText("" +response.body().getName());
                        address.setText("Address : " +response.body().getAddress());
                        city.setText("City : " + response.body().getCity());
                        taluka.setText("Taluka : " + response.body().getTaluka());
                        district.setText("District : " + response.body().getDistrict());
                    } else {
                        // Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SetterEditProfile> call, Throwable t) {
                    progressDialog.dismiss();
                    //   Toast.makeText(getApplicationContext(),"Invalid contact number", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Connect to network and refresh", Toast.LENGTH_SHORT).show();
        }
    }
}
