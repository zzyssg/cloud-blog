package com.zzy.cloudblogblog.service;

import com.zzy.cloudblogblog.entity.Type;

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

    /**
     * 新增类型
     * @param type
     * @return
     */
    Boolean insertType(Type type);

    /**
     * 删除类型前确认此类型下的博客是否已经没有博客了
     * 若还存在博客——删除失败，false
     * 若不存在博客——删除成功，true
     * @param typeById
     * @return
     */
    Boolean deleteType(Type typeById);
}
