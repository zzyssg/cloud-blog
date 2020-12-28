package com.zzy.cloudblogcomment.dao;

import com.zzy.cloudblogcomment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


/**
 * @author zzy
 */
public interface CommentMapper {
    /**
     * 新增评论
     * @param comment
     * @return
     */
    Integer insertComment11(Comment comment);

    /**
     * 向t_comment中间表中添加数据——已经拿到commentId
     * @param comment
     * @return
     */
    Integer insertCommentRelative(Comment comment);

    /**
     * 根据ID删除评论
     * @param commentId
     * @return
     */
    Integer deleteCommentById(Integer commentId);
}