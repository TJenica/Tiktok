package com.hcl.tiktok.json;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
        // https://beiyou.bytedance.com/api/invoke/video/invoke/video
        @GET("api/invoke/video/invoke/video")
        Call<List<VideoResponse>> getVideos();
        @FormUrlEncoded
        @POST("user/register")
        Call<UserResponse> register(@Field("username")String userName,
                                    @Field("password")String password,
                                    @Field("repassword")String repassword);
    }


