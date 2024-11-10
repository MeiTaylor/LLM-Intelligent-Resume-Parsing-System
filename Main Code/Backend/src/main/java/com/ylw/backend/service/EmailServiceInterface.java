package com.ylw.backend.service;


import com.ylw.backend.dto.AllEmailInfo;
import com.ylw.backend.dto.EmailAddInfo;
import com.ylw.backend.dto.CommonReturn;
import com.ylw.backend.dto.EmailReceiveResumeInfo;

import java.util.List;

public interface EmailServiceInterface {
    public CommonReturn addEmail(EmailAddInfo emailAddInfo);
    public List<AllEmailInfo> getAllEmailInfo(int userId);
    public CommonReturn changeEmailStatus(int emailId);
    public CommonReturn deleteEmail(int emailId);
    public List<EmailReceiveResumeInfo> fondAllEmailReceiveResumeInfo(int userId);
}
