package com.ylw.backend.dto;

import lombok.Data;

@Data
public class RegisterModelClass {
    public String msg; // 注册成功或失败的信息
    public boolean IsSuccess; // 注册是否成功
}
