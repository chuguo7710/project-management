package com.example.modules.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchDTO {
    private Integer page;
    private Integer pageSize;
    private Long userId;
    private String name;
    private String leader;
    private Long offset;
    public Long getOffset() {
        return (long) (this.page - 1) * this.pageSize;
    }
}
