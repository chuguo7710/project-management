package com.example.modules.task.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskChangeDTO {
    @NotNull(message = "任务状态不能为空")
    @Min(value = 1, message = "任务状态不合法")
    @Max(value = 3, message = "任务状态不合法")
    private Integer status;

    private  String description;
}
