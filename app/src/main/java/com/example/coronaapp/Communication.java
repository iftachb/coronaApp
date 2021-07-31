package com.example.coronaapp;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Communication {
    public void getByCountry(String Country, String from, String to) {
        Call<List<Results>> call = RetrofitClient.getInstance().getMyApi().getByCountry(Country, CommApi.CONFIRMED,
                from, to);
        parseCall(call);

        call = RetrofitClient.getInstance().getMyApi().getByCountry(Country, CommApi.RECOVERED,
                from, to);
        parseCall(call);

        call = RetrofitClient.getInstance().getMyApi().getByCountry(Country, CommApi.DEATHS,
                from, to);
        parseCall(call);

    }
    private void parseCall(Call<List<Results>> call) {
        call.enqueue(new Callback<List<Results>>() {
            @Override
            public void onResponse(Call<List<Results>> call, Response<List<Results>> response) {
                List<Results> resultsList = response.body();
                int sum = 0;
                if (resultsList != null && !resultsList.isEmpty()) {
                    for(Results results : resultsList) {
                        sum += results.getCases();
                    }
                    MainManager.getInstance().reportCovid(resultsList.get(0).getStatus(), sum);
                    Log.i("comm", ""+ resultsList.size());
                }
                else {
                    Log.i("comm", "return no answers");
                    //TODO MainManager.getInstance().noResult("return no answers");
                }

            }

            @Override
            public void onFailure(Call<List<Results>> call, Throwable t) {
                Log.e("Communication", "An error has accrued");
            }

        });

    }

}
