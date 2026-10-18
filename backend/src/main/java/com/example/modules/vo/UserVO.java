package com.example.modules.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户信息视图对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private String username;    // 账户名
    private short role;         // 身份
    private short status;       // 状态（启用/禁用）
    private Date createdTime;   // 账户创建时间
}
