package com.zzy.cloudbloguser.controller;

import com.zzy.cloudbloguser.dto.UserDTO;
import com.zzy.cloudbloguser.entity.ResponseBean;
import com.zzy.cloudbloguser.entity.User;
import com.zzy.cloudbloguser.enums.ResponseEnum;
import com.zzy.cloudbloguser.exception.CommonException;
import com.zzy.cloudbloguser.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zzy
 * @Date 2020/12/2 10:16
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserServiceImpl userServiceImpl;

    /**
     * 验证后台登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseBean login(@RequestBody User user){
        ResponseBean result;
        //1、检验user登录参数合法性
        if (!loginParamLegal(user)) {
            log.error("用户后台登录参数非法!登录参数：{}",user);
            throw new CommonException(ResponseEnum.LOGIN_PARAM_ERROR.getRetCode(),
                    ResponseEnum.LOGIN_PARAM_ERROR.getRetMsg());
        }
        //2、根据用户名查询用户
        User userByName = userServiceImpl.getUserByName(user.getUsername());
        //3、根据查询出的用户比较密码是否一样，一样则登录通过；否则不通过
        if (!isEquals(user, userByName)) {
            log.error("用户{}的密码不正确!",user.getUsername());
            throw new CommonException(ResponseEnum.USERNAME_OR_PASSWORD_ERROR.getRetCode(),
                    ResponseEnum.USERNAME_OR_PASSWORD_ERROR.getRetMsg());
        }
        //用户名存在并且密码正确，返回用户的除了密码的一切信息
        User userReturn = new User();
        BeanUtils.copyProperties(userByName,userReturn);
        userReturn.setPassword(null);
        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getRetCode(),
                ResponseEnum.RESPONSE_SUCCESS.getRetMsg(),
                userReturn);
        return result;
    }

    /**
     * 验证密码
     * @param user
     * @param userByName
     * @return
     */
    private boolean isEquals(User user, User userByName) {
        return user.getPassword().equals(userByName.getPassword());
    }

    /**
     * 验证登录参数是否合法
     * @param user
     * @return
     */
    private Boolean loginParamLegal(User user) {
        Boolean loginParamLegal;
        loginParamLegal = user != null &&
                user.getUsername() != null &&
                !user.getUsername().isEmpty() &&
                user.getPassword() != null &&
                !user.getPassword().isEmpty();
        return loginParamLegal;
    }

    /**
     * 验证前端登录 —— 目前暂时用于评论-2020/12/22
     * @param user
     * @return
     */
    @PostMapping("/frontLogin")
    public ResponseBean frontLogin(@RequestBody User user){
        ResponseBean result;
        //根据用户名查询密码，若密码正确，则验证通过
        //1、检验user登录参数合法性
        if (!loginParamLegal(user)) {
            log.error("用户登录参数非法!前端登录参数：{}",user);
            throw new CommonException(ResponseEnum.LOGIN_PARAM_ERROR.getRetCode(),
                    ResponseEnum.LOGIN_PARAM_ERROR.getRetMsg());
        }
        //2、根据用户名查询用户
        User userByName = userServiceImpl.getUserByName(user.getUsername());
        //3、根据查询出的用户比较密码是否一样，一样则登录通过；否则不通过
        if (!isEquals(user, userByName)) {
            log.error("用户{}的密码不正确!",user.getUsername());
            throw new CommonException(ResponseEnum.USERNAME_OR_PASSWORD_ERROR.getRetCode(),
                    ResponseEnum.USERNAME_OR_PASSWORD_ERROR.getRetMsg());
        }
        //返回loginVO / loginDTO
        UserDTO loginUser = new UserDTO();
        BeanUtils.copyProperties(userByName, loginUser);
        loginUser.setToken("123456");

        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getRetCode(),ResponseEnum.RESPONSE_SUCCESS.getRetMsg(),
                loginUser);
        log.info("用户{}登录成功!",user.getUsername());
        return result;

    }

    /**
     * 根据用户ID查询用户
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public User queryUser(@PathVariable Integer userId){
        User user = this.userServiceImpl.getUserById(userId);
        log.info("id为{}的user是:{}", userId, user);
        return user;
    }

    @GetMapping("/query")
    public UserDTO query(UserDTO userDTO){
        return userDTO;
    }

    @PostMapping("/frontRegister")
    public ResponseBean frontRegister(@RequestBody User user){
        ResponseBean result;
        //根据用户名查询密码，若密码正确，则验证通过
        //1、检验user登录参数合法性
        if (!loginParamLegal(user)) {
            log.error("用户登录参数非法!前端登录参数：{}",user);
            throw new CommonException(ResponseEnum.LOGIN_PARAM_ERROR.getRetCode(),
                    ResponseEnum.LOGIN_PARAM_ERROR.getRetMsg());
        }
        //2、根据用户名查询用户
        User userByName = userServiceImpl.getUserByName(user.getUsername());
        //3、根据查询出的用户比较密码是否一样，一样则登录通过；否则不通过
        if (!isEquals(user, userByName)) {
            log.error("用户{}的密码不正确!",user.getUsername());
            throw new CommonException(ResponseEnum.USERNAME_OR_PASSWORD_ERROR.getRetCode(),
                    ResponseEnum.USERNAME_OR_PASSWORD_ERROR.getRetMsg());
        }
        //返回loginVO / loginDTO
        UserDTO loginUser = new UserDTO();
        BeanUtils.copyProperties(userByName, loginUser);

        result = new ResponseBean(ResponseEnum.RESPONSE_SUCCESS.getRetCode(),ResponseEnum.RESPONSE_SUCCESS.getRetMsg(),
                loginUser);
        log.info("用户{}登录成功!",user.getUsername());
        return result;
    }
}
