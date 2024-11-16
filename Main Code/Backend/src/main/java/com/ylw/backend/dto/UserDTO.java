package com.ylw.backend.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDTO {
    private int id;

    private String account;

    private String email;

    private String password;

    private String role;
}
