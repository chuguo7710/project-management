package com.example.modules.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Long id;
    private Long displayedId;
    private String name;
    private short status;
    private String leader;
    private String progress;
    private String createTime;
    private String description;
}
