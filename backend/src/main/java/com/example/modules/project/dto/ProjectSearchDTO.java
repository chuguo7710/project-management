package com.example.modules.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchDTO {
    // 查询参数基本数据
    private Integer page = 1;
    private Integer pageSize = 10;
    private String projectName;
    private String leaderName;
    public Long getOffset() {
        return (long) (this.page - 1) * this.pageSize;
    }

    // 我的项目额外参数
    private Long userId;
}
