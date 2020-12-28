package com.zzy.cloudblogcomment.handler;

import com.zzy.cloudblogcomment.entity.ResponseBean;
import com.zzy.cloudblogcomment.enums.ResponseEnum;
import com.zzy.cloudblogcomment.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zzy
 * @Date 2020/12/21 21:21
 */
@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseBean exceptionHandler(HttpServletRequest request,
                                         CommonException exception) {
        log.error("发生异常 url:{},异常信息:{}",
                request.getRequestURL(),
                exception.getRetMsg());
        return new ResponseBean(ResponseEnum.RESPONSE_FAILED.getCode(),
                ResponseEnum.RESPONSE_FAILED.getMsg(),
                exception);
    }
}
