package com.limap.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.limap.BaseController;
import com.limap.Interface.APIService;
import com.limap.Model.APIUrl;
import com.limap.Model.ServerResponse;
import com.limap.Model.SetterEditProfile;
import com.limap.Pref.Pref;
import com.limap.R;
import com.limap.Upload.FileUtils;
import com.limap.Upload.Utility;
import com.limap.Upload.Utils;
import com.yalantis.ucrop.UCrop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadPhotosActivity extends AppCompatActivity {

    Activity context=UploadPhotosActivity.this;
    String selectedVideoPath;
    private Toolbar toolbar;
    int PERMISSION_ALL = 1;

    int PICK_IMAGE_CAMERA = 1;
    int PICK_IMAGE_GALLERY = 2;
    int PICK_IMAGE_VIDEO = 3;
    private static final int CAMERA_REQUEST_CODE = 1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    private String userChoosenTask;

    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    View view;
    private CardView card1;
    private CardView card2;
    private CardView card3;
    ///private CardView card4;
    private CardView card5;
    //private CardView card6;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
   //private ImageView image4;
    private ImageView image5;
   // private ImageView image6;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
   // private TextView textView4;
    private TextView textView5;
   // private TextView textView6;

    private int frame1;

    private Button postadd;
    String mCurrentPhotoPath;
    Uri resultUri;
    Bundle bundle;
    String imgFileName="",finalpath_org,finalpath_crop;
//mediaPath4="",,mediaPath6=""
    private String mediaPath1="",mediaPath2="",mediaPath3="",mediaPath5="";
    ProgressBar progressBar;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    byte[] videoBytes;
    Uri selectedImage;
    private String app_user_id="",profileid="",category="",speciality="",variety="",vet="",description="",preg_status="",latitude="",longitude="",city="",age="",lastmilkhistory="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photos);
        toolbar     =   findViewById(R.id.toolbar);
        toolbar.setTitle("Post Advertisement");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        bundle = getIntent().getExtras();

        app_user_id =   Pref.getInstance(getApplicationContext()).getUserId();
        profileid   =   Pref.getInstance(getApplicationContext()).getMobileNo();
        category    =   bundle.getString("category");
        speciality  =   bundle.getString("speciality");
        variety     =   bundle.getString("variety");
        age         =   bundle.getString("age");
        vet         =   bundle.getString("vet");
        lastmilkhistory=bundle.getString("milkhistory");
        description =   bundle.getString("description");
        preg_status =   bundle.getString("preg_status");
        latitude    =   String.valueOf(bundle.getDouble("lat"));
        longitude   =   String.valueOf(bundle.getDouble("longi"));
        city= "";



        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
       // image4 = (ImageView)findViewById(R.id.image4);
        image5 = (ImageView)findViewById(R.id.image5);
       // image6 = (ImageView)findViewById(R.id.image6);

        card1 = (CardView)findViewById(R.id.card1);
        card2 = (CardView)findViewById(R.id.card2);
        card3 = (CardView)findViewById(R.id.card3);
       // card4 = (CardView)findViewById(R.id.card4);
        card5 = (CardView)findViewById(R.id.card5);
       // card6 = (CardView)findViewById(R.id.card6);

        textView1 =  (TextView) findViewById(R.id.textView1);
        textView2 =  (TextView)findViewById(R.id.textView2);
        textView3 =  (TextView)findViewById(R.id.textView3);
      //  textView4 = (TextView) findViewById(R.id.textView4);
        textView5 =  (TextView)findViewById(R.id.textView5);
     //   textView6 =  (TextView)findViewById(R.id.textView6);
        frame1     = 0;

        if(speciality.equals("Pregnant"))
        {
            textView5.setText(getResources().getString(R.string.title_photo_certificate));
        }
        else
        {
            textView5.setText(getResources().getString(R.string.title_photo_seller));
        }

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frame1 = 1;
                selectImage();
                //     textView1.setVisibility(View.GONE);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frame1 = 2;
                selectImage();
                // textView2.setVisibility(View.GONE);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frame1 = 3;
                selectImage();
                //        textView3.setVisibility(View.GONE);
            }
        });

       /* card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frame1 = 4;
                selectImage();
                // textView4.setVisibility(View.GONE);
            }
        }); */

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frame1 = 5;
                selectImage();
                //  textView5.setVisibility(View.GONE);
            }
        });
        /*
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frame1 = 6;
                openVideoGallery();
            }
        });

         */



        postadd = findViewById(R.id.postadd);
        postadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPath1.equals(""))
                {
                    showAlert("Please select image1");
                }
                else if(mediaPath2.equals(""))
                {
                    showAlert("Please select image2");
                }
                else if(mediaPath3.equals(""))
                {
                    showAlert("Please select image3");
                }
               /* else if(mediaPath4.equals(""))
                {
                    showAlert("Please select image4");
                }*/
                else if(mediaPath5.equals(""))
                {
                    showAlert("Please select image5");
                }
              /*  else if(mediaPath6.equals(""))
                {
                    showAlert("Please select Video");
                } */
                else {
                    postAdvertisement();
                }
            }
        });

        //    progressBar = (ProgressBar) findViewById(R.id.progressBar);
        requestStoragePermission();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                else {
                    //code for deny
                }
            case CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Now user should be able to use camera
                }
                else
                {
                    // Your app will not have this permission. Turn off all functions
                    // that require this permission or it will force close like your
                    // original question
                }
                break;
        }
    }
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showSettingsDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhotosActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                openSettings();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postAdvertisement()
    {
        if (BaseController.isNetworkAvailable(getApplicationContext()))
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Map is used to multipart the file using okhttp3.RequestBody
            File file1 = new File(mediaPath1);
            File file2 = new File(mediaPath2);
            File file3 = new File(mediaPath3);
          //  File file4 = new File(mediaPath4);
            File file5 = new File(mediaPath5);
          //  File file6 = new File(mediaPath6);

            // Parsing any Media type file
            RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file1);
            RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file2);
            RequestBody requestBody3 = RequestBody.create(MediaType.parse("*/*"), file3);
        //    RequestBody requestBody4 = RequestBody.create(MediaType.parse("*/*"), file4);
            RequestBody requestBody5 = RequestBody.create(MediaType.parse("*/*"), file5);
          //  RequestBody requestBody6 = RequestBody.create(MediaType.parse("*/*"), file6);


            RequestBody appUserBody = RequestBody.create(MediaType.parse("text/plain"), app_user_id);
            RequestBody profileidBody = RequestBody.create(MediaType.parse("text/plain"), profileid);
            RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);
            RequestBody specialityBody = RequestBody.create(MediaType.parse("text/plain"), speciality);
            RequestBody varietyBody = RequestBody.create(MediaType.parse("text/plain"), variety);
            RequestBody ageBody = RequestBody.create(MediaType.parse("text/plain"), age);
            Log.e("upllaoodsdasd" , "postAdvertisement: "+age );
            RequestBody vetBody = RequestBody.create(MediaType.parse("text/plain"), vet);
            RequestBody lastmilkhistoryBody = RequestBody.create(MediaType.parse("text/plain"), lastmilkhistory);
            RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody preg_statusBody = RequestBody.create(MediaType.parse("text/plain"), preg_status);
            RequestBody latitudeBody = RequestBody.create(MediaType.parse("text/plain"), latitude);
            RequestBody longitudeBody = RequestBody.create(MediaType.parse("text/plain"), longitude);
            RequestBody cityBody = RequestBody.create(MediaType.parse("text/plain"), city);

            MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody1);
            MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestBody2);
            MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestBody3);
         //   MultipartBody.Part fileToUpload4 = MultipartBody.Part.createFormData("file4", file4.getName(), requestBody4);
            MultipartBody.Part fileToUpload5 = MultipartBody.Part.createFormData("file5", file5.getName(), requestBody5);
        //    MultipartBody.Part fileToUpload6 = MultipartBody.Part.createFormData("file6", file6.getName(), requestBody6);
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
            //Call<ServerResponse> call = service.uploadMulFile(fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload4, fileToUpload5, fileToUpload6, appUserBody, profileidBody, categoryBody, specialityBody, varietyBody, ageBody, vetBody, lastmilkhistoryBody, descriptionBody,preg_statusBody, latitudeBody, longitudeBody, cityBody);
            Call<ServerResponse> call = service.uploadMulFile(fileToUpload1, fileToUpload2, fileToUpload3, fileToUpload5, appUserBody, profileidBody, categoryBody, specialityBody, varietyBody, ageBody, vetBody, lastmilkhistoryBody, descriptionBody,preg_statusBody, latitudeBody, longitudeBody, cityBody);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    ServerResponse serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.getSuccess()) {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent target = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(target);
                        } else {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        assert serverResponse != null;
                        Log.v("Response", serverResponse.toString());
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Connect to network and refresh", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhotosActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(UploadPhotosActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        fromCamera();
//                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        fromGallary();
//                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void fromGallary() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);

    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        //  startActivityForResult(intent, REQUEST_CAMERA);
        try {
            captureImage();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_REQUEST_CODE);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void captureImage(){
        if( ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            }
            else {
                // Open your camera here.
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("requestCode", "onActivityResult: "+requestCode );

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
//                onSelectFromGalleryResult(data);
                resultUri = data.getData();
                finalpath_org = getRealPathFromURI(resultUri);
                Log.d("TAG", "resultUri  " + finalpath_org);
                cropImage(resultUri);
            }
            else if (requestCode == CAMERA_REQUEST_CODE) {
                resultUri = Uri.fromFile(new File(mCurrentPhotoPath));
                finalpath_org = resultUri.getPath();
                Log.d("TAG", "resultUri  " + finalpath_org);
                cropImage(resultUri);
//                onCaptureImageResult(data);
            }
            else if (requestCode == PICK_IMAGE_VIDEO) {

                if(data.getData()!=null)
                {
                    Uri uri = data.getData();

                    try {
                        String path = Utils.getPath(getApplicationContext(), uri);
                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                        File originalFile = new File(FileUtils.getRealPath(this,uri));

                      //  mediaPath6 =originalFile.getAbsolutePath();
                        Log.d("path6",originalFile.getAbsolutePath());

                       // image6.setImageBitmap(bitmap);
                      //  textView6.setVisibility(View.GONE);

                        //saveImage(bitmap);
                         saveVideo(path);

                    }
                    catch(Exception e) {

                        Log.e("%%%%%%%%", ""+e);
                        Toast.makeText(getApplicationContext(),  "Error: "+e, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Failed to select video" , Toast.LENGTH_LONG).show();
                }
            }
            else if(requestCode == 4) {

                resultUri = UCrop.getOutput(data);

                finalpath_crop = resultUri.getPath();
                Log.e("finalpath_crop", "onActivityResult: "+finalpath_crop);
                onCaptureCropImageResult(finalpath_crop);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult(Intent data)
    {
        String str="";
        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        try {
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap,(int)(imageBitmap.getWidth()*0.5), (int)(imageBitmap.getHeight()*0.5), true);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
            String randString =   getSaltString();
            String directory = (UploadPhotosActivity.this).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+File.separator+"pictures"+File.separator;
            File f = new File(directory);

            if(!f.exists()) {

                f.mkdir();
            }
            String destinationPath = directory+"t"+randString+".jpg";
            File destination = new File(destinationPath);
            FileOutputStream fo;

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            selectedImage = Uri.fromFile(destination);
            imgFileName="selected";
            str= destination.getAbsolutePath();
            fo.close();

            Log.d("path122",destination.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace( );
        }
        //ivImage.setImageBitmap(thumbnail);


        if(frame1 == 1) {
            mediaPath1  =   str;
            image1.setImageBitmap(imageBitmap);
            textView1.setVisibility(View.GONE);

        }else if(frame1 == 2) {
            mediaPath2  =   str;
            image2.setImageBitmap(imageBitmap);
            textView2.setVisibility(View.GONE);

        }else if(frame1 == 3) {
            mediaPath3  =   str;
            image3.setImageBitmap(imageBitmap);
            textView3.setVisibility(View.GONE);

        }else if(frame1 == 5) {
            mediaPath5  =   str;
            image5.setImageBitmap(imageBitmap);
            textView5.setVisibility(View.GONE);

        }
        /*else if(frame1 == 4) {
            mediaPath4  =   str;
            image4.setImageBitmap(imageBitmap);
            textView4.setVisibility(View.GONE);

        }*/
    }

    private void onCaptureCropImageResult(String s)
    {
        String str="";
        Bitmap imageBitmap = BitmapFactory.decodeFile(s);
        try {
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap,(int)(imageBitmap.getWidth()*0.5), (int)(imageBitmap.getHeight()*0.5), true);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
            String randString =   getSaltString();
            String directory = (UploadPhotosActivity.this).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+File.separator+"pictures"+File.separator;
            File f = new File(directory);

            if(!f.exists()) {

                f.mkdir();
            }
            String destinationPath = directory+"t"+randString+".jpg";
            File destination = new File(destinationPath);
            FileOutputStream fo;

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            selectedImage = Uri.fromFile(destination);
            imgFileName="selected";
            str= destination.getAbsolutePath();
            fo.close();

            Log.d("path122",destination.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace( );
        }
        //ivImage.setImageBitmap(thumbnail);


        if(frame1 == 1) {
            mediaPath1  =   str;
            image1.setImageBitmap(imageBitmap);
            textView1.setVisibility(View.GONE);

        }else if(frame1 == 2) {
            mediaPath2  =   str;
            image2.setImageBitmap(imageBitmap);
            textView2.setVisibility(View.GONE);

        }else if(frame1 == 3) {
            mediaPath3  =   str;
            image3.setImageBitmap(imageBitmap);
            textView3.setVisibility(View.GONE);

        }else if(frame1 == 5) {
            mediaPath5  =   str;
            image5.setImageBitmap(imageBitmap);
            textView5.setVisibility(View.GONE);

        }
        /*else if(frame1 == 4) {
            mediaPath4  =   str;
            image4.setImageBitmap(imageBitmap);
            textView4.setVisibility(View.GONE);

        }*/
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap imageBitmap=null;
        String str1="";
        if (data != null)
        {
            try
            {
                imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                selectedImage = data.getData();
                //imgFileName="selected";
                Uri uri = data.getData();
                File originalFile = new File(FileUtils.getRealPath(this,uri));
                str1 =   originalFile.getAbsolutePath();
                Log.d("path",originalFile.getAbsolutePath());

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        // ivImage.setImageBitmap(bm);
        if(frame1 == 1) {
            mediaPath1  =   str1;
            image1.setImageBitmap(imageBitmap);
            textView1.setVisibility(View.GONE);

        }else if(frame1 == 2) {
            mediaPath2  =   str1;
            image2.setImageBitmap(imageBitmap);
            textView2.setVisibility(View.GONE);

        }else if(frame1 == 3) {
            mediaPath3  =   str1;
            image3.setImageBitmap(imageBitmap);
            textView3.setVisibility(View.GONE);

        }
        else if(frame1 == 5) {
            mediaPath5  =   str1;
            image5.setImageBitmap(imageBitmap);
            textView5.setVisibility(View.GONE);
        }

        /*else if(frame1 == 4) {
            mediaPath4  =   str1;
            image4.setImageBitmap(imageBitmap);
            textView4.setVisibility(View.GONE);

        }
        */

    }

    public void openVideoGallery()
    {
        Intent intent;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        }
        else
        {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        }
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,PICK_IMAGE_VIDEO);
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
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // Providing Thumbnail For Selected Image
    public Bitmap getThumbnailPathForLocalFile(Activity context, Uri fileUri) {
        long fileId = getFileId(context, fileUri);
        return MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                fileId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
    }

    // Getting Selected File ID
    public long getFileId(Activity context, Uri fileUri) {
        Cursor cursor = context.managedQuery(fileUri, mediaColumns, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            return cursor.getInt(columnIndex);
        }
        return 0;
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
    void saveVideo(String sourcepath)
    {
        if(sourcepath!=null) {

            String directory = (UploadPhotosActivity.this).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+File.separator+"pictures"+File.separator;

            File f = new File(directory);

            if(!f.exists()) {

                f.mkdir();
            }
            String randString =   getSaltString();
            String destinationPath = directory+"t"+randString+".mp4";
            File destinationFilename = new File(destinationPath);
Log.d("vedio",destinationPath);
           // mediaPath6 =destinationPath;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {
                bis = new BufferedInputStream(new FileInputStream(sourcepath));
                bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
                byte[] buf = new byte[1024];
                bis.read(buf);
                do {
                    bos.write(buf);
                } while(bis.read(buf) != -1);
            } catch (IOException e) {
                Log.e("ERROR", ""+e);
            } finally {
                try {
                    if (bis != null) bis.close();
                    if (bos != null) bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void fromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(UploadPhotosActivity.this, getPackageName() + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void cropImage(Uri imagePath) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageExtension = "." + MimeTypeMap.getFileExtensionFromUrl(finalpath_org);

        UCrop uCrop = UCrop.of(imagePath, Uri.fromFile(new File(getCacheDir(), timeStamp + imageExtension)));
        uCrop.withAspectRatio(1, 1);
        // uCrop.withMaxResultSize(250,250);
        UCrop.Options options = new UCrop.Options();

        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.white));
        options.setRootViewBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

        uCrop.withOptions(options);
        uCrop.start(context,
                4);


    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

}
