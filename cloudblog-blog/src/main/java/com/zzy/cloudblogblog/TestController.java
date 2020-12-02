package com.zzy.cloudblogblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/2 19:28
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/blogInstances")
    public List<ServiceInstance> instance() {
        List<ServiceInstance> instances = discoveryClient.getInstances("blog-service");
        return instances;
    }
}
