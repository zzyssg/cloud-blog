package com.zzy.cloudblogblog.feignclient;

import com.zzy.cloudblogblog.dto.UserDTO;
import com.zzy.cloudblogblog.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zzy
 * @Date 2020/12/3 9:11
 */
@FeignClient(name = "user-service")
public interface UserServiceFeignClient {
    /**
     * 根据用户ID查询该用户下所有博客
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}")
    User getUserById(@PathVariable Integer userId);

    /**
     * 根据用户的条件，查询相应的博客
     * @param userDTO
     * @return
     */
    @GetMapping("/user/query")
    UserDTO listBlogsByUserCondition(@SpringQueryMap UserDTO userDTO);
}
