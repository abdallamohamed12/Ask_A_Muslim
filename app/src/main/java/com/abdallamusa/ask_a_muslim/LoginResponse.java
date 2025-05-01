package com.abdallamusa.ask_a_muslim;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public String getToken()     { return token; }
    public boolean isSuccess()   { return success; }
    public String getMessage()   { return message; }
}
