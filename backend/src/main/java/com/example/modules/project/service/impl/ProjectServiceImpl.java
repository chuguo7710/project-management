package com.example.modules.project.service.impl;

import com.example.common.context.UserContext;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.mapper.ProjectMapper;
import com.example.modules.project.service.ProjectService;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;
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
        // 设置项目展示ID
        short count = 1;
        for(Project project : projects) {
            project.setDisplayedId(projectSearchDTO.getOffset()+count++);
        }
        // 设置项目总数
        Long total = projectMapper.countSearchProjects(projectSearchDTO);
        log.info("项目查询完毕，项目查询总数：{}，查询参数：name = {}，leader = {}", total, projectSearchDTO.getName(), projectSearchDTO.getLeader());
        return new PageResultVO<>(projects, total);
    }

    @Override
    public PageResultVO<Project[]> getProjects(ProjectSearchDTO projectSearchDTO) {
        Project[] projects = projectMapper.getProjects(projectSearchDTO);
        // 添加查询参数：账户ID
        Long userId = UserContext.get().getUserId();
        projectSearchDTO.setUserId(userId);
        // 设置项目展示ID
        short count = 1;
        for(Project project : projects) {
            project.setDisplayedId(projectSearchDTO.getOffset()+count++);
        }
        // 设置项目总数
        Long total = projectMapper.countSearchProjects(projectSearchDTO);
        log.info("账户相关项目查询完毕，项目查询总数：{}，查询参数：name = {}，leader = {}", total, projectSearchDTO.getName(), projectSearchDTO.getLeader());
        return new PageResultVO<>(projects, total);
    }

    @Transactional
    @Override
    public void addProject(Project project) {
        int isAddProject = projectMapper.insertProject(project);
        int isAddProjectUser = projectMapper.insertProjectUser(project.getId(), UserContext.get().getUserId(), UserContext.get().getRole().getCode(), project.getCreateTime());
        if (isAddProject == 1 && isAddProjectUser == 1) {
            log.info("项目添加完成，project：{}", project);
        }
        else {
            log.error("项目添加失败，project：{}", project);
        }
    }

    @Override
    public void deleteProject(Long id) {
        int isDelete = projectMapper.deleteProjectById(id);
        if (isDelete == 1) {
            log.info("项目删除成功，项目ID：{}", id);
        }
        else {
            log.error("项目删除失败，项目ID：{}", id);
        }
    }
}
