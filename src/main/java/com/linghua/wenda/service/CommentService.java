package com.linghua.wenda.service;

import com.linghua.wenda.model.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService {
    int addComment(Comment comment);

    Comment getCommentById(int id);

    List<Comment> getByEntity(int entityType, int entityId);

    int getCommmentCount(int entityType, int entityId);

    boolean deleteComment(int commentId);

    int getUserCommentCount(int userId);
}
