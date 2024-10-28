package com.ylw.backend.dto;

import lombok.Data;

@Data

public class LoginModelClass {
    private String data; // 用于标识用户权限，为"admin"则是普通用户，为"editor"则为创建者用户
    private int code; // 登录成功状态码，20000表示登录成功，60204表示登录失败
    private int userId; // 用户ID
}
