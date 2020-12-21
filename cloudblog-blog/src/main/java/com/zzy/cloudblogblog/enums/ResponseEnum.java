package com.zzy.cloudblogblog.enums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzy
 * @Date 2020/12/21 11:07
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseEnum {
    /**
     * 成功时返回
     */
    RESPONSE_SUCCESS(666, "success"),

    /**
     * 失败时返回
     */
    RESPONSE_FAILED(101, "failed"),
    TYPE_IS_NULL(102,"博客类型为空"),
    BLOG_IS_NULL(103,"博客为空"),
    BLOG_CONDITION_IS_NULL(105,"条件查询的博客为空"),
    BLOG_OFTYPE_IS_NULL(106,"此类型下的博客为空"),
    BLOG_ONID_IS_NULL(105,"此ID博客为空"),

    //3开头为前端相关的错误
    BLOG_FROM_FRONT_IS_NULL(301,"前端传来的博客对象为空"),

    //7开头为数据库相关的错误
    INSERT_BLOG_ERROR(701,"新增博客错误"),
    UPDATE_BLOG_ERROR(702,"更新博客错误"),
    DELETE_BLOG_ERROR(703,"删除博客错误");

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
