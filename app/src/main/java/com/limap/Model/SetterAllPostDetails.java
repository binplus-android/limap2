package com.limap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SetterAllPostDetails implements Serializable {

    @SerializedName("tbl_add_post_data_id")
    @Expose
    private String tbl_add_post_data_id;

    @SerializedName("app_user_id")
    @Expose
    private String app_user_id;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("speciality")
    @Expose
    private String speciality;

    @SerializedName("variety")
    @Expose
    private String variety;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("vet")
    @Expose
    private String vet;

    @SerializedName("lastmilkhistory")
    @Expose
    private String lastmilkhistory;

    @SerializedName("image1")
    @Expose
    private String image1;

    @SerializedName("image2")
    @Expose
    private String image2;

    @SerializedName("image3")
    @Expose
    private String image3;

   /* @SerializedName("image4")
    @Expose
    private String image4;

    */
    @SerializedName("image5")
    @Expose
    private String image5;

  /*  @SerializedName("video1")
    @Expose
    private String video1; */

    public String getTbl_add_post_data_id() {
        return tbl_add_post_data_id;
    }

    public void setTbl_add_post_data_id(String tbl_add_post_data_id) {
        this.tbl_add_post_data_id = tbl_add_post_data_id;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVet() {
        return vet;
    }

    public void setVet(String vet) {
        this.vet = vet;
    }

    public String getLastmilkhistory() {
        return lastmilkhistory;
    }

    public void setLastmilkhistory(String lastmilkhistory) {
        this.lastmilkhistory = lastmilkhistory;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

  /*  public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

   */

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    /*   public String getVideo1() {
        return video1;
    }

    public void setVideo1(String video1) {
        this.video1 = video1;
    }  */

    @Override
    public String toString() {
        return "SetterAllPostDetails{" +
                "tbl_add_post_data_id='" + tbl_add_post_data_id + '\'' +
                ", app_user_id='" + app_user_id + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", distance='" + distance + '\'' +
                ", address='" + address + '\'' +
                ", speciality='" + speciality + '\'' +
                ", variety='" + variety + '\'' +
                ", age='" + age + '\'' +
                ", vet='" + vet + '\'' +
                ", lastmilkhistory='" + lastmilkhistory + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image5='" + image5 + '\'' +
                '}';
    }
}
