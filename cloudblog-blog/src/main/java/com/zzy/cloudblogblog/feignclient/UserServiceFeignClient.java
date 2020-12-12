package com.zzy.cloudblogblog.feignclient;

import com.zzy.cloudblogblog.dto.UserDTO;
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
    @GetMapping("/user/{userId}")
    UserDTO findById(@PathVariable Integer userId);

    @GetMapping("/user/query")
    UserDTO findByCondition(@SpringQueryMap UserDTO userDTO);
}
