package com.abdallamusa.ask_a_muslim;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
// put update data
    // delete
//Post creation of data
    @FormUrlEncoded
    @POST("Identity/Register/register")
    Call<RegisterResponse> registerUser(
            @Field("FirstName")   String firstName,
            @Field("LastName")    String lastName,
            @Field("Email")       String email,
            @Field("Password")    String password,
            @Field("PhoneNumber") String phoneNumber,
            @Field("Role")        String role,
            @Field("ProfilePicture") String profilePicture // send empty string if none
    );
}
