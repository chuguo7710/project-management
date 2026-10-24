package com.example.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {
    // 登录基本数据
    @NotBlank(message = "账户名不能为空")
    @Size(min = 1, max = 20, message = "账户名长度在1到20个字符之间")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 1, max = 18, message = "密码长度不能超过18个字符")
    private String password;
}
