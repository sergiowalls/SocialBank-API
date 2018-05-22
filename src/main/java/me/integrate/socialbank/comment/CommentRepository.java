package me.integrate.socialbank.comment;

import java.util.List;

public interface CommentRepository {
    Comment getCommentById(int event_id, int id);

    void updateContent(int event_id, int id, String content);

    Comment saveComment(Comment comment);

    List<Comment> getAllComments(int event_id);

    void deleteComment(int event_id, int id);
}
