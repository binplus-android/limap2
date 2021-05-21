package com.limap.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 08-Feb-19.
 */

public class SetterLogin {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("app_user_id")
    private String app_user_id;

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String tbl_user_id) {
        this.app_user_id = tbl_user_id;
    }

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

    @Override
    public String toString() {
        return "SetterLogin{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", app_user_id='" + app_user_id + '\'' +
                '}';
    }
}
