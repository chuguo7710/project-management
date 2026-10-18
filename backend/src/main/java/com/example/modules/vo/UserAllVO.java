package com.example.modules.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAllVO {
    UserVO userVO;  // 用户账户信息
    UserProfileVO userProfileVO;    // 用户扩展信息
}
