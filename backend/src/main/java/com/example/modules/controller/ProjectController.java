package com.example.modules.controller;

import com.example.common.context.UserContext;
import com.example.common.utils.Result;
import com.example.modules.dto.ProjectSearchDTO;
import com.example.modules.service.ProjectService;
import com.example.modules.entity.Project;
import com.example.modules.vo.PageResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/list")
    public Result<PageResultVO<Project[]>> searchProjects(@ModelAttribute ProjectSearchDTO projectSearchDTO) {
        log.info("尝试搜索项目服务 {}", projectSearchDTO);
        PageResultVO<Project[]> pageResultVO = projectService.searchProjects(projectSearchDTO);
        log.info("项目搜索服务完成");
        if(pageResultVO.getResult() != null) {
            return Result.success(pageResultVO);
        }
        return Result.error("未查询到数据");
    }

    @GetMapping("/manage")
    public Result<PageResultVO<Project[]>> getProjects(@ModelAttribute ProjectSearchDTO projectSearchDTO) {
        log.info("尝试搜索我的项目服务 {}", projectSearchDTO);
        PageResultVO<Project[]> pageResultVO = projectService.getProjects(projectSearchDTO);
        log.info("项目搜索我的服务完成");
        if(pageResultVO.getResult() != null) {
            return Result.success(pageResultVO);
        }
        return Result.error("未查询到数据");
    }
    @PostMapping
    public Result<?> addProject(@RequestBody Project project) {
        log.info("尝试添加项目服务 {}", project);
        projectService.addProject(project);
        log.info("项目添加服务完成");
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteProject(@PathVariable Long id) {
        log.info("尝试删除项目服务，id：{}", id);
        projectService.deleteProject(id);
        log.info("项目删除服务完成");
        return Result.success();
    }
}
