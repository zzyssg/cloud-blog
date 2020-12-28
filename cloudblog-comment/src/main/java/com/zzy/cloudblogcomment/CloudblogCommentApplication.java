package com.zzy.cloudblogcomment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zzy
 */
@MapperScan(value = "com.zzy.cloudblogcomment.dao")
@SpringBootApplication
public class CloudblogCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudblogCommentApplication.class, args);
    }

}
