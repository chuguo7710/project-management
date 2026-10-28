package com.example.modules.project.mapper;

import com.example.modules.project.dto.EditProjectDTO;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.entity.Project;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProjectMapper {

    /** 查询项目详情 */
    @Select("SELECT project_id, project_name, status, leader_id, progress, create_time, description FROM projects WHERE project_id = #{projectId}")
    Project selectProjectByProjectId(@Param("projectId") Long projectId);

    /** 查询我的项目角色 */
    @Select("SELECT my_project_role FROM project_account WHERE project_id = #{projectId} AND user_id = #{userId}")
    Integer selectMyProjectRoleByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    /** 查询项目列表计数 */
    Long countListProjects(ProjectSearchDTO projectSearchDTO);

    /** 查询项目列表 */
    Project[] selectListProjects(ProjectSearchDTO projectSearchDTO);

    /** 查询我的项目列表计数 */
    Long countListMyProjects(ProjectSearchDTO projectSearchDTO);

    /** 查询我的项目列表 */
    Project[] selectListMyProjects(ProjectSearchDTO projectSearchDTO);

    /** 添加项目，回填 project_id */
    @Options(useGeneratedKeys = true, keyProperty = "projectId")
    @Insert("INSERT INTO projects (project_name, status, leader_id, progress, description, create_time) VALUES (#{projectName}, #{status}, #{leaderId}, #{progress}, #{description}, #{createTime})")
    int insertProject(Project project);

    /** 删除项目 */
    @Delete("DELETE FROM projects WHERE project_id = #{projectId}")
    int deleteProjectByProjectId(@Param("projectId") Long projectId);

    /** 更新项目 */
    @Update("UPDATE projects SET project_name = #{projectName}, status = #{status}, description = #{description} WHERE project_id = #{projectId}")
    int updateProject(@Param("projectId") Long projectId, EditProjectDTO editProjectDTO);
}
