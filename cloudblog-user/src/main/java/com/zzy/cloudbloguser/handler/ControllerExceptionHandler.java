package com.zzy.cloudbloguser.handler;

import com.zzy.cloudbloguser.entity.ResponseBean;
import com.zzy.cloudbloguser.enums.ResponseEnum;
import com.zzy.cloudbloguser.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzy
 * @Date 2020/12/22 16:19
 */
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseBean exceptionHandler(HttpServletRequest request,
                                         CommonException exception){
        ResponseBean result;
        log.error("收到异常--请求地址为:{},异常信息为:{}", request.getRequestURL(),
                exception.getRetMsg());
        result = new ResponseBean(ResponseEnum.RESPONSE_FAILED.getRetCode(), ResponseEnum.RESPONSE_FAILED.getRetMsg(),
                exception);
        return result;
    }

}
