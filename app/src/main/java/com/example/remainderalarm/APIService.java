package com.example.remainderalarm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @GET("posts")
    Call<List<Post>> getData();

    @POST("users")
    Call<Post> createData(@Body List<Post> postData);
}
