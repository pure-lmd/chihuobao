package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.example.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, Exception e) {
        log.error("Exception: ", e);
        return Result.error();
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result customError(HttpServletRequest request, ServiceException e) {
        return Result.error(e.getMsg());
    }
}
