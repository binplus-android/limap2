package com.limap.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.limap.BaseController;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.SetterLogin;
import com.limap.Pref.Pref;
import com.limap.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPActivity extends AppCompatActivity {
    EditText editTextMobNo;
    Button btnGetOTP;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_otp);
        toolbar     =   findViewById(R.id.toolbar);
        toolbar.setTitle("Registration");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Your code
                finish();
            }
        });
        editTextMobNo = (EditText) findViewById(R.id.editTextMobileNo);
        btnGetOTP = (Button) findViewById(R.id.btnGetOTP);

        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMobNo.getText().toString().equals("")) {
                    showAlert("Please enter Mobile No ");
                } else if (editTextMobNo.getText().length() != 10) {
                    showAlert("Mobile number should be 10 digit ");
                } else {

                    getOtp();

                }
            }
        });
    }
    private void getOtp() {
        if (BaseController.isNetworkAvailable(getApplicationContext()))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait...");
            progressDialog.show();
            final String mob = editTextMobNo.getText().toString().trim();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(APIUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIService service = retrofit.create(APIService.class);
            Call<SetterLogin> call = service.userLogin(mob);
            call.enqueue(new Callback<SetterLogin>() {
                @Override
                public void onResponse(Call<SetterLogin> call, Response<SetterLogin> response) {
                    progressDialog.dismiss();
                    Log.e("register_res",response.body()+"");
                    if (response.body().getError().equals(true)) {

                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Intent target = new Intent(getApplicationContext(), OTPVerifyActivity.class);
//                        Intent target = new Intent(getApplicationContext(), ProfileEditActivity.class);//
//                        Pref.getInstance(getApplicationContext()).setUserId(response.body().getApp_user_id());//
                        Pref.getInstance(getApplicationContext()).setMobileNo(mob); //temp please removew
                        target.putExtra("app_user_id", response.body().getApp_user_id());
                        target.putExtra("mobile_no", mob);
                        Log.e("OTPACTIVYT", "onResponse: "+mob+" :: "+ response.body().getApp_user_id());
                        startActivity(target);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SetterLogin> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_LONG).show();
                }
            });
        }
        else
            {
            Toast.makeText(getApplicationContext(), "Connect to network and refresh", Toast.LENGTH_SHORT).show();
        }
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent target = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(target);
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

}
