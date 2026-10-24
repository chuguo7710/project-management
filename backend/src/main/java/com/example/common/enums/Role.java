package com.example.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum Role {
    ROOT(1, "root"),
    ADMIN(2, "admin"),
    USER(3, "user");

    private final Integer code;
    private final String desc;

    Role(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, Role> MAP = Arrays.stream(values())
            .collect(Collectors.toMap(Role::getCode, role -> role, (existing, replacement) -> existing));

    public static Role toRole(Integer code) {
        return MAP.get(code);
    }
}
