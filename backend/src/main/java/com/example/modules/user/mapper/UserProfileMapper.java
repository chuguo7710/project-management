package com.example.modules.user.mapper;

import com.example.modules.user.vo.UserProfileVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserProfileMapper {

    /** 用户信息页：根据用户id查询档案 */
    @Select("SELECT nickname, avatar, gender, bio, update_time FROM user_profile WHERE user_id = #{userId}")
    UserProfileVO selectUserProfileByUserId(@Param("userId") Long userId);

    /** 注册后初始化一条空档案（1:1，只写主键，其余走默认值） */
    @Insert("INSERT INTO user_profile (user_id) VALUES (#{userId})")
    int insertEmptyProfile(@Param("userId") Long userId);
}
