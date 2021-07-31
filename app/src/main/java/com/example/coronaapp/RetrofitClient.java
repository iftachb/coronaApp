package com.example.coronaapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private CommApi myApi;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(CommApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(CommApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public CommApi getMyApi() {
        return myApi;
    }
}
