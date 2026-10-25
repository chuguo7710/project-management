package com.example.modules.project.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectMemberVO {
    private Long userId;
    private String username;
    private String avatar;
    private Integer myProjectRole;
    private LocalDateTime joinTime;
}
