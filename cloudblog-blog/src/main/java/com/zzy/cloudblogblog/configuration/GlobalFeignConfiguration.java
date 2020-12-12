package com.zzy.cloudblogblog.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author zzy
 * @Date 2020/12/3 9:47
 */
public class GlobalFeignConfiguration {
    @Bean
    public Logger.Level feignCLoggerLevel(){
        return Logger.Level.FULL;
    }
}
