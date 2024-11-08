package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentJobInfo {
    private String name; // 部门名称
    private int jobNumber; // 部门下的职位数量
    private List<JobInfoForUpload> children; // 部门下的职位列表
}
