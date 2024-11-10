package com.ylw.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CommonReturn {
    int code;
    String message;
    int emailId;

    public CommonReturn(int code, String msg) {
        this.code = code;
        this.message = msg;
    }
}
