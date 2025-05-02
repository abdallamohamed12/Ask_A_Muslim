package com.abdallamusa.ask_a_muslim;

public class Course {
    public final String title;
    public final String instructor;

    public final String id;

    public final String level;

    public final String category;


    public final String thumbnailUrl;




    public Course(String id ,String title,String thumbnailUrl,String category,String level, String instructor) {
        this.id = id ;
this.thumbnailUrl = thumbnailUrl;

        this.title = title;
        this.instructor = instructor;

        this.level = level;
        this.category = category;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getInstructor() {
        return instructor;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getLevel() {
        return level;
    }

    public String getCategory() {
        return category;
    }



}
