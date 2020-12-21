package com.zzy.cloudblogblog.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zzy
 * @Date 2020/12/21 10:41
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseBean {
    /**
     * 返回状态code
     */
    private Integer retCode;
    /**
     * 返回值状态信息
     */
    private String retMsg;

    /**
     * 返回值payload
     */
    private Object result;

}
