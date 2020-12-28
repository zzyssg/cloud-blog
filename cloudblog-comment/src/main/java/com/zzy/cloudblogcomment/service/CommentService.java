package com.zzy.cloudblogcomment.service;

import com.zzy.cloudblogcomment.entity.Comment;

/**
 * @author zzy
 * @Date 2020/12/23 15:00
 */
public interface CommentService {


    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    Boolean addComment(Comment comment);

    /**
     * 根据iD删除评论
     * @param commentId
     * @return
     */
    Integer deleteCommentById(Integer commentId);
}
