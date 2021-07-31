package com.example.coronaapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommApi {

    static final String BASE_URL = "https://api.covid19api.com";

    // TODO: move to enum
    public static final String CONFIRMED = "confirmed";
    public static final String RECOVERED = "recovered";
    public static final String DEATHS = "deaths";

    //@GET("country/south-africa/status/deaths?from=2020-03-01T00:00:00Z&to=2020-04-02T00:00:00Z")
    @GET("country/{country}/status/{status}")
    Call<List<Results>> getByCountry(@Path("country") String country, @Path("status") String status,
                                     @Query("from") String from, @Query("to") String to);
}
