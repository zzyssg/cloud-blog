package com.zzy.cloudblogblog;

import com.zzy.cloudblogblog.configuration.GlobalFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zzy
 */
@MapperScan("com.zzy.cloudblogblog.dao")
@SpringBootApplication
@EnableFeignClients
@EnableBinding(Source.class)
public class CloudblogBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudblogBlogApplication.class, args);
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
