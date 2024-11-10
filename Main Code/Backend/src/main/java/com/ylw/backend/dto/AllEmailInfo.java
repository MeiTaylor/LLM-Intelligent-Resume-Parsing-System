package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllEmailInfo {
    private int emailId;
    private String emailAddress;
    private boolean isSyncEnabled;
}
