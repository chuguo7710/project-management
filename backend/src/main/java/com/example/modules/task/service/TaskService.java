package com.example.modules.task.service;

import com.example.modules.task.dto.TaskCreatePayloadDTO;
import com.example.modules.task.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> searchTaskList(Long projectId);
    void addTask(Long projectId, TaskCreatePayloadDTO taskCreatePayloadDTO);
}
