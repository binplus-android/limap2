package com.limap.Interface;


import com.limap.Model.ServerResponse;
import com.limap.Model.SetterAllPostDetails;
import com.limap.Model.SetterDoctorList;
import com.limap.Model.SetterEditProfile;
import com.limap.Model.SetterLogin;
import com.limap.Model.SetterResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by dell on 25-Dec-18.
 */

public interface APIService {

    //the signin call
    @FormUrlEncoded
    @POST("api_get_otp.php")
    Call<SetterLogin> userLogin(
            @Field("mob_no") String mob_no
    );

    @FormUrlEncoded
    @POST("api_verify_otp.php")
    Call<SetterLogin> verifyOTP(
            @Field("otp") String otp,
            @Field("tbl_app_user_id") String user_id
    );

    @FormUrlEncoded
    @POST("api_get_user_profile.php")
    Call<SetterEditProfile> userProfile(
            @Field("tbl_app_user_id") String user_id
    );


    @FormUrlEncoded
    @POST("api_set_profile_details.php")
    Call<SetterResponse> saveUserProfile(
            @Field("app_user_id") String app_user_id,
            @Field("profileid") String profileid,
            @Field("name") String name,
            @Field("address") String address,
            @Field("city") String city,
            @Field("taluka") String taluka,
            @Field("district") String district,
            @Field("state") String state,
            @Field("isdoctor") boolean isdoctor,
            @Field("speciality") String speciality,
            @Field("experience") String experience,
            @Field("servicelocation") String servicelocation,
            @Field("servingVillage") String servingVillage,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude
    );

    // @Part MultipartBody.Part file4,// @Part MultipartBody.Part file6,

    @Multipart
    @POST("api_set_add_post.php")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,
                                       @Part MultipartBody.Part file2,
                                       @Part MultipartBody.Part file3,
                                       @Part MultipartBody.Part file5,
                                       @Part("app_user_id") RequestBody desc,
                                       @Part("profileid") RequestBody profileidBody,
                                       @Part("category") RequestBody categoryBody,
                                       @Part("speciality") RequestBody specialityBody,
                                       @Part("variety") RequestBody varietyBody,
                                       @Part("age") RequestBody ageBody,
                                       @Part("vet") RequestBody vetBody,
                                       @Part("lastmilkhistory") RequestBody lastmilkhistoryBody,
                                       @Part("description") RequestBody descriptionBody,
                                       @Part("preg_statusBody") RequestBody preg_statusBody,
                                       @Part("latitude") RequestBody latitudeBody,
                                       @Part("longitude") RequestBody longitudeBody,
                                       @Part("city") RequestBody cityBody

    );


    @FormUrlEncoded
    @POST("api_get_all_post.php")
    Call<List<SetterAllPostDetails>> homePostAll(
            @Field("lat") double lat,
            @Field("longi") double longi
    );


    @FormUrlEncoded
    @POST("get_post_by_category.php")
    Call<List<SetterAllPostDetails>> categoryPostAll(
            @Field("lat") double lat,
            @Field("longi") double longi,
            @Field("category") String category
    );

    @FormUrlEncoded
    @POST("api_get_my_post.php")
    Call<List<SetterAllPostDetails>> myPostDetails(
            @Field("lat") double lat,
            @Field("longi") double longi,
            @Field("app_user_id") String app_user_id
    );

    @FormUrlEncoded
    @POST("api_get_my_favourite.php")
    Call<List<SetterAllPostDetails>> myFavourite(
            @Field("app_user_id") String app_user_id,
            @Field("lat") double lat,
            @Field("longi") double longi
    );

    @FormUrlEncoded
    @POST("api_get_doctor_list.php")
    Call<List<SetterDoctorList>> doctorList(
            @Field("lat") double lat,
            @Field("longi") double longi
    );


    @FormUrlEncoded
    @POST("api_set_favourite.php")
    Call<SetterResponse> setFavourite(
            @Field("post_id") String post_id,
            @Field("app_user_id") String app_user_id
    );

    @FormUrlEncoded
    @POST("api_get_favourite.php")
    Call<SetterResponse> getFavourite(
            @Field("post_id") String post_id,
            @Field("app_user_id") String app_user_id
    );

    @FormUrlEncoded
    @POST("api_get_search_details.php")
    Call<List<SetterAllPostDetails>> searchDetailsView(
            @Field("category") String category,
            @Field("speciality") String speciality,
            @Field("variety") String variety
    );

}
