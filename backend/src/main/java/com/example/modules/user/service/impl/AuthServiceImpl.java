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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Slf4j
@Service
@RequestMapping("/auth")
public class AuthServiceImpl implements AuthService {

    private static final int STATUS_NORMAL = 1;

    @Autowired
    private JwtTools jwtTools;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public String login(LoginDTO loginDTO) {
        // 检验账户是否存在，获取密码哈希
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        String truePassword = userMapper.selectPasswordByUsername(username);
        if(truePassword == null) {
            log.info("账户\"{}\"因账户不存在登录失败", username);
            throw new BusinessException("账户不存在");
        }
        // 比对密码哈希
        if(!passwordEncoder.matches(password, truePassword)) {
            log.info("账户\"{}\"因密码错误登录失败", username);
            throw new BusinessException("密码错误");
        }
        log.info("账户\"{}\"登录成功", username);
        // 判断账户是否正常并创建token
        UserAuthInfoVO authInfo = userMapper.selectAuthInfoByUsername(username);
        if(authInfo.getStatus() != STATUS_NORMAL) {
            log.info("账户\"{}\"因被禁用登录失败", username);
            throw new BusinessException("账户被禁用");
        }
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
        // 验证两次密码是否一致
        if(!Objects.equals(registerDTO.getPassword(), registerDTO.getRePassword())) {
            log.info("账户\"{}\"因密码不一致注册失败", registerDTO.getUsername());
            throw new BusinessException("两次输入的密码不一致");
        }
        // 判断账户是否已经存在
        if (userMapper.selectPasswordByUsername(registerDTO.getUsername()) != null) {
            log.info("账户\"{}\"因账户已存在注册失败", registerDTO.getUsername());
            throw new BusinessException("账户已存在");
        }
        // 加密上传
        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        int isAddUser = userMapper.insertUser(registerDTO);
        int isAddUserProfile = userProfileMapper.insertEmptyProfile(registerDTO.getUserId());
        // 判断注册是否成功
        if (isAddUser == 0 || isAddUserProfile == 0) {
            log.error("账户\"{}\"注册失败", registerDTO.getUsername());
            throw new BusinessException("注册失败");
        }
        log.info("账户\"{}\"注册成功,账户ID：{}", registerDTO.getUsername(), registerDTO.getUserId());
    }

}
