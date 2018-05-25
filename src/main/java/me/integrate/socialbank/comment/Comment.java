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
    private Integer replyTo;
    private String content;

    public Comment() {
        createdAt = updatedAt = new Date();
    }

    public int getId() {
        return id;
    }


    public Integer getReplyTo() {
        return replyTo;
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

    public void setReplyTo(Integer replyTo) {
        this.replyTo = replyTo;
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
                Objects.equals(updatedAt, comment.updatedAt) && Objects.equals(replyTo, comment.replyTo) &&
                Objects.equals(content, comment.content);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, eventId, creatorEmail, createdAt, updatedAt, replyTo, content);
    }
}
