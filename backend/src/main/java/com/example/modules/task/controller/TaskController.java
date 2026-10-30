package com.example.modules.task.controller;

import com.example.common.utils.Result;
import com.example.modules.task.dto.TaskCreatePayloadDTO;
import com.example.modules.task.entity.Task;
import com.example.modules.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{projectId}")
    public Result<List<Task>> getTask(@PathVariable("projectId") Long projectId) {
        return Result.success(taskService.searchTaskList(projectId));
    }

    @PostMapping("/{projectId}")
    public Result<?> addTask(@PathVariable("projectId") Long projectId, @Valid @RequestBody TaskCreatePayloadDTO taskCreatePayloadDTO) {
        taskService.addTask(projectId, taskCreatePayloadDTO);
        return Result.success();
    }
}
