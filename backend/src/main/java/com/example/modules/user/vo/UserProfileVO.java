package com.example.modules.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileVO {
    private String nickname;    // 用户名
    private String avatar;      // 头像url
    private short gender;       // 性别
    private String bio;         // 简介
    private Date updateTime;    // 简介更新时间
}
