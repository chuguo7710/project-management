package com.example.modules.user.controller;

import com.example.common.annotation.RequireRole;
import com.example.common.context.UserContext;
import com.example.common.enums.Role;
import com.example.common.model.PageResultVO;
import com.example.common.utils.Result;
import com.example.modules.user.dto.UserListQueryDTO;
import com.example.modules.user.service.UserService;
import com.example.modules.user.vo.auth.UserAllVO;
import com.example.modules.user.vo.user.UserListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    /** 获取用户所有数据请求 */
    @GetMapping("/me")
    public Result<UserAllVO> fetchUserAllData() {
        Long userId = UserContext.get().getUserId();
        return Result.success(userServiceImpl.fetchUserAllData(userId));
    }

    /** 获取用户链表请求 */
    @GetMapping("/list")
    public Result<PageResultVO<List<UserListVO>>> fetchUserList(@ModelAttribute UserListQueryDTO userListQueryDTO) {
        return Result.success(userServiceImpl.fetchUserList(userListQueryDTO));
    }

    /** 更改用户状态请求 */
    @RequireRole(value = {Role.ROOT, Role.ADMIN})
    @PutMapping("/{userId}/status")
    public Result<?> changeUserStatus(@PathVariable Long userId, @RequestBody Integer status) {
        userServiceImpl.changeUserStatus(userId, status);
        return Result.success();
    }

    /** 更改用户角色请求 */
    @RequireRole(value = {Role.ROOT})
    @PutMapping("/{userId}/role")
    public Result<?> changeUserRole(@PathVariable Long userId, @RequestBody Integer role) {
        userServiceImpl.changeUserRole(userId, role);
        return Result.success();
    }

    /** 更改账户密码请求 */
    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody String oldPassword, @RequestBody String newPassword) {
        userServiceImpl.changeUserPassword(UserContext.get().getUserId(), oldPassword, newPassword);
        return Result.success();
    }
}
