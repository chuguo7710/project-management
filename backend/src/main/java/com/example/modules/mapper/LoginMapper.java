package com.example.modules.mapper;

import com.example.modules.dto.LoginDTO;
import com.example.modules.dto.RegisterDTO;
import com.example.modules.entity.CreateToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginMapper {

    /**
     * 查询账户密码
     *
     * @param username 账户名
     * @return 密码
     */
    @Select("SELECT password FROM user WHERE username = #{username}")
    String selectPasswordByUsername(String username);

    /**
     * 查询用户ID和身份
     *
     * @param username 账户名
     * @return 用户ID和身份
     */
    @Select("SELECT id, role FROM user WHERE username = #{username}")
    CreateToken selectUserIdAndRoleByUsername(String username);

    /**
     * 查询用户==账户
     *
     * @param loginDTO 账户信息：{ username, password }
     * @return 用户ID(主键)
     */
    @Select("SELECT id FROM user WHERE username = #{username} AND password = #{password}")
    Long checkUser(LoginDTO loginDTO);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    void registerUser(RegisterDTO registerDTO);

    @Insert("INSERT INTO user_profile ( user_id ) VALUES ( #{userId} )")
    void initUserProfile(Long userId);
}
