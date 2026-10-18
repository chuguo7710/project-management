package com.example.common.handler;
import com.example.common.exception.BusinessException;
import com.example.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleException(BusinessException e) {
        log.info("发生业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }
}
