package com.zzy.cloudblogblog.dto;

import com.zzy.cloudblogblog.entity.type.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author zzy
 * @Date 2020/12/2 17:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDTO {

    /**
     * 博客标识
     */
    private Integer blogId;

    /**
     * 博客名称
     */
    private String title;

    /**
     * 博客类型标识
     */
    private Integer typeId;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 博客首图
     */
    private String firstPicture;

    /**
     * 博客简介
     */
    private String description;

    /**
     * 是否可转载
     */
    private boolean sharement;

    /**
     * 是否推荐
     */
    private boolean recommend;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 博客类型
     */
    private Type type;


}
