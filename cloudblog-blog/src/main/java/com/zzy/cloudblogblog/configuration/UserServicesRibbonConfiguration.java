package com.zzy.cloudblogblog.configuration;

//import com.zzy.configuration.UserserveConfiguration;
import com.zzy.configuration.UserserveConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzy
 * @Date 2020/12/2 20:35
 */
@Configuration
@RibbonClients(defaultConfiguration = UserserveConfiguration.class)
public class UserServicesRibbonConfiguration {
}
