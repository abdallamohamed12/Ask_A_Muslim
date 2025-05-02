package com.abdallamusa.ask_a_muslim;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EnrollmentApi {
    // GET all enrolled
    @GET("Enrollment/GetEnrolledCoursesByStudent")
    Call<List<CourseSummary>> getEnrolledCourses(
            @Query("studentId") String studentId


    );




    // Enroll (body JSON)
    @POST("Enrollment/Enroll")
    Call<Void> enrollCourse(
            @Body EnrollmentRequest request
    );

    // Un-enroll (query parameters)
    @DELETE("Enrollment/UnEnroll")
    Call<Void> unenrollCourse(
            @Query("studentId") String studentId,
            @Query("courseId")  String courseId
    );
}

// JSON body for enroll only:
class EnrollmentRequest {
    private final String studentId;
    private final String courseId;
    public EnrollmentRequest(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId  = courseId;
    }
    public String getStudentId() { return studentId; }
    public String getCourseId()  { return courseId;  }



}
