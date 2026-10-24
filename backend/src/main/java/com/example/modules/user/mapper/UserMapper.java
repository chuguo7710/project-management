package com.example.modules.user.mapper;

import com.example.common.model.SessionInfo;
import com.example.modules.user.dto.RegisterDTO;
import com.example.modules.user.vo.UserAuthInfoVO;
import com.example.modules.user.vo.UserVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /** 登录：根据账号名查询密码 */
    @Select("SELECT password FROM accounts WHERE username = #{username}")
    String selectPasswordByUsername(@Param("username") String username);

    /** 登录成功后签发token用：id + role */
    @Select("SELECT user_id, role FROM accounts WHERE username = #{username}")
    UserAuthInfoVO selectAuthInfoByUsername(@Param("username") String username);

    /** 拦截器：账号角色和状态，状态 -> 1 正常，0 禁用*/
    @Select("SELECT role, status FROM accounts WHERE user_id = #{userId}")
    SessionInfo selectRoleAndStatusByUserId(@Param("userId") Long userId);

    /** 个人信息页：根据id查询账号信息 */
    @Select("SELECT user_id, username, role, status, create_time FROM accounts WHERE user_id = #{userId}")
    UserVO selectUserByUserId(@Param("userId") Long userId);

    /** 注册：插入新用户，并把自增主键/账号id（id）回填 */
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    @Insert("INSERT INTO accounts (username, password) VALUES (#{username}, #{password})")
    int insertUser(RegisterDTO registerDTO);

}
