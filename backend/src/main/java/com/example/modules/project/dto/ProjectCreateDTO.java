package com.example.modules.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectCreateDTO {
    // 空项目基本数据
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 20, message = "项目名称不能超过20个字")
    private String projectName;
    private Integer status = 1;
    private Integer progress = 0;
    @Size(max = 255, message = "项目介绍不能超过255个字")
    private String description;
}
