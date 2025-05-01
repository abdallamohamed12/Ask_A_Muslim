package com.abdallamusa.ask_a_muslim;

// CoursesApi.java

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CoursesApi {
    @GET("Course/GetCourseByCategoryName/ByName/{cat}")
    Call<List<CourseSummary>> getByCategory(@Path("cat") String category);

    @GET("Course/GetCourses")
    Call<List<CourseSummary>> getAll();
}
