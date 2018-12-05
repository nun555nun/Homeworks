package com.example.homeworks.model;

public class HomeworkItem {
    public final long _id;
    public final String  title;
    public final String  subject;
    public final String  start;
    public final String  deadline;
    public final String details;
    public final String image;


    public HomeworkItem(long id, String title, String subject, String start, String deadline, String details, String image) {
        _id = id;
        this.title = title;
        this.subject = subject;
        this.start = start;
        this.deadline = deadline;
        this.details = details;
        this.image = image;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

}
