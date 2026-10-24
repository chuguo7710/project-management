package com.example.modules.project.controller;

import com.example.common.utils.Result;
import com.example.modules.project.dto.ProjectCreateDTO;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.service.ProjectService;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /** 查询项目列表 */
    @GetMapping("/list")
    public Result<PageResultVO<Project[]>> searchListProjects(@ModelAttribute ProjectSearchDTO projectSearchDTO) {
            return Result.success(projectService.searchListProjects(projectSearchDTO));
    }

    /** 查询我的项目列表 */
    @GetMapping("/my")
    public Result<PageResultVO<Project[]>> searchListMyProjects(@ModelAttribute ProjectSearchDTO projectSearchDTO) {
        return Result.success(projectService.searchListMyProjects(projectSearchDTO));
    }

    /** 添加项目 */
    @PostMapping
    public Result<?> addProject(@Valid @RequestBody ProjectCreateDTO projectCreateDTO) {
        projectService.addProject(projectCreateDTO);
        return Result.success();
    }

    /** 删除项目 */
    @DeleteMapping("/{id}")
    public Result<?> deleteProject(@PathVariable Long id) {
        projectService.removeProject(id);
        return Result.success();
    }

}
