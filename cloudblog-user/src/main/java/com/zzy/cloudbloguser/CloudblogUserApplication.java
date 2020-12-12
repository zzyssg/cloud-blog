package com.zzy.cloudbloguser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.stream.messaging.Sink;

@MapperScan("com.zzy.cloudbloguser")
@SpringBootApplication
@EnableBinding(Sink.class)
public class CloudblogUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudblogUserApplication.class, args);
    }

}
