package com.example.modules.user.mapper;

import com.example.common.model.SessionInfo;
import com.example.modules.user.dto.RegisterDTO;
import com.example.modules.user.dto.UserListQueryDTO;
import com.example.modules.user.dto.UserSearchQueryDTO;
import com.example.modules.user.vo.auth.UserAuthInfoVO;
import com.example.modules.user.vo.auth.UserVO;
import com.example.modules.user.vo.user.UserListVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    /** 登录：根据账号名查询密码 */
    @Select("SELECT password FROM accounts WHERE username = #{username}")
    String selectPasswordByUsername(@Param("username") String username);

    /** 登录成功后签发token用：id + role */
    @Select("SELECT user_id, status, role FROM accounts WHERE username = #{username}")
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

    /** 根据账户ID查询账户名 */
    @Select("SELECT username FROM accounts WHERE user_id = #{userId}")
    String selectUsernameByUserId(@Param("userId") Long userId);

    /** 查询用户列表 */
    List<UserListVO> selectUserList(UserListQueryDTO userListQueryDTO);

    /** 查询用户链表数量 */
    Long selectUserListCount(UserListQueryDTO userListQueryDTO);

    /** 根据账户ID更新账户状态 */
    @Update("UPDATE accounts SET status = #{status} WHERE user_id = #{userId}")
    int updateStatusByUserId(@Param("userId") Long userId, @Param("status") Integer status);

    /** 根据账户ID更新账户角色 */
    @Update("UPDATE accounts SET role = #{role} WHERE user_id = #{userId}")
    int updateRoleByUserId(@Param("userId") Long userId, @Param("role") Integer role);

    /** 根据账户ID更改账户密码 */
    @Update("UPDATE accounts SET password = #{password} WHERE user_id = #{userId}")
    int updatePasswordByUserId(@Param("userId") Long userId, @Param("password") String password);
}
