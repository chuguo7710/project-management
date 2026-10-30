package com.example.modules.task.entity;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Task {
    private Long taskId;
    private String title;
    private String description;
    private Long projectId;
    private Long assigneeId;
    private Long creatorId;
    private String creatorName;
    private Integer status;
    private Integer priority;
    private Date deadline;
    private LocalDateTime completedAt;
    private LocalDateTime createTime;

    // 来自accounts、projects表
    private String assigneeName;
    private String projectName;
}
