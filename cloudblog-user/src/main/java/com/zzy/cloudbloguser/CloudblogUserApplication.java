package com.zzy.cloudbloguser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan("com.zzy.cloudbloguser")
@SpringBootApplication
public class CloudblogUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudblogUserApplication.class, args);
    }

}
