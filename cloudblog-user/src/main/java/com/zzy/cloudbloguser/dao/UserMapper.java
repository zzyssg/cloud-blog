package com.zzy.cloudbloguser.dao;

import com.zzy.cloudbloguser.entity.user.User;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zzy
 */
@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User getUserByName(String username);
}