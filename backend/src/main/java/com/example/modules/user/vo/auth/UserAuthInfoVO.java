package com.example.modules.user.vo.auth;

import lombok.Data;

@Data
public class UserAuthInfoVO {
    private Long userId;
    private Integer status;
    private Integer role;
}
