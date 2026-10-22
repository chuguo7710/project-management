package com.example.modules.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户信息视图对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Long userId;
    private String username;    // 账户名
    private Integer role;         // 身份
    private Integer status;       // 状态（启用/禁用）
    private LocalDateTime createTime;   // 账户创建时间
}
