package com.example.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum ProjectRole {
    OWNER(1,"owner"),
    MEMBER(2,"member");

    private final Integer code;
    private final String description;

    ProjectRole(int code, String description) {
        this.code = code;
        this.description = description;
    }

    private static final Map<Integer, ProjectRole> MAP = Arrays.stream(values())
            .collect(Collectors.toMap(ProjectRole::getCode, role -> role, (existing, replacement) -> existing));

    public static ProjectRole toRole(Integer code) {
        return MAP.get(code);
    }
}
