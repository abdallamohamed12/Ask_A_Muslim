package com.abdallamusa.ask_a_muslim;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthApi {
    @POST("Identity/Login/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("Identity/Logout/logout")
    Call<Void> logout(@retrofit2.http.Header("Authorization") String bearerToken);

    @PUT("Identity/UpdateEmail/update-email")
    Call<Void> updateEmail(
            @Header("Authorization") String bearer,
            @Body UpdateEmailRequest body
    );

    @PUT("Identity/UpdatePassword/update-password")
    Call<Void> updatePassword(
            @Header("Authorization") String bearer,
            @Body UpdatePasswordRequest body
    );

    @PUT("Identity/UpdateName/update-name")
    Call<Void> updateName(
            @Header("Authorization") String bearer,
            @Body UpdateNameRequest body
    );

    @DELETE("Identity/DeleteUser/delete-user/{id}")
    Call<Void> deleteUser(

               @Path("id") String userId
    );

    public static class UpdateEmailRequest {
        public String userId, newEmail;

        public UpdateEmailRequest(String u, String e) {
            userId = u;
            newEmail = e;
        }
    }

    public static class UpdatePasswordRequest {
        public String userId, oldPassword, newPassword;

        public UpdatePasswordRequest(String u, String o, String n) {
            userId = u;
            oldPassword = o;
            newPassword = n;
        }
    }

    public static class UpdateNameRequest {
        public String userId, firstName, lastName;

        public UpdateNameRequest(String u, String f, String l) {
            userId = u;
            firstName = f;
            lastName = l;
        }

    }
}
