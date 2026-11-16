package com.example.modules.task.controller;

import com.example.common.utils.Result;
import com.example.modules.task.dto.TaskChangeDTO;
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

    /** 查询项目下的任务列表请求 */
    @GetMapping("/{projectId}")
    public Result<List<Task>> getTask(@PathVariable("projectId") Long projectId) {
        return Result.success(taskService.searchTaskList(projectId));
    }

    /** 在项目下创建任务请求 */
    @PostMapping("/{projectId}")
    public Result<?> addTask(@PathVariable("projectId") Long projectId, @Valid @RequestBody TaskCreatePayloadDTO taskCreatePayloadDTO) {
        taskService.addTask(projectId, taskCreatePayloadDTO);
        return Result.success();
    }

    /** 删除任务请求 */
    @DeleteMapping("/{taskId}")
    public Result<?> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.removeTask(taskId);
        return Result.success();
    }

    /** 更新任务状态请求 */
    @PutMapping("/{taskId}/status")
    public Result<?> changeTaskStatus(@PathVariable("taskId") Long taskId, @Valid @RequestBody TaskChangeDTO taskChangeDTO) {
        taskService.changeTaskStatus(taskId, taskChangeDTO.getStatus());
        return Result.success();
    }

}
