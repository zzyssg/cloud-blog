package com.zzy.cloudbloguser;

import com.zzy.cloudbloguser.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.stream.messaging.Sink;

import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/2 19:13
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;


    @GetMapping("/instances")
    public List<ServiceInstance> getUser(){
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        return instances;
    }

    @StreamListener(Sink.INPUT)
    public void getMsg(String msg){
        log.info("收到消息...", msg);
    }

    @StreamListener("errorChannel")
    public void getErrorMsg(Message message) {
        log.info("user收到异常信息: {}", message);
    }


}
