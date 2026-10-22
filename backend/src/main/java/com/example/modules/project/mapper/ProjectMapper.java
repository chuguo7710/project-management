package com.example.modules.project.mapper;

import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.entity.Project;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProjectMapper {
    Long countSearchProjects(ProjectSearchDTO projectSearchDTO);

    Project[] searchProjects(ProjectSearchDTO projectSearchDTO);

    Long countGetProjects(ProjectSearchDTO projectSearchDTO);

    Project[] getProjects(ProjectSearchDTO projectSearchDTO);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO projects (name, status, leader, progress, description, create_time) VALUES (#{name}, #{status}, #{leader}, #{progress}, #{description}, #{createTime})")
    int insertProject(Project project);

    @Insert("INSERT INTO project_user (project_id, user_id, role, join_time) VALUES (#{projectId}, #{userId}, #{role}, #{joinTime})")
    int insertProjectUser(Long projectId, Long userId, Integer role, String joinTime);

    @Delete("DELETE FROM projects WHERE id = #{id}")
    int deleteProjectById(Long id);
}
