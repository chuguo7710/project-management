package com.example.modules.task.service.impl;

import com.example.common.context.UserContext;
import com.example.common.enums.Role;
import com.example.common.exception.BusinessException;
import com.example.modules.project.mapper.ProjectMapper;
import com.example.modules.task.dto.TaskCreatePayloadDTO;
import com.example.modules.task.entity.Task;
import com.example.modules.task.entity.TaskValid;
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
        // 检查项目是否存在
        isProjectExist(projectId);
        // 是否是该项目成员
        if (UserContext.get().getRole() != Role.ROOT) {
            Long userId = UserContext.get().getUserId();
            if(projectMapper.selectMyProjectRoleByProjectIdAndUserId(projectId, userId) == null) {
                log.warn("无权限查看项目任务，projectId = {}", projectId);
                throw new BusinessException("无权限查看项目任务");
            }
        }
        // 查询任务列表
        List<Task> tasks = taskMapper.selectTaskListByProjectId(projectId);
        log.info("查询任务列表成功，projectId = {}, tasks:{}", projectId, tasks);
        return tasks;
    }

    @Override
    public void addTask(Long projectId, TaskCreatePayloadDTO taskCreatePayloadDTO) {
        // 是否是该项目成员
        if (UserContext.get().getRole() != Role.ROOT) {
            Long userId = UserContext.get().getUserId();
            if(projectMapper.selectMyProjectRoleByProjectIdAndUserId(projectId, userId) == null) {
                log.warn("无权限添加任务，projectId = {}", projectId);
                throw new BusinessException("无权限添加任务");
            }
        }
        // 创建任务
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

    @Override
    public void removeTask(Long taskId) {
        TaskValid taskValid = taskMapper.selectTaskValidByTaskId(taskId);
        // 检查任务是否存在
        isTaskNotExist(taskId, taskValid);
        // 检查是否是任务创建者或项目领导者
        if(UserContext.get().getRole() != Role.ROOT) {
            Long userId = UserContext.get().getUserId();
            if(!userId.equals(taskValid.getTaskCreatorId()) && !userId.equals(taskValid.getProjectLeaderId())) {
                log.warn("无权限删除任务，taskId = {}", taskId);
                throw new BusinessException("无权限删除任务");
            }
        }
        // 删除任务
        int isDeleted = taskMapper.deleteTaskByTaskId(taskId);
        if (isDeleted == 0) {
            log.warn("删除任务失败，taskId = {}", taskId);
            throw new BusinessException("删除任务失败");
        }
        log.info("删除任务成功，taskId = {}", taskId);
    }

    @Override
    public void changeTaskStatus(Long taskId, Integer status) {
        // 检查任务是否存在
        TaskValid taskValid = taskMapper.selectTaskValidByTaskId(taskId);
        isTaskNotExist(taskId, taskValid);
        // 检查是否是任务创建者、任务负责人或项目领导者
        if(UserContext.get().getRole() != Role.ROOT) {
            Long userId = UserContext.get().getUserId();
            if(!userId.equals(taskValid.getTaskCreatorId())
                    && !userId.equals(taskValid.getTaskAssigneeId())
                    && !userId.equals(taskValid.getProjectLeaderId())) {
                log.warn("无权限更新任务状态，taskId = {}", taskId);
                throw new BusinessException("无权限更新任务状态");
            }
        }
        // 更新任务状态
        int isChanged = taskMapper.updateTaskStatusByTaskId(taskId, status);
        if (isChanged == 0) {
            log.warn("更新任务状态失败，taskId = {}", taskId);
            throw new BusinessException("更新任务状态失败");
        }
        log.info("更新任务状态成功，taskId = {}", taskId);
    }

    private void isProjectExist(Long projectId) {
        if (projectMapper.selectProjectByProjectId(projectId) == null) {
            log.warn("项目不存在，projectId = {}", projectId);
            throw new BusinessException("项目不存在");
        }
    }
    private void isTaskNotExist(Long taskId, TaskValid taskValid) {
        if(taskValid == null) {
            log.warn("任务不存在，taskId = {}", taskId);
            throw new BusinessException("任务不存在");
        } else if (taskValid.getProjectLeaderId() == null) {
            log.warn("任务所附属的项目不存在，taskId = {}", taskId);
            throw new BusinessException("任务所附属的项目不存在");
        }
    }

}
