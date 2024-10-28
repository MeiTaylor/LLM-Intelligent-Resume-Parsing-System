package com.ylw.backend.dto;

import lombok.Data;

@Data// Lombok annotation
public class LoginSentModel {
    public String password;
    public String userName;
}
