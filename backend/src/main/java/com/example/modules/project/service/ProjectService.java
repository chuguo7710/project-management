package com.example.modules.project.service;

import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;

public interface ProjectService {
    PageResultVO<Project[]> searchProjects(ProjectSearchDTO projectSearchDTO);
    PageResultVO<Project[]> getProjects(ProjectSearchDTO projectSearchDTO);
    void addProject(Project project);
    void deleteProject(Long id);
}
