package com.example.modules.user.service.impl;

import com.example.common.context.UserContext;
import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import com.example.common.model.PageResultVO;
import com.example.common.model.SessionInfo;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.entity.Project;
import com.example.modules.user.dto.UserListQueryDTO;
import com.example.modules.user.dto.UserSearchQueryDTO;
import com.example.modules.user.mapper.UserMapper;
import com.example.modules.user.mapper.UserProfileMapper;
import com.example.modules.user.service.UserService;
import com.example.modules.user.vo.auth.UserAllVO;
import com.example.modules.user.vo.auth.UserProfileVO;
import com.example.modules.user.vo.auth.UserVO;
import com.example.modules.user.vo.user.UserListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public UserAllVO fetchUserAllData(Long userId) {
        // 查询账户数据
        UserVO userVO = userMapper.selectUserByUserId(userId);
        if (userVO == null) {
            throw new BusinessException("账户不存在");
        }
        // 查询用户资料数据
        UserProfileVO userProfileVO = userProfileMapper.selectUserProfileByUserId(userId);
        return new UserAllVO(userVO, userProfileVO);
    }

    @Override
    public PageResultVO<List<UserListVO>> fetchUserList(UserListQueryDTO userListQueryDTO) {
        // 查询项目列表和总数
        List<UserListVO> userVOs = userMapper.selectUserList(userListQueryDTO);
        Long total = userMapper.selectUserListCount(userListQueryDTO);
        log.info("用户查询完毕，用户查询总数：{}，查询参数：username = {}", total, userListQueryDTO);
        return new PageResultVO<>(userVOs, total);
    }

    @Override
    public void changeUserStatus(Long userId, Integer status) {
        SessionInfo sessionInfo = isUserExist(userId);
        // 身份验证
        if(UserContext.get().getRole().getCode() >= sessionInfo.getRole()) {
            log.warn("无权限更改账户状态，userId: {}", userId);
            throw new BusinessException("无权限更改账户状态");
        }
        // 更新账户状态
        int isUpdated = userMapper.updateStatusByUserId(userId, status);
        if(isUpdated == 0) {
            log.warn("账户状态更改失败，userId: {}", userId);
            throw new BusinessException("账户状态更改失败");
        }
    }

    @Override
    public void changeUserRole(Long userId, Integer role) {
        isUserExist(userId);
        // 身份验证
        if(UserContext.get().getRole() != Role.ROOT) {
            log.warn("无权限更改账户角色，userId: {}", userId);
            throw new BusinessException("无权限更改账户角色");
        }
        // 更新账户角色
        int isUpdated = userMapper.updateRoleByUserId(userId, role);
        if(isUpdated == 0) {
            log.warn("账户角色更改失败，userId: {}", userId);
            throw new BusinessException("账户角色更改失败");
        }
    }

    @Override
    public void changeUserPassword(Long userId, String oldPassword, String newPassword) {
        isUserExist(userId);

    }

    public SessionInfo isUserExist(Long userId) {
        SessionInfo sessionInfo = userMapper.selectRoleAndStatusByUserId(userId);
        if (sessionInfo == null) {
            log.warn("账户不存在，userId: {}", userId);
            throw new BusinessException("您所操作的账户不存在");
        }
        return sessionInfo;
    }
}
