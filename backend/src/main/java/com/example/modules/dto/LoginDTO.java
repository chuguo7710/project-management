package com.example.modules.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String username;
    private String password;
}
