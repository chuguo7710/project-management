package com.example.modules.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResultVO<T> {
    private T result;
    private Long total = 0L;
}
