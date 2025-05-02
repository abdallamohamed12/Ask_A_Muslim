// ProgressResponse.java
package com.abdallamusa.ask_a_muslim;

import com.google.gson.annotations.SerializedName;

// matches: { "totalLessonsCompleted": 5 }
public class ProgressResponse {
    @SerializedName("totalLessonsCompleted")
    public int totalLessonsCompleted;
}
