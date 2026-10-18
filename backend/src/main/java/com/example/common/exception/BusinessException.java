package com.example.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    public BusinessException(String msg) {
        super(msg);
    }
}
