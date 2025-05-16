package com.example.lab4.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // URL gốc của API trên server XAMPP của bạn
    // thì BASE_URL sẽ là "http://10.0.2.2/lab5_api/" (10.0.2.2 là localhost cho máy ảo Android)
    private static final String BASE_URL = "http://10.0.2.2/lab5_api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}