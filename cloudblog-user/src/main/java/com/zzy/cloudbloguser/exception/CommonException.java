package com.zzy.cloudbloguser.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author zzy
 * @Date 2020/12/22 16:17
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonException extends RuntimeException {

    /**
     * 异常码
     */
    private Integer retCode;

    /**
     * 异常信息
     */
    private String retMsg;

}
