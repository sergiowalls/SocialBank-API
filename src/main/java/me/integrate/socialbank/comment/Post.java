package me.integrate.socialbank.comment;

import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Objects;

public class Post {
    private int id;
    private int eventId;
    private String creatorEmail;
    private Date createdAt;
    private Date updatedAt;
    @Nullable
    private Integer answerTo;
    private String content;

    public void update() {
        this.updatedAt = new Date();
    }

    public Post() {
        createdAt = updatedAt = new Date();
    }

    public int getId() {
        return id;
    }


    public Integer getAnswerTo() {
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

    public void setAnswerTo(Integer answerTo) {
        this.answerTo = answerTo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return  id == post.id &&
                Objects.equals(creatorEmail, post.creatorEmail) &&
                Objects.equals(creatorEmail, post.creatorEmail) &&
                Objects.equals(createdAt, post.createdAt) &&
                Objects.equals(updatedAt, post.updatedAt) &&
                Objects.equals(answerTo, post.answerTo) &&
                Objects.equals(content, post.content);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, creatorEmail, createdAt, updatedAt, answerTo, content);
    }
}
