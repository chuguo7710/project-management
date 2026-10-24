package com.example.modules.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Project {
    // 项目基本数据
    private Long projectId;
    private String projectName;
    private Integer status;
    private Long leaderId;
    private Integer progress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String description;

    // 我的项目卡片额外参数
    private String leaderName;
    private Integer myProjectRole;
    private boolean canEdit;
}
