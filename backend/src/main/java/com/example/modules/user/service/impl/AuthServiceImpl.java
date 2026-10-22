package com.example.modules.user.service.impl;

import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import com.example.common.utils.JwtTools;
import com.example.modules.user.dto.LoginDTO;
import com.example.modules.user.dto.RegisterDTO;
import com.example.modules.user.vo.UserAuthInfoVO;
import com.example.modules.user.mapper.UserMapper;
import com.example.modules.user.mapper.UserProfileMapper;
import com.example.modules.user.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Service
@RequestMapping("/auth")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtTools jwtTools;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public String login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        String truePassword = userMapper.selectPasswordByUsername(username);
        if(truePassword == null) {
            log.info("账户\"{}\"因账户不存在登录失败", username);
            throw new BusinessException("账户不存在");
        }
        // 比对密码
        if(!truePassword.equals(password)) {
            log.info("账户\"{}\"因密码错误登录失败", username);
            throw new BusinessException("密码错误");
        }
        log.info("账户\"{}\"登录成功", username);
        // 创建token
        UserAuthInfoVO authInfo = userMapper.selectAuthInfoByUsername(username);
        Role role = Role.toRole(authInfo.getRole());
        if (role == null) {
            log.error("账户\"{}\"的角色值 {} 无法识别", username, authInfo.getRole());
            throw new BusinessException("账户数据异常");
        }

        log.info("成功获取账户\"{}\"帐户ID：{}, 角色：{}", username, authInfo.getUserId(), role.getDesc());
        return jwtTools.generateToken(authInfo.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterDTO registerDTO) {
        userMapper.insertUser(registerDTO);
        userProfileMapper.insertEmptyProfile(registerDTO.getUserId());
        log.info("账户\"{}\"注册成功,账户ID：{}", registerDTO.getUsername(), registerDTO.getUsername());
    }
}
