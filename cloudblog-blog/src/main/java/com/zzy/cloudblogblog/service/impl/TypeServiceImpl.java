package com.zzy.cloudblogblog.service.impl;

import com.zzy.cloudblogblog.dao.type.TypeMapper;
import com.zzy.cloudblogblog.entity.type.Type;
import com.zzy.cloudblogblog.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/21 21:12
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TypeServiceImpl implements TypeService {

    private final TypeMapper typeMapper;

    /**
     * 查询所有博客类型
     * @return
     */
    @Override
    public List<Type> listAllTypes() {
        return typeMapper.selectAll();
    }

    /**
     * 根据主键查询博客类型
     *
     * @param typeId
     * @return
     */
    @Override
    public Type getTypeById(Long typeId) {
        return typeMapper.selectByPrimaryKey(typeId);
    }
}
