package com.example.modules.mapper;

import com.example.modules.vo.UserProfileVO;
import com.example.modules.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UtilsMapper {

    /**
     * 根据id查询用户状态
     * @param id 用户id
     * @return 用户状态:<p>1:正常</p><p>0:禁用</p>
     */
    @Select("SELECT status FROM user WHERE id = #{id}")
    Integer selectStatusById(Long id);

    /**
     * 根据id查询账户名
     * @param id 用户id
     * @return 账户名
     */
    @Select("SELECT username FROM user WHERE id = #{id}")
    String selectUsernameById(Long id);

    /**
     * 根据id查询用户账户信息
     * @param id 用户id
     * @return 用户账户信息
     */
    @Select("SELECT username, role, status, create_time FROM user WHERE id = #{id}")
    UserVO selectUserById(Long id);

    /**
     * 根据id查询用户扩展信息
     * @param id 用户id
     * @return 用户扩展信息
     */
    @Select("SELECT nickname, avatar, gender, bio, update_time FROM user_profile WHERE user_id = #{id}")
    UserProfileVO selectUserProfileById(Long id);
}
