package com.zzy.cloudblogblog.dao;

import com.zzy.cloudblogblog.entity.Type;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zzy
 */
public interface TypeMapper extends Mapper<Type> {
    /**
     * 根据类型名字查询类型
     * @param typeName
     * @return
     */
    Type getTypeByName(String typeName);

    /**
     * 查询全部类型
     * @return
     */
    List<Type> listAllTypes();

    /**
     * 统计所有类型总数
     * @return
     */
    Integer countAllType();

    /**
     * 根据ID查询类型
     * @param typeId
     * @return
     */
    Type getTypeById(Integer typeId);
}