package com.example.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum Role {
    ROOT(1, "root"), // Changed from (short) 1 to 1
    ADMIN(2, "admin"), // Changed from (short) 2 to 2
    USER(3, "user"); // Changed from (short) 3 to 3

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
