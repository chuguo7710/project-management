package com.example.modules.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditProjectDTO {
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 20, message = "项目名称长度不能超过20个字符")
    private String projectName;
    @Size(max = 255, message = "项目描述长度不能超过255个字符")
    private String description;
    private Integer status = 1;
}
