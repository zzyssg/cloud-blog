package com.zzy.cloudblogblog.service;

import com.zzy.cloudblogblog.entity.type.Type;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/21 21:10
 */
public interface TypeService {

    /**
     * 查询所有博客类型
     * @return
     */
    List<Type> listAllTypes();

    /**
     * 根据主键查询博客类型
     * @param typeId
     * @return
     */
    Type getTypeById(Long typeId);
}
