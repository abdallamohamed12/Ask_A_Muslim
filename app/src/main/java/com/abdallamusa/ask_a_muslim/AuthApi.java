package com.abdallamusa.ask_a_muslim;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("Identity/Login/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
