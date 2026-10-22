package com.example.modules.project.controller;

import com.example.common.utils.Result;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.service.ProjectService;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;
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
        PageResultVO<Project[]> pageResultVO = projectService.searchProjects(projectSearchDTO);
        if(pageResultVO.getResult() != null) {
            return Result.success(pageResultVO);
        }
        return Result.error("未查询到数据");
    }

    @GetMapping("/manage")
    public Result<PageResultVO<Project[]>> getProjects(@ModelAttribute ProjectSearchDTO projectSearchDTO) {
        PageResultVO<Project[]> pageResultVO = projectService.getProjects(projectSearchDTO);

        if(pageResultVO.getResult() != null) {
            return Result.success(pageResultVO);
        }
        return Result.error("未查询到数据");
    }

    @PostMapping
    public Result<?> addProject(@RequestBody Project project) {
        projectService.addProject(project);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return Result.success();
    }
}
