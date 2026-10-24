package com.example.modules.user.controller;

import com.example.common.context.UserContext;
import com.example.common.utils.Result;
import com.example.modules.user.service.UserService;
import com.example.modules.user.vo.UserAllVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return Result.success(userServiceImpl.fetchUserAllDataByUserId(userId));
    }
}
