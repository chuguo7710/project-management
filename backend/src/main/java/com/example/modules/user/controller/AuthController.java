package com.example.modules.user.controller;

import com.example.modules.user.dto.LoginDTO;
import com.example.modules.user.dto.RegisterDTO;
import com.example.modules.user.service.AuthService;
import com.example.common.utils.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authServiceImpl;

    /** 检测会话 */
    @GetMapping("/session")
    public Result<String> session() {
        return Result.success();
    }

    /** 登录请求 */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        return Result.success(authServiceImpl.login(loginDTO));
    }

    /** 注册请求 */
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authServiceImpl.register(registerDTO);
        return Result.success();
    }
}
