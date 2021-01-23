package com.limap.Model;

import com.google.gson.annotations.SerializedName;

public class SetterEditProfile {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("city")
    private String city;

    @SerializedName("taluka")
    private String taluka;

    @SerializedName("district")
    private String district;

    @SerializedName("state")
    private String state;

    @SerializedName("bisdoctor")
    private String bisdoctor;

    @SerializedName("speciality")
    private String speciality;

    @SerializedName("experience")
    private String experience;

    @SerializedName("serviceLoc")
    private String serviceLoc;

    @SerializedName("serviceVillage")
    private String serviceVillage;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTaluka() {
        return taluka;
    }

    public void setTaluka(String taluka) {
        this.taluka = taluka;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBisdoctor() {
        return bisdoctor;
    }

    public void setBisdoctor(String bisdoctor) {
        this.bisdoctor = bisdoctor;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getServiceLoc() {
        return serviceLoc;
    }

    public void setServiceLoc(String serviceLoc) {
        this.serviceLoc = serviceLoc;
    }

    public String getServiceVillage() {
        return serviceVillage;
    }

    public void setServiceVillage(String serviceVillage) {
        this.serviceVillage = serviceVillage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
