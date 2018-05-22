package me.integrate.socialbank.comment;

import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Objects;

public class Comment {
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

    public Comment() {
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
        Comment comment = (Comment) o;
        return  id == comment.id &&
                Objects.equals(creatorEmail, comment.creatorEmail) &&
                Objects.equals(creatorEmail, comment.creatorEmail) &&
                Objects.equals(createdAt, comment.createdAt) &&
                Objects.equals(updatedAt, comment.updatedAt) &&
                Objects.equals(answerTo, comment.answerTo) &&
                Objects.equals(content, comment.content);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, creatorEmail, createdAt, updatedAt, answerTo, content);
    }
}
