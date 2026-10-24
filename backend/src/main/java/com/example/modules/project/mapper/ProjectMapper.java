package com.example.modules.project.mapper;

import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.entity.Project;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface ProjectMapper {

    /** 查询项目详情 */
    @Select("SELECT project_id, project_name, status, leader_id, progress, create_time, description FROM projects WHERE project_id = #{projectId}")
    Project selectProjectByProjectId(Long projectId);

    /** 查询我的项目角色 */
    @Select("SELECT my_project_role FROM project_account WHERE project_id = #{projectId} AND user_id = #{userId}")
    Integer selectMyProjectRoleByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    /** 查询项目列表计数 */
    Long countListProjects(ProjectSearchDTO projectSearchDTO);

    /** 查询项目列表 */
    Project[] listProjects(ProjectSearchDTO projectSearchDTO);

    /** 查询我的项目列表计数 */
    Long countListMyProjects(ProjectSearchDTO projectSearchDTO);

    /** 查询我的项目列表 */
    Project[] listMyProjects(ProjectSearchDTO projectSearchDTO);

    /** 添加项目，回填 project_id */
    @Options(useGeneratedKeys = true, keyProperty = "projectId")
    @Insert("INSERT INTO projects (project_name, status, leader_id, progress, description, create_time) VALUES (#{projectName}, #{status}, #{leaderId}, #{progress}, #{description}, #{createTime})")
    int insertProject(Project project);

    /** 添加 “项目——成员 ”关联 */
    @Insert("INSERT INTO project_account (project_id, user_id, my_project_role, join_time) VALUES (#{projectId}, #{userId}, #{myProjectRole}, #{joinTime})")
    int insertProjectMember(@Param("projectId") Long projectId, @Param("userId") Long userId, @Param("myProjectRole") Integer myProjectRole, @Param("joinTime") LocalDateTime joinTime);

    /** 删除项目 */
    @Delete("DELETE FROM projects WHERE project_id = #{projectId}")
    int deleteProjectByProjectId(Long projectId);

    /** 删除项目所有成员关联 */
    @Delete("DELETE FROM project_account WHERE project_id = #{projectId}")
    int deleteProjectMembersByProjectId(Long projectId);

    /** 删除 “项目——成员 ”关联 */
    @Delete("DELETE FROM project_account WHERE project_id = #{projectId} AND user_id = #{userId}")
    int deleteProjectMemberByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
