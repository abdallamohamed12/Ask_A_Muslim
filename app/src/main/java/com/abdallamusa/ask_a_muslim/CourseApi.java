package com.abdallamusa.ask_a_muslim;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CourseApi {
    @GET("Course/GetCourseById/{id}")
    Call<CourseDetail> getCourseById(@Path("id") String courseId);
}
