package com.zzy.cloudbloguser.service;

import com.zzy.cloudbloguser.dao.user.UserMapper;
import com.zzy.cloudbloguser.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zzy
 * @Date 2020/12/2 17:03
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper userMapper;

    public User findById(Integer userId) {
        return this.userMapper.selectByPrimaryKey(userId);

    }
}
