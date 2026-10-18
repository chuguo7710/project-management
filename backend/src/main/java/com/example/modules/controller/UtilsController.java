package com.example.modules.controller;

import com.example.common.utils.Result;
import com.example.modules.service.UtilsService;
import com.example.modules.vo.UserAllVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/utils")
public class UtilsController {

    @Autowired
    private UtilsService utilsServiceImpl;

    /**
     * 校验token
     * @return 始终返回成功。如果校验失败，拦截器会拦截
     */
    @GetMapping("/tokenValid")
    public Result<String> tokenValid() {
        return Result.success();
    }

    /**
     * 获取用户所有数据
     * @param request 从请求获得用户ID
     * @return 用户所有数据
     */
    @GetMapping("/fetchUserAllData")
    public Result<UserAllVO> fetchUserAllData(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取用户数据服务启动，userId: {}", userId);
        UserAllVO userAllVO = utilsServiceImpl.fetchUserAllDataByUserId(userId);
        log.info("获取用户数据服务完成，userId: {}", userId);
        return Result.success(userAllVO);
    }
}
