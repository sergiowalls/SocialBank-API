package me.integrate.socialbank.post;

import java.util.Date;

public class Post {
    private int id;
    private int eventId;
    private String creatorEmail;
    private Date createdAt;
    private Date updatedAt;
    private int answerTo;
    private String content;

    private void update() {
        this.updatedAt = new Date();
    }

    public Post() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    public int getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getAnswerTo() {
        return answerTo;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }


    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public void setAnswerTo(int answerTo) {
        this.answerTo = answerTo;
    }

    public void setContent(String content) {
        this.content = content;
        update();
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
