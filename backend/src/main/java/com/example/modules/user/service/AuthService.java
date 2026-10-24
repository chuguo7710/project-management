package com.example.modules.user.service;

import com.example.modules.user.dto.LoginDTO;
import com.example.modules.user.dto.RegisterDTO;

public interface AuthService {

    /**
     * 登录服务
     * @return  token
     */
    String login(LoginDTO loginDTO);

    /** 注册服务：插入新 user + 初始化 user_profile */
    void register(RegisterDTO registerDTO);

}
