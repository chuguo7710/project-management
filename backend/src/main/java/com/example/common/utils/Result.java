package com.example.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return new Result<>(1, "success", null);
    }

    public static <T> Result<T> success(T object) {
        return new Result<>(1, "success", object);
    }

    public static <T> Result<T> error() {
        return new Result<>(0, "error", null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(0, msg, null);
    }
}
