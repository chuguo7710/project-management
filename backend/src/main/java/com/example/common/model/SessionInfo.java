package com.example.common.model;

import lombok.Data;

@Data
public class SessionInfo {
    private Long userId;
    private Integer role;
    private Integer status;
}
