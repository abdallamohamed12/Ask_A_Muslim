package com.abdallamusa.ask_a_muslim;

import java.util.List;

public class CourseDetail {
    public String id;
    public String title;
    public String description;
    public String thumbnailUrl;
    public String category;
    public String level;
    public String instructorID;
    public String instructorName;
    public List<Lesson> lessons;
    public int numberOfLessons;
    public int numberOfStudentsEnrolled;

    public static class Lesson {
        public String id;
        public String title;
        public String content;
        public String thumbnailUrl;
        public String videoUrl;
        public String externalVideoUrl;
        public String courseId;
    }
}
