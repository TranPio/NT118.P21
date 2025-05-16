package com.example.lab4.api;

import com.example.lab4.model.AuthResponse;
import com.example.lab4.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // Endpoint cho việc đăng ký người dùng
    @POST("register.php")
    Call<AuthResponse> registerUser(@Body User userRequest);

    // Endpoint cho việc đăng nhập người dùng
    @POST("login.php")
    Call<AuthResponse> loginUser(@Body User userRequest);

}