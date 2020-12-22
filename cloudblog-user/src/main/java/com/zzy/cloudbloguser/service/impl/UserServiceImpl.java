package com.zzy.cloudbloguser.service.impl;

import com.zzy.cloudbloguser.dao.UserMapper;
import com.zzy.cloudbloguser.entity.user.User;
import com.zzy.cloudbloguser.enums.ResponseEnum;
import com.zzy.cloudbloguser.exception.CommonException;
import com.zzy.cloudbloguser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zzy
 * @Date 2020/12/2 17:03
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    /**
     * 按照主键查询用户
     *
     * @param userId
     * @return
     */
    @Override
    public User getUserById(Integer userId) {
        return this.userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 按照用户名查询用户
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {
        User userByName = userMapper.getUserByName(username);
        //用户不存在
        if (userByName == null) {
            log.error("未查到名为{}的用户!",username);
            throw new CommonException(ResponseEnum.USER_NOT_FOUND.getRetCode(), ResponseEnum.USER_NOT_FOUND.getRetMsg());
        }
        return userByName;
    }
}
