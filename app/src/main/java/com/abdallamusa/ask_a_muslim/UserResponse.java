// UserResponse.java
package com.abdallamusa.ask_a_muslim;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("fullName")
    public String fullName;

    @SerializedName("email")
    public String email;


    public UserResponse() {}

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }
}
