package com.example.modules.service.impl;

import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import com.example.common.utils.JwtTools;
import com.example.modules.dto.LoginDTO;
import com.example.modules.entity.CreateToken;
import com.example.modules.mapper.LoginMapper;
import com.example.modules.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private JwtTools jwtTools;

    @Override
    public String login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String rightPassword = loginMapper.selectPasswordByUsername(username);
        if(rightPassword == null) {
            log.info("账户\"{}\"因账户不存在登录失败", username);
            throw new BusinessException("账户不存在");
        } else if(!rightPassword.equals(loginDTO.getPassword())) {
            log.info("账户\"{}\"因密码错误登录失败", username);
            throw new BusinessException("密码错误");
        }
        log.info("账户\"{}\"登录成功", username);
        try {
            CreateToken createToken = loginMapper.selectUserIdAndRoleByUsername(username);
            Role role = Role.toRole(createToken.getRole());
            log.info("成功获取账户\"{}\"用户ID：{}, 角色：{}", username, createToken.getId(), role.getDesc());
            return jwtTools.generateToken(createToken.getId().toString(), role.name());
        } catch (Exception e) {
            log.info("账户\"{}\"ID获取失败", username);
            throw new BusinessException("账户ID获取失败");
        }

    }
}
