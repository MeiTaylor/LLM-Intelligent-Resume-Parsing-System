package com.ylw.backend.dto;

import lombok.Data;

@Data
public class RegisterSentModel {
    public String name;
    public String account;
    public String email;
    public String password;
}

