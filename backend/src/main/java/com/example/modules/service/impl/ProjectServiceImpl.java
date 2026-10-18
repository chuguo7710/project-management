package com.example.modules.service.impl;

import com.example.common.context.UserContext;
import com.example.modules.dto.ProjectSearchDTO;
import com.example.modules.mapper.ProjectMapper;
import com.example.modules.service.ProjectService;
import com.example.modules.entity.Project;
import com.example.modules.vo.PageResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public PageResultVO<Project[]> searchProjects(ProjectSearchDTO projectSearchDTO) {
        Project[] projects = projectMapper.searchProjects(projectSearchDTO);
        log.info("项目搜索完成");
        log.info("尝试设置项目ID中");
        short count = 1;
        for(Project project : projects) {
            project.setDisplayedId(projectSearchDTO.getOffset()+count++);
        }
        log.info("项目ID设置完成");
        Long total = projectMapper.countSearchProjects(projectSearchDTO);
        return new PageResultVO<>(projects, total);
    }

    @Override
    public PageResultVO<Project[]> getProjects(ProjectSearchDTO projectSearchDTO) {
        Long userId = UserContext.get().getId();
        projectSearchDTO.setUserId(userId);

        Project[] projects = projectMapper.getProjects(projectSearchDTO);
        log.info("项目搜索完成");
        log.info("尝试设置项目ID中");
        short count = 1;
        for(Project project : projects) {
            project.setDisplayedId(projectSearchDTO.getOffset()+count++);
        }
        log.info("项目ID设置完成");
        Long total = projectMapper.countSearchProjects(projectSearchDTO);
        return new PageResultVO<>(projects, total);
    }

    @Transactional
    @Override
    public void addProject(Project project) {
        log.info("尝试添加项目 {}", project);
        projectMapper.insertProject(project);
        projectMapper.insertProjectUser(project.getId(), UserContext.get().getId(), UserContext.get().getRole().getCode(), project.getCreateTime());
        log.info("项目添加完成");
    }

    @Override
    public void deleteProject(Long id) {
        log.info("尝试删除项目 {}", id);
        projectMapper.deleteProjectById(id);
        log.info("项目删除成功");
    }
}
