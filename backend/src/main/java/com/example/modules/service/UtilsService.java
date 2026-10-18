package com.example.modules.service;

import com.example.modules.vo.UserAllVO;

public interface UtilsService {

    /**
     * 根据userId获取用户所有数据
     * @param userId 用户id
     * @return 用户所有数据
     */
    UserAllVO fetchUserAllDataByUserId(Long userId);

}
