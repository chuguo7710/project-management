package com.example.modules.task.service.impl;

import com.example.common.context.UserContext;
import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import com.example.modules.project.mapper.ProjectMapper;
import com.example.modules.task.dto.TaskCreatePayloadDTO;
import com.example.modules.task.entity.Task;
import com.example.modules.task.mapper.TaskMapper;
import com.example.modules.task.service.TaskService;
import com.example.modules.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Task> searchTaskList(Long projectId) {
        // 是否是该项目成员,root直接放行
        if(isNotProjectMember(projectId) && UserContext.get().getRole() != Role.ROOT) {
            log.warn("无权限访问项目，projectId = {}", projectId);
            throw new BusinessException("无权限访问项目");
        }
        // 检查项目是否存在
        if (isProjectNotExist(projectId)) {
            log.warn("项目不存在，projectId = {}", projectId);
            throw new BusinessException("项目不存在");
        }
        // 查询任务列表
        List<Task> tasks = taskMapper.selectTaskListByProjectId(projectId);
        log.info("查询任务列表成功，projectId = {}, tasks:{}", projectId, tasks);
        return tasks;
    }

    @Override
    public void addTask(Long projectId, TaskCreatePayloadDTO taskCreatePayloadDTO) {
        Task task = new Task();
        task.setProjectId(projectId);
        task.setTitle(taskCreatePayloadDTO.getTitle());
        task.setDescription(taskCreatePayloadDTO.getDescription());
        task.setStatus(taskCreatePayloadDTO.getStatus());
        task.setPriority(taskCreatePayloadDTO.getPriority());
        task.setDeadline(taskCreatePayloadDTO.getDeadline());
        task.setAssigneeId(taskCreatePayloadDTO.getAssigneeId());
        task.setCreatorId(UserContext.get().getUserId());
        task.setCreatorName(userMapper.selectUsernameByUserId(UserContext.get().getUserId()));
        int isAdded = taskMapper.insertTask(task);
        if (isAdded == 0) {
            log.warn("添加任务失败，taskId = {}", task.getTaskId());
            throw new BusinessException("添加任务失败");
        }
        log.info("添加任务成功，taskId = {}", task.getTaskId());
    }

    private boolean isNotProjectMember(Long projectId) {
        return projectMapper.selectMyProjectRoleByProjectIdAndUserId(projectId, UserContext.get().getUserId()) == null;
    }
    private boolean isProjectNotExist(Long projectId) {
        return projectMapper.selectProjectByProjectId(projectId) == null;
    }
}
