package com.example.modules.user.service;

import com.example.modules.user.vo.UserAllVO;

public interface UserService {

    /** 根据 userId 获取用户所有数据 */
    UserAllVO fetchUserAllDataByUserId(Long userId);

}
