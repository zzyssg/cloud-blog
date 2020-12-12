package com.zzy.cloudbloggateway.configuration;

import lombok.Data;

import java.time.LocalTime;

/**
 * @author zzy
 * @Date 2020/12/7 21:50
 */
@Data
public class TimeBetweenRouteConfig {
    private LocalTime start;
    private LocalTime end;

}
