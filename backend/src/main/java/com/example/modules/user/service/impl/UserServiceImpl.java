package com.example.modules.user.service.impl;

import com.example.modules.user.mapper.UserMapper;
import com.example.modules.user.mapper.UserProfileMapper;
import com.example.modules.user.service.UserService;
import com.example.modules.user.vo.UserAllVO;
import com.example.modules.user.vo.UserProfileVO;
import com.example.modules.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public UserAllVO fetchUserAllDataByUserId(Long userId) {
        UserVO userVO = userMapper.selectUserByUserId(userId);
        if (userVO == null) {
            throw new RuntimeException("账户不存在");
        }
        UserProfileVO userProfileVO = userProfileMapper.selectUserProfileByUserId(userId);
        return new UserAllVO(userVO, userProfileVO);
    }
}
