package com.zzy.gateway;

import lombok.Data;

import java.time.LocalTime;

/**
 * @author zzy
 * @Date 2020/12/7 20:11
 */
@Data
public class TimeBetweenConfig {
    private LocalTime start;
    private LocalTime end;

}
