package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeResumeCount {
    private String date; // 日期
    private int count; // 当天新增简历数量
}
