package com.example.modules.service;

import com.example.modules.dto.ProjectSearchDTO;
import com.example.modules.entity.Project;
import com.example.modules.vo.PageResultVO;

public interface ProjectService {
    PageResultVO<Project[]> searchProjects(ProjectSearchDTO projectSearchDTO);
    PageResultVO<Project[]> getProjects(ProjectSearchDTO projectSearchDTO);
    void addProject(Project project);
    void deleteProject(Long id);
}
