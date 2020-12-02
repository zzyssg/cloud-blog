package com.zzy.cloudbloguser.controller;

import com.zzy.cloudbloguser.dao.user.UserMapper;
import com.zzy.cloudbloguser.entity.user.User;
import com.zzy.cloudbloguser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zzy
 * @Date 2020/12/2 10:16
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public User queryUser(@PathVariable Integer userId){
        log.info("user...");
        return this.userService.findById(userId);
    }

}
