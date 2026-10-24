package com.example.modules.project.service.impl;

import com.example.common.context.UserContext;
import com.example.common.enums.ProjectRole;
import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import com.example.modules.project.dto.ProjectCreateDTO;
import com.example.modules.project.dto.ProjectSearchDTO;
import com.example.modules.project.mapper.ProjectMapper;
import com.example.modules.project.mapper.ProjectMemberMapper;
import com.example.modules.project.service.ProjectService;
import com.example.modules.project.entity.Project;
import com.example.common.model.PageResultVO;
import com.example.modules.project.vo.ProjectMemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public PageResultVO<Project[]> searchListProjects(ProjectSearchDTO projectSearchDTO) {
        // 查询项目列表和总数
        Project[] projects = projectMapper.listProjects(projectSearchDTO);
        Long total = projectMapper.countListProjects(projectSearchDTO);
        for(Project project : projects) {
            project.setCanEdit(isCanEdit(project));
        }
        log.info("项目查询完毕，项目查询总数：{}，查询参数：projectName = {}，leaderName = {}", total, projectSearchDTO.getProjectName(), projectSearchDTO.getLeaderName());
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
        log.info("账户相关项目查询完毕，项目查询总数：{}，查询参数：projectName = {}，leaderName = {}", total, projectSearchDTO.getProjectName(), projectSearchDTO.getLeaderName());
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
        int isAddProjectUser = projectMemberMapper.insertProjectMember
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
        projectMemberMapper.deleteProjectMembersByProjectId(projectId);
        if (isDeleteProject == 0) {
            log.warn("项目删除失败，项目ID：{}", projectId);
            throw new BusinessException("项目删除失败");
        }
        log.info("项目删除成功，项目ID：{}", projectId);
    }

    @Override
    public List<ProjectMemberVO> searchProjectMembers(Long projectId) {
        // 查询项目是否存在
        Project project = projectMapper.selectProjectByProjectId(projectId);
        if (project == null) {
            log.warn("查询项目成员失败，项目ID：{}", projectId);
            throw new BusinessException("项目不存在");
        }
        // 检测身份
        Long userId = UserContext.get().getUserId();
        Integer myProjectRole = projectMapper.selectMyProjectRoleByProjectIdAndUserId(projectId, userId);
        if(UserContext.get().getRole() != Role.ROOT && myProjectRole == null) {
            log.warn("账户{}无权限查询项目成员，项目ID：{}", userId, projectId);
            throw new BusinessException("无权限查询项目成员");
        }
        return projectMemberMapper.selectMemberByProjectId(projectId);
    }

    @Override
    public void addProjectMember(Long projectId, Long userId) {
        // 检查项目是否存在
        Project project = projectMapper.selectProjectByProjectId(projectId);
        if (project == null) {
            log.warn("添加项目成员失败，项目ID：{}", projectId);
            throw new BusinessException("项目不存在");
        }
        // 检测身份
        Integer myRole = projectMapper.selectMyProjectRoleByProjectIdAndUserId(UserContext.get().getUserId(), projectId);
        if (UserContext.get().getRole() != Role.ROOT && (!Objects.equals(myRole, ProjectRole.OWNER.getCode()))) {
            log.warn("账户无权限添加项目成员，项目ID：{}", projectId);
            throw new BusinessException("无权限添加项目成员");
        }
        // 添加 “项目——成员” 关联
        int isAddProjectMember = projectMemberMapper.insertProjectMember(projectId, userId
                , ProjectRole.MEMBER.getCode()
                , LocalDateTime.now());
        if (isAddProjectMember == 0) {
            log.warn("项目成员添加失败，项目ID：{}", projectId);
        throw new BusinessException("项目成员添加失败");
        }
    }

    @Override
    public void removeProjectMember(Long projectId, Long userId) {
        // 检查项目是否存在
        Project project = projectMapper.selectProjectByProjectId(projectId);
        if (project == null) {
            log.warn("删除项目成员失败，项目ID：{}", projectId);
            throw new BusinessException("项目不存在");
        }
        // 检测身份
        Integer myRole = projectMapper.selectMyProjectRoleByProjectIdAndUserId(UserContext.get().getUserId(), projectId);
        if (UserContext.get().getRole() != Role.ROOT && (!Objects.equals(myRole, ProjectRole.OWNER.getCode()))) {
            log.warn("账户无权限删除项目成员，项目ID：{}", projectId);
            throw new BusinessException("无权限删除项目成员");
        }
        // 删除 “项目——成员” 关联
        int isDeleteProjectMember = projectMemberMapper.deleteProjectMemberByProjectIdAndUserId(projectId, userId);
        if (isDeleteProjectMember == 0) {
            log.warn("项目成员删除失败，项目ID：{}", projectId);
            throw new BusinessException("项目成员删除失败");
        }
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
