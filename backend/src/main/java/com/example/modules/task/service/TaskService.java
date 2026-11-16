package com.example.modules.task.service;

import com.example.modules.task.dto.TaskChangeDTO;
import com.example.modules.task.dto.TaskCreatePayloadDTO;
import com.example.modules.task.entity.Task;

import java.util.List;

public interface TaskService {

    /** 查询项目下的任务列表 */
    List<Task> searchTaskList(Long projectId);
    /** 在项目下创建任务 */
    void addTask(Long projectId, TaskCreatePayloadDTO taskCreatePayloadDTO);
    /** 删除任务 */
    void removeTask(Long taskId);
    /** 更新任务状态 */
    void changeTaskStatus(Long taskId, Integer status);

}
