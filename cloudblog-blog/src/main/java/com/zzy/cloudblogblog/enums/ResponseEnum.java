package com.zzy.cloudblogblog.enums;

import lombok.AllArgsConstructor;
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
    BLOG_IS_NULL(103,"博客为空"),
    BLOG_CONDITION_IS_NULL(104,"条件查询的博客为空"),
    BLOG_OFTYPE_IS_NULL(106,"此类型下的博客为空"),
    BLOG_ONID_IS_NULL(105,"此ID博客为空"),
    TYPE_IS_NULL(102,"博客类型为空"),
    TYPE_ONID_IS_NULL(107,"此ID类型为空"),
    TYPE_ALREADY_EXISTS(108,"此类型已存在"),


    /**
     * 3xx为前端相关的错误
     */
    BLOG_FROM_FRONT_IS_NULL(301,"前端传来的博客对象为空"),
    TYPE_PARAM_ILLEGAL(302,"新增(博客)类型参数非法"),

    /**
     * 7xx为数据库相关的错误
     */
    INSERT_BLOG_ERROR(701,"新增博客错误"),
    UPDATE_BLOG_ERROR(702,"更新博客错误"),
    DELETE_BLOG_ERROR(703,"删除博客错误"),
    INSERT_BLOG_RELATIVE_ERROR(704,"新增博客关联表记录错误"),
    INSERT_TYPE_ERROR(705,"新增(博客)类型错误"),
    DELETE_TYPE_PARAM_ERROR(706,"删除(博客)类型参数非法"),
    DELETE_TYPE_ERROR(707,"删除(博客)类型错误");

    /**
     * 8xx为登录相关错误
     */

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
