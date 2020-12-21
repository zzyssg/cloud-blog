package com.zzy.cloudblogblog.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @author zzy
 * @Date 2020/12/21 21:18
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommonException extends RuntimeException {

    /**
     * 异常状态码
     */
    private Integer retCode;

    /**
     * 异常状态信息
     */
    private String retMsg;

}
