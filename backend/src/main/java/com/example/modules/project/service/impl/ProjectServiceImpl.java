package com.example.modules.project.service.impl;

import com.example.common.context.UserContext;
import com.example.common.enums.ProjectRole;
import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import com.example.modules.project.dto.ProjectCreateDTO;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.mapper.ProjectMapper;
import com.example.modules.project.service.ProjectService;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public PageResultVO<Project[]> searchListProjects(ProjectSearchDTO projectSearchDTO) {
        // 查询项目列表和总数
        Project[] projects = projectMapper.listProjects(projectSearchDTO);
        Long total = projectMapper.countListProjects(projectSearchDTO);
        log.info("项目查询完毕，项目查询总数：{}，查询参数：name = {}，leader = {}", total, projectSearchDTO.getProjectName(), projectSearchDTO.getLeaderName());
        return new PageResultVO<>(projects, total);
    }

    @Override
    public PageResultVO<Project[]> searchListMyProjects(ProjectSearchDTO projectSearchDTO) {
        // 添加查询参数：账户ID
        Long userId = UserContext.get().getUserId();
        projectSearchDTO.setUserId(userId);
        // 查询项目列表和总数，设置账户是否可以操作项目
        Project[] projects = projectMapper.listMyProjects(projectSearchDTO);
        Long total = projectMapper.countListMyProjects(projectSearchDTO);
        for(Project project : projects) {
            project.setCanEdit(isCanEdit(project));
        }
        log.info("账户相关项目查询完毕，项目查询总数：{}，查询参数：name = {}，leader = {}", total, projectSearchDTO.getProjectName(), projectSearchDTO.getLeaderName());
        return new PageResultVO<>(projects, total);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addProject(ProjectCreateDTO projectCreateDTO) {
        // 设置项目信息
        Project project = new Project();
        project.setProjectName(projectCreateDTO.getProjectName());
        project.setStatus(projectCreateDTO.getStatus());
        project.setProgress(projectCreateDTO.getProgress());
        project.setDescription(projectCreateDTO.getDescription());

        project.setCreateTime(LocalDateTime.now());
        project.setLeaderId(UserContext.get().getUserId());
        // 添加项目和 “项目——成员” 关联
        int isAddProject = projectMapper.insertProject(project);
        int isAddProjectUser = projectMapper.insertProjectMember
                (project.getProjectId()
                , UserContext.get().getUserId()
                , ProjectRole.OWNER.getCode()
                , project.getCreateTime());
        // 判断项目添加是否成功
        if (isAddProject == 0 || isAddProjectUser == 0) {
            log.warn("项目添加失败，project：{}", project);
            throw new BusinessException("项目添加失败");
        }
        log.info("项目添加完成，project：{}", project);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeProject(Long projectId) {
        // 检查项目是否存在
        Project project = projectMapper.selectProjectByProjectId(projectId);
        if (project == null) {
            log.warn("删除项目不存在，项目ID：{}", projectId);
            throw new BusinessException("项目不存在");
        }
        // 检测身份
        project.setMyProjectRole(projectMapper
                .selectMyProjectRoleByProjectIdAndUserId
                        (projectId, UserContext.get().getUserId()));
        if (!isCanEdit(project)) {
            log.warn("账户无权限删除项目，项目ID：{}", projectId);
            throw new BusinessException("无权限删除项目");
        }
        // 删除项目和项目所有成员关联
        int isDeleteProject = projectMapper.deleteProjectByProjectId(projectId);
        projectMapper.deleteProjectMembersByProjectId(projectId);
        if (isDeleteProject == 0) {
            log.warn("项目删除失败，项目ID：{}", projectId);
            throw new BusinessException("项目删除失败");
        }
        log.info("项目删除成功，项目ID：{}", projectId);
    }

    private boolean isCanEdit(Project project) {
        // root不受限制
        if (UserContext.get().getRole() == Role.ROOT) {
            return true;
        }
        // 项目owner不受限制
        if (project.getMyProjectRole() != null) {
            return Objects.equals(project.getMyProjectRole(), ProjectRole.OWNER.getCode());
        }
        // 若项目myProjectRole未设置，根据项目查询是否是创建者
        return Objects.equals(project.getLeaderId(), UserContext.get().getUserId());
    }

}
