package com.zzy.cloudblogblog;

import com.zzy.cloudblogblog.dto.UserDTO;
import com.zzy.cloudblogblog.feignclient.UserServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zzy
 * @Date 2020/12/2 19:28
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/blogInstances")
    public List<ServiceInstance> instance() {
        List<ServiceInstance> instances = discoveryClient.getInstances("blog-service");
        return instances;
    }

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    @Resource
    private Source source;

    @GetMapping("/q")
    public UserDTO q(UserDTO userDTO){
        return userServiceFeignClient.findByCondition(userDTO);
    }

    @GetMapping("/send")
    public void send() {
        this.source.output().send(
                MessageBuilder.withPayload("hello,this is CLOUD-BLOG")
                        .build()
        );
    }

    @StreamListener("errorChannel")
    public void getErrorMsg(Message message) {
        log.info("blog 收到异常信息: {}");
    }

}
