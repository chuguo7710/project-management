package com.example.modules.project.service;

import com.example.modules.project.dto.ProjectCreateDTO;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;

public interface ProjectService {

    /** 搜索项目列表 */
    PageResultVO<Project[]> searchListProjects(ProjectSearchDTO projectSearchDTO);

    /** 搜索我的项目列表 */
    PageResultVO<Project[]> searchListMyProjects(ProjectSearchDTO projectSearchDTO);

    /** 添加项目 */
    void addProject(ProjectCreateDTO projectCreateDTO);

    /** 删除项目 */
    void removeProject(Long id);

}
