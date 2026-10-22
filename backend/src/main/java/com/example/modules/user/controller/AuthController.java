package com.example.modules.user.controller;

import com.example.modules.user.dto.LoginDTO;
import com.example.modules.user.dto.RegisterDTO;
import com.example.modules.user.service.AuthService;
import com.example.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authServiceImpl;

    @GetMapping("/session")
    public Result<String> session() {
        return Result.success();
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(authServiceImpl.login(loginDTO));
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDTO registerDTO) {
        authServiceImpl.register(registerDTO);
        return Result.success();
    }
}
