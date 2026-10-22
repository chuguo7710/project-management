package com.example.modules.service;

import com.example.modules.dto.LoginDTO;
import com.example.modules.dto.RegisterDTO;

public interface LoginService {
    /**
     * 登录请求
     * @param loginDTO 账户信息：{ username, password }
     * @return 登录成功：返回token <p>登录失败：抛出业务异常（返回Result.error）</p>
     */
    String login(LoginDTO loginDTO);
    /**
     * 注册请求
     * @param registerDTO 账户信息：{ username, password }
     */
    void register(RegisterDTO registerDTO);
}
