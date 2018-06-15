package me.integrate.socialbank.comment;

import java.util.List;

public interface CommentRepository {
    Comment getCommentById(int id);

    void updateContent(int id, String content);

    Comment saveComment(Comment comment);

    List<Comment> getAllComments(int event_id);

    void deleteComment(int id);
}
