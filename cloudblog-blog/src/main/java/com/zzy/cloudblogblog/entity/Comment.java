package com.zzy.cloudblogblog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * @author zzy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "t_comment")
public class Comment {
    /**
     * 评论唯一标识
     */
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(generator = "JDBC")
    private Integer commentId;

    /**
     * 博客唯一标识
     */
    private Integer blogId;

    /**
     * 用户唯一标识
     */
    private Integer userId;

    /**
     * 评论的用户
     */
    private User user;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private List<Comment> replyComments;

    /**
     * 父级评论的用户
     */
    private User parentCommentUser;

    /**
     * 父评论ID
     */
    @Column(name = "parent_comment_id")
    private Integer parentCommentId;
}