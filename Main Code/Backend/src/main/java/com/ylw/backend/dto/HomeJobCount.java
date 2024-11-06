package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeJobCount {
    private String date; // 日期
    private int count; // 当天新增岗位数量
}
