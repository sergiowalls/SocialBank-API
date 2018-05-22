package me.integrate.socialbank.post;

import org.springframework.lang.Nullable;

import java.util.Date;

public class Post {
    private int id;
    private Date created_at;
    private Date updated_at;
    @Nullable
    private int answer_to;
    private String content;

    public Post(int id, int answer_to, String content) {
        this.id = id;
        this.answer_to = answer_to;
        this.content = content;
        created_at = new Date();
        updated_at = new Date();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        updated_at = new Date();
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public int getAnswer_to() {
        return answer_to;
    }
}
