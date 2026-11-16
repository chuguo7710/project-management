package com.example.modules.user.service;

import com.example.common.model.PageResultVO;
import com.example.modules.user.dto.UserListQueryDTO;
import com.example.modules.user.vo.auth.UserAllVO;
import com.example.modules.user.vo.user.UserListVO;

import java.util.List;

public interface UserService {

    /** 根据 userId 获取用户所有数据 */
    UserAllVO fetchUserAllData(Long userId);

    /** 获取用户链表 */
    PageResultVO<List<UserListVO>> fetchUserList(UserListQueryDTO userListQueryDTO);

    /** 根据 userId 更改用户状态 */
    void changeUserStatus(Long userId, Integer status);

    /** 根据 userId 更改用户角色 */
    void changeUserRole(Long userId, Integer role);

    /** 根据 userId 更改账户密码 */
    void changeUserPassword(Long userId, String oldPassword, String newPassword);

}
