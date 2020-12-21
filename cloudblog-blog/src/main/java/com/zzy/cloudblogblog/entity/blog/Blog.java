package com.zzy.cloudblogblog.entity.blog;

import com.zzy.cloudblogblog.entity.type.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

/**
 * @author zzy
 */
@Table(name = "t_blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {
    /**
     * 博客ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    @Column(name = "blog_id")
    private Integer blogId;

    /**
     * 博客名称
     */
    private String title;

    /**
     * 用户标识
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 博客类型标识
     */
    @Column(name = "type_id")
    private Integer typeId;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 博客详情首图
     */
    @Column(name = "first_picture")
    private String firstPicture;

    /**
     * 描述博客信息
     */
    private String description;

    /**
     * 1-原创、2-转载
     */
    private Boolean flag;

    /**
     * 浏览量
     */
    private Integer views;

    /**
     * 是否开启分享
     */
    private Boolean sharement;

    /**
     * 是否开启评论
     */
    private Boolean commentabled;

    /**
     * 发布状态：1-已发布、0-草稿
     */
    private Boolean published;

    /**
     * 是否推荐
     */
    private Boolean recommend;

    /**
     * 创作时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 博客类型
     */
    private Type type;

}