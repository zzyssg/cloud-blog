package com.zzy.cloudblogblog.service.impl;

import com.zzy.cloudblogblog.dao.TypeMapper;
import com.zzy.cloudblogblog.entity.Blog;
import com.zzy.cloudblogblog.entity.Type;
import com.zzy.cloudblogblog.enums.ResponseEnum;
import com.zzy.cloudblogblog.exception.CommonException;
import com.zzy.cloudblogblog.service.BlogService;
import com.zzy.cloudblogblog.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/21 21:12
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TypeServiceImpl implements TypeService {

    private final TypeMapper typeMapper;
    private final BlogService blogService;

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

    /**
     * 新增类型
     *
     * @param type
     * @return
     */
    @Override
    public Boolean insertType(Type type) {
        Boolean paramIsLegal = (type != null && !"".equals(type.getTypeName()));
        if (!paramIsLegal) {
            log.error("新增类型的参数不合法!");
            throw new CommonException(ResponseEnum.TYPE_PARAM_ILLEGAL.getCode(),
                    ResponseEnum.TYPE_PARAM_ILLEGAL.getMsg());
        }
        int insertLine = typeMapper.insertSelective(type);
        if (insertLine != 1) {
            log.error("新增类型失败!新增类型:{}",type);
            throw new CommonException(ResponseEnum.INSERT_TYPE_ERROR.getCode(),
                    ResponseEnum.INSERT_TYPE_ERROR.getMsg());
        }
        return true;
    }

    /**
     * 删除类型前确认此类型下的博客是否已经没有博客了
     * 若还存在博客——删除失败，false
     * 若不存在博客——删除成功，true
     *
     * @param typeById
     * @return
     */
    @Override
    public Boolean deleteType(Type typeById) {
        //若删除时的参数非法
        if (typeById == null || typeById.getTypeId() == null) {
            log.error(ResponseEnum.DELETE_TYPE_PARAM_ERROR.getMsg());
            throw new CommonException(ResponseEnum.DELETE_TYPE_PARAM_ERROR.getCode(),
                    ResponseEnum.DELETE_TYPE_PARAM_ERROR.getMsg());
        }
        //删除之前先查询此类型下是否还存在博客
        List<Blog> blogsByTypeId = blogService.listBlogsByTypeId(typeById.getTypeId().longValue());
        //此类型下还有博客，不进行删除，返回false
        if (blogsByTypeId.size() != 0) {
            log.error("id为{}的类型下的博客数量为{},不能删除此类型!",
                    typeById.getTypeId(),
                    blogsByTypeId.size());
            return false;
        }
        //删除类型(同时删除)，返回true
        int deleteLine = typeMapper.deleteByPrimaryKey(typeById);
        if (deleteLine != 1) {
            log.error("删除类型时错误!影响了{}条数据!", deleteLine);
        }
        log.info("删除类型{}成功!",typeById.getTypeName());
        return true;
    }
}
