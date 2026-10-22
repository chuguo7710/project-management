package com.example.modules.user.service;

import com.example.modules.user.vo.UserAllVO;

public interface UserService {

    /**
     * 根据userId获取用户所有数据
     * @param userId 用户id
     * @return 用户所有数据
     */
    UserAllVO fetchUserAllDataByUserId(Long userId);

}
