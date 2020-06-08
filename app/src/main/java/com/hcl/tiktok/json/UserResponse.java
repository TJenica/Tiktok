package com.hcl.tiktok.json;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("errorMsg")
    public String errorMsg;
    @SerializedName("data")
    public User user;
    public class User {
        @SerializedName("nickname")
        public String nickname;
    }
}
