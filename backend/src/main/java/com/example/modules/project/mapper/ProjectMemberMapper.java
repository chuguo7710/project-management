package com.example.modules.project.mapper;

import com.example.modules.project.vo.ProjectMemberVO;
import org.apache.ibatis.annotations.*;


import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProjectMemberMapper {

    /** 查询项目成员信息 */
    @Select("""
            SELECT pa.user_id, a.username, pr.avatar, pa.my_project_role, pa.join_time
            FROM project_account AS pa
            JOIN accounts AS a
            ON a.user_id = pa.user_id
            LEFT JOIN account_profile AS pr
            ON pr.user_id = pa.user_id
            WHERE pa.project_id = #{projectId}
            ORDER BY pa.my_project_role, pa.join_time
            """)
    List<ProjectMemberVO> selectMemberByProjectId(@Param("projectId") Long projectId);

    /** 添加 “项目——成员 ”关联 */
    @Insert("INSERT INTO project_account (project_id, user_id, my_project_role, join_time) VALUES (#{projectId}, #{userId}, #{myProjectRole}, #{joinTime})")
    int insertProjectMember(@Param("projectId") Long projectId, @Param("userId") Long userId, @Param("myProjectRole") Integer myProjectRole, @Param("joinTime") LocalDateTime joinTime);

    /** 删除 “项目——成员 ”关联 */
    @Delete("DELETE FROM project_account WHERE project_id = #{projectId} AND user_id = #{userId}")
    int deleteProjectMemberByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    /** 删除项目所有成员关联 */
    @Delete("DELETE FROM project_account WHERE project_id = #{projectId}")
    void deleteProjectMembersByProjectId(@Param("projectId") Long projectId);

}
