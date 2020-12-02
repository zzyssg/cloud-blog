package com.zzy.configuration;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzy
 * @Date 2020/12/2 20:37
 */
@Configuration
public class UserserveConfiguration{
    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }


}
