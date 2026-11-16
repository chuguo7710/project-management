package com.example.modules.user.dto;

import lombok.Data;

@Data
public class UserListQueryDTO {
    // 查询基本参数
    private Integer page = 1; // 当前页码
    private Integer pageSize = 10; // 每页大小
    private String username; // 用户名

    public Long getOffset() {
        return (long) (this.page - 1) * this.pageSize;
    }
}
