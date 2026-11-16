package com.example.modules.task.entity;

import lombok.Data;

@Data
public class TaskValid {
    private Long projectLeaderId;   // 项目是否存在 + 项目负责人
    private Long taskCreatorId;     // 任务创建者
    private Long taskAssigneeId;    // 任务负责人
}
