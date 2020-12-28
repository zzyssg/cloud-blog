package com.zzy.cloudbloguser.service;

import com.zzy.cloudbloguser.entity.User;

/**
 * @author zzy
 * @Date 2020/12/22 17:04
 */
public interface UserService {
    /**
     * 按照主键查询用户
     * @param userId
     * @return
     */
    User getUserById(Integer userId);

    /**
     * 按照用户名查询用户
     * @param username
     * @return
     */
    User getUserByName(String username);
}
