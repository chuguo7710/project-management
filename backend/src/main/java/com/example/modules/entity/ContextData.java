package com.example.modules.entity;

import com.example.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContextData {
    private Long userId;
    private Role role;
}
