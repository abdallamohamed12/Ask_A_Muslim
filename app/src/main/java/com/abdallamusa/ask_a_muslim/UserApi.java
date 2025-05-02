package com.abdallamusa.ask_a_muslim;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    /**
     * GET https://ask-a-muslim.runasp.net/api/Identity/GetUserById/Users/{userId}
     * Returns a UserResponse containing only fullName & email.
     */
    @GET("Identity/GetUserById/Users/{userId}")
    Call<UserResponse> getUserById(@Path("userId") String userId);
}

