package com.ylw.backend.service.impl;


import com.ylw.backend.dto.EmailMoniterInfo;
import com.ylw.backend.model.Applicant;
import com.ylw.backend.model.EmailMessage;
import com.ylw.backend.model.Resume;
import com.ylw.backend.model.UserEmail;
import com.ylw.backend.repository.EmailMessageRepository;
import com.ylw.backend.repository.UserEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class  EmailMoniterSercive {

    private final UserEmailRepository userEmailRepository;
    private final EmailUtils emailUtils;
    private final EmailService emailService;
    private final ResumeService resumeService;
    private final EmailMessageRepository emailMessageRepository;

    @Autowired
    public EmailMoniterSercive(UserEmailRepository userEmailRepository, EmailUtils emailUtils, EmailService emailService,ResumeService resumeService,EmailMessageRepository emailMessageRepository) {
        this.userEmailRepository = userEmailRepository;
        this.emailUtils = emailUtils;
        this.emailService = emailService;
        this.resumeService = resumeService;
        this.emailMessageRepository = emailMessageRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void moniterEmail(){
        List<EmailMoniterInfo> emails = userEmailRepository.findEmailBySyncEnabled(true);
        for (EmailMoniterInfo email : emails) {
            System.out.println(email);
            //地址， 授权码  => [四个信息]
            List<Map<String, Object>> test = emailUtils.getNewEmails(email.getEmailAddress(),email.getEmailPassword(),"D:\\study\\resume");
            //
            for (Map<String, Object> map : test) {
                String path =  ((List<String>)map.get("attachment")).get(0);
                Applicant applicant = resumeService.parseResume(path);
                Resume resume =  resumeService.createAndSaveResume(path, applicant, 1, email.getUserId());
                //
                EmailMessage emailMessage = new EmailMessage();
                emailMessage.setUserEmailId(email.getEmailId());
                emailMessage.setResumeId(resume.getId());
                emailMessage.setSender((String) map.get("sender"));
                emailMessage.setSubject((String) map.get("subject"));
                emailMessage.setBody((String) map.get("body"));
                emailMessage.setAttachment(path);
                emailMessage.setReceivedDate(LocalDateTime.now());
                emailMessage.setUserEmail(userEmailRepository.findById(email.getEmailId()).orElse(null));
                emailMessage.setResume(resume);
                emailMessageRepository.save(emailMessage);

            }
        }
        System.out.println("又隔了5S了");
    }


}
