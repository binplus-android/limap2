package com.limap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetterDoctorList {
    @SerializedName("tbl_add_user_id")
    @Expose
    private String tbl_add_user_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("speciality")
    @Expose
    private String speciality;

    @SerializedName("serving_village")
    @Expose
    private String serving_village;

    public String getTbl_add_user_id() {
        return tbl_add_user_id;
    }

    public void setTbl_add_user_id(String tbl_add_user_id) {
        this.tbl_add_user_id = tbl_add_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getServing_village() {
        return serving_village;
    }

    public void setServing_village(String serving_village) {
        this.serving_village = serving_village;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
