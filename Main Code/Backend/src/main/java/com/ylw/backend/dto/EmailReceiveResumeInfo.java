package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailReceiveResumeInfo {
    String receiveEmail;
    String sendEmail;
    String emailTitle;
    String applicantName;
    String IntentionJob;
    LocalDateTime receiveDate;

}

