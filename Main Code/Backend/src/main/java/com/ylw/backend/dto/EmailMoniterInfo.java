package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMoniterInfo {
    int emailId;
    int userId;
    String  emailAddress;
    String  emailPassword;

}
