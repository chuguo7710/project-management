package com.example.modules.project.controller;

import com.example.common.annotation.RequireRole;
import com.example.common.enums.Role;
import com.example.common.utils.Result;
import com.example.modules.project.dto.AddMemberDTO;
import com.example.modules.project.dto.EditProjectDTO;
import com.example.modules.project.dto.ProjectCreateDTO;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.service.ProjectService;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;
import com.example.modules.project.vo.ProjectMemberVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @RequireRole({Role.ADMIN, Role.ROOT})
    @PostMapping
    public Result<?> addProject(@Valid @RequestBody ProjectCreateDTO projectCreateDTO) {
        projectService.addProject(projectCreateDTO);
        return Result.success();
    }

    /** 删除项目 */
    @RequireRole({Role.ADMIN, Role.ROOT})
    @DeleteMapping("/{projectId}")
    public Result<?> deleteProject(@PathVariable Long projectId) {
        projectService.removeProject(projectId);
        return Result.success();
    }

    /** 更新项目 */
    @RequireRole({Role.ADMIN, Role.ROOT})
    @PutMapping("/{projectId}")
    public Result<?> updateProject(@PathVariable Long projectId, @Valid @RequestBody EditProjectDTO editProjectDTO) {
        projectService.changeProject(projectId, editProjectDTO);
        return Result.success();
    }

    /** 查询项目成员 */
    @GetMapping("/{projectId}/members")
    public Result<List<ProjectMemberVO>> searchProjectMembers(@PathVariable Long projectId) {
        return Result.success(projectService.searchProjectMembers(projectId));
    }

    /** 添加项目成员 */
    @PostMapping("/{projectId}/members")
    public Result<?> addProjectMember(@PathVariable Long projectId, @RequestBody AddMemberDTO addMemberDTO) {
        projectService.addProjectMember(projectId, addMemberDTO.getUserId());
        return Result.success();
    }

    /** 删除项目成员 */
    @DeleteMapping("/{projectId}/members/{userId}")
    public Result<?> deleteProjectMember(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.removeProjectMember(projectId, userId);
        return Result.success();
    }

}
