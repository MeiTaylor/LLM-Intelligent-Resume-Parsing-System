package com.ylw.backend.controller;

import com.ylw.backend.dto.*;
import com.ylw.backend.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/add")
    public CommonReturn addEmail(@RequestBody EmailAddInfo emailAddInfo) {
        System.out.println(emailAddInfo);
        return emailService.addEmail(emailAddInfo);
    }

    @PostMapping("/get/all/email")
    public List<AllEmailInfo> getAllEmailInfo(@RequestBody WebSentUserId webSentUserId) {
        System.out.println(webSentUserId);
        return emailService.getAllEmailInfo(webSentUserId.getUserId());
    }

    @PostMapping("/change/status")
    public CommonReturn changeEmailStatus(@RequestBody WebSentEmailId webSentEmailId) {
        System.out.println(webSentEmailId);
        return emailService.changeEmailStatus(webSentEmailId.getEmailId());
    }

    @PostMapping("/delete/email")
    public CommonReturn deleteEmail(@RequestBody WebSentEmailId webSentEmailId) {
        System.out.println(webSentEmailId);
        return emailService.deleteEmail(webSentEmailId.getEmailId());
    }

    @PostMapping("get/all/email/receive/resume/info")
    public List<EmailReceiveResumeInfo> getAllEmailReceiveResumeInfo(@RequestBody WebSentUserId webSentUserId) {
        System.out.println(webSentUserId);
        return emailService.fondAllEmailReceiveResumeInfo(webSentUserId.getUserId());
    }
}
