package com.example.remainderalarm;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    private static Retrofit instance = null;
    private APIService apiService;

    public Retrofit() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder().baseUrl("http://192.168.0.189:3000").addConverterFactory(GsonConverterFactory.create()).build();

        this.apiService = retrofit.create(APIService.class);
    }

    public static synchronized Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit();
        }
        return new Retrofit();
    }

    public APIService getApiService() {
        return apiService;
    }
}
