package com.abdallamusa.ask_a_muslim;




import com.google.gson.annotations.SerializedName;

public class RegisterResponse {


    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    public String getMessage() { return message; }

    public String getToken() {
return token;
    }
}
