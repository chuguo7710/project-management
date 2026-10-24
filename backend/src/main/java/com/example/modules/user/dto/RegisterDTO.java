package com.example.modules.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    // 注册基本数据
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;
    @NotBlank(message = "账户名不能为空")
    @Size(min = 1, max = 20, message = "账户名不能超过20个字符")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 1, max = 18, message = "密码长度不能超过18个字符")
    private String password;
    @NotBlank(message = "请再次输入密码")
    private String rePassword;
    private String email;

}
