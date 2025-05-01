// CourseSummary.java
package com.abdallamusa.ask_a_muslim;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CourseSummary {
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("instructorName")
    public String instructor;

    @SerializedName("thumbnailUrl")
    public String thumbnailUrl;

    // we’ll ignore lessons+others here…
}
