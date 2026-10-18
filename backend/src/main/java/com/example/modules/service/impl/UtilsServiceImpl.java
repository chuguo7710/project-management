package com.example.modules.service.impl;

import com.example.modules.mapper.UtilsMapper;
import com.example.modules.service.UtilsService;
import com.example.modules.vo.UserAllVO;
import com.example.modules.vo.UserProfileVO;
import com.example.modules.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UtilsServiceImpl implements UtilsService {

    @Autowired
    private UtilsMapper utilsMapper;

    @Override
    public UserAllVO fetchUserAllDataByUserId(Long userId) {
        UserVO userVO = utilsMapper.selectUserById(userId);
        log.info("用户账户信息获取成功");
        UserProfileVO userProfileVO = utilsMapper.selectUserProfileById(userId);
        log.info("用户扩展信息获取成功");
        return new UserAllVO(userVO, userProfileVO);
    }
}
