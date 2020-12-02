package com.zzy.cloudbloguser;

import com.zzy.cloudbloguser.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/2 19:13
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;


    @GetMapping("/instances")
    public List<ServiceInstance> getUser(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        return instances;
    }


}
