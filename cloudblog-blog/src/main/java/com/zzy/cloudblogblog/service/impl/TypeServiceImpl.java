package com.zzy.cloudblogblog.service.impl;

import com.zzy.cloudblogblog.dao.BlogMapper;
import com.zzy.cloudblogblog.dao.TypeMapper;
import com.zzy.cloudblogblog.dto.TypeDTO;
import com.zzy.cloudblogblog.entity.Blog;
import com.zzy.cloudblogblog.entity.Type;
import com.zzy.cloudblogblog.enums.ResponseEnum;
import com.zzy.cloudblogblog.exception.CommonException;
import com.zzy.cloudblogblog.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final BlogMapper blogMapper;

    /**
     * 查询所有博客类型
     * @return
     */
    @Override
    public List<TypeDTO> listAllTypes() {
        // 返回List<TypeDTO>
        //类型为空
        List<Type> types = typeMapper.listAllTypes();
        if (types == null || types.size() == 0) {
            log.error(ResponseEnum.TYPE_IS_NULL.getMsg());
            throw new CommonException(ResponseEnum.TYPE_IS_NULL.getCode(),
                    ResponseEnum.TYPE_IS_NULL.getMsg());
        }
        List<TypeDTO> typeDTOS = new ArrayList<>();
        for (Type type : types) {
            TypeDTO typeDTO = TypeDTO.builder().typeId(type.getTypeId()).typeName(type.getTypeName()).build();
            // 根据TypeId查询List<Blog>
            List<Blog> blogs = blogMapper.listBlogsByTypeId(type.getTypeId().longValue());
            typeDTO.setBlogs(blogs);
            typeDTOS.add(typeDTO);
        }
        log.info("博客类型查询成功!");
        return typeDTOS;
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
        //验证参数的合法性
        if (paramLegal(type)) {
            log.error("新增类型的参数不合法!");
            throw new CommonException(ResponseEnum.TYPE_PARAM_ILLEGAL.getCode(),
                    ResponseEnum.TYPE_PARAM_ILLEGAL.getMsg());
        }
        //验证类型是否已经存在
        Type typeByName = typeMapper.getTypeByName(type.getTypeName());
        if (typeByName != null) {
            log.info("类型[{}]已经存在!",typeByName.getTypeName());
            throw new CommonException(ResponseEnum.TYPE_ALREADY_EXISTS.getCode(), ResponseEnum.TYPE_ALREADY_EXISTS.getMsg());
        }
        //库中不存在，执行插入
        int insertLine = typeMapper.insertSelective(type);
        if (insertLine != 1) {
            log.error("新增类型失败!新增类型:{}",type);
            throw new CommonException(ResponseEnum.INSERT_TYPE_ERROR.getCode(),
                    ResponseEnum.INSERT_TYPE_ERROR.getMsg());
        }
        return true;
    }

    /**
     * 检查Type参数的合法性
     * @param type
     * @return
     */
    private Boolean paramLegal(Type type) {
        return type != null && type.getTypeName()!= null && !"".equals(type.getTypeName());
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
        List<Blog> blogsByTypeId = blogMapper.listBlogsByTypeId(typeById.getTypeId().longValue());
        //此类型下还有博客，不进行删除，返回false
        if (blogsByTypeId.size() != 0) {
            log.error("id为{}的类型下的博客数量为{},不能删除此类型!",
                    typeById.getTypeId(),
                    blogsByTypeId.size());
            return false;
        }
        //删除类型(同时删除，相关)，返回true
        int deleteLine = typeMapper.deleteByPrimaryKey(typeById);
        if (deleteLine != 1) {
            log.error("删除类型时错误!影响了{}条数据!", deleteLine);
        }
        log.info("删除类型{}成功!",typeById.getTypeName());
        return true;
    }
}
