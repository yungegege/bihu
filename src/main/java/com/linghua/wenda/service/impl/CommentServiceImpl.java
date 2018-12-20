package com.linghua.wenda.service.impl;

import com.linghua.wenda.dao.CommentDao;
import com.linghua.wenda.model.Comment;
import com.linghua.wenda.service.CommentService;
import com.linghua.wenda.util.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveUtil sensitiveUtil;

    @Override
    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveUtil.filter(comment.getContent()));
        return commentDao.addComment(comment)>0?comment.getId():0;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentDao.selectCommentById(id);
    }

    @Override
    public List<Comment> getByEntity(int entityType, int entityId) {
        return commentDao.selectByEntity(entityType,entityId);
    }

    @Override
    public int getCommmentCount(int entityType, int entityId) {
        return commentDao.getCommmentCount(entityType,entityId);
    }

    @Override
    public boolean deleteComment(int commentId) {
        return commentDao.updateStatus(commentId,1)>0;
    }

    @Override
    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }
}
