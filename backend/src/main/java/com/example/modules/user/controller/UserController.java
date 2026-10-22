package com.example.modules.user.controller;

import com.example.common.context.UserContext;
import com.example.common.utils.Result;
import com.example.modules.user.service.UserService;
import com.example.modules.user.vo.UserAllVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    @GetMapping("/me")
    public Result<UserAllVO> fetchUserAllData(HttpServletRequest request) {
        Long userId = UserContext.get().getUserId();
        return Result.success(userServiceImpl.fetchUserAllDataByUserId(userId));
    }
}
