package com.example.modules.controller;

import com.example.modules.dto.LoginDTO;
import com.example.modules.service.LoginService;
import com.example.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginServiceImpl;

    @PostMapping
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        log.info("账户\"{}\"尝试登录服务", username);
        String token = loginServiceImpl.login(loginDTO);
        log.info("登录服务完成");
        return Result.success(token);
    }
}
