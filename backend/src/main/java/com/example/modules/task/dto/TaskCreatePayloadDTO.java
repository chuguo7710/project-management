package com.example.modules.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class TaskCreatePayloadDTO {
    @NotBlank(message = "任务标题不能为空")
    private String title;
    private String description;
    private Long assigneeId;
    private Integer status;
    private Integer priority;
    private Date deadline;
}
