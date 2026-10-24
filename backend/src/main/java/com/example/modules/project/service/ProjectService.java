package com.example.modules.project.service;

import com.example.modules.project.dto.ProjectCreateDTO;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;
import com.example.modules.project.vo.ProjectMemberVO;

import java.util.List;

public interface ProjectService {

    /** 搜索项目列表 */
    PageResultVO<Project[]> searchListProjects(ProjectSearchDTO projectSearchDTO);

    /** 搜索我的项目列表 */
    PageResultVO<Project[]> searchListMyProjects(ProjectSearchDTO projectSearchDTO);

    /** 添加项目 */
    void addProject(ProjectCreateDTO projectCreateDTO);

    /** 删除项目 */
    void removeProject(Long id);

    /** 查询项目成员 */
    List<ProjectMemberVO> searchProjectMembers(Long projectId);

    /** 添加项目成员 */
    void addProjectMember(Long projectId, Long userId);

    /** 删除项目成员 */
    void removeProjectMember(Long projectId, Long userId);
}
