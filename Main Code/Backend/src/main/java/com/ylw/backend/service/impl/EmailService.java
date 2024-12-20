package com.ylw.backend.service.impl;

import com.ylw.backend.dto.AllEmailInfo;
import com.ylw.backend.dto.EmailAddInfo;
import com.ylw.backend.dto.CommonReturn;
import com.ylw.backend.dto.EmailReceiveResumeInfo;
import com.ylw.backend.model.EmailMessage;
import com.ylw.backend.model.User;
import com.ylw.backend.model.UserEmail;
import com.ylw.backend.repository.EmailMessageRepository;
import com.ylw.backend.repository.UserEmailRepository;
import com.ylw.backend.repository.UserRepository;
import com.ylw.backend.service.EmailServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedHashMap;

@Service
public class EmailService implements EmailServiceInterface {

    private final UserEmailRepository userEmailRepository;
    private final UserRepository userRepository;
    private final EmailUtils emailUtils;
    private final EmailMessageRepository emailMessageRepository;

    @Autowired
    public EmailService(UserEmailRepository userEmailRepository,
                        UserRepository userRepository,
                        EmailUtils emailUtils,
                        EmailMessageRepository emailMessageRepository) {
        this.userEmailRepository = userEmailRepository;
        this.userRepository = userRepository;
        this.emailUtils = emailUtils;
        this.emailMessageRepository = emailMessageRepository;
    }

    @Override
    public CommonReturn addEmail(EmailAddInfo emailAddInfo) {
        // 1. 首先验证邮箱账号密码是否正确
        if (!emailUtils.validateEmailCredentials(emailAddInfo.getEmail(), emailAddInfo.getEmailPassword())) {
            return new CommonReturn(50000, "邮箱账号或授权码错误");
        }

        try {
            // 2. 创建邮箱记录
            UserEmail userEmail = new UserEmail();
            userEmail.setEmailAddress(emailAddInfo.getEmail());
            userEmail.setEmailPassword(emailAddInfo.getEmailPassword());
            userEmail.setUserId(emailAddInfo.getUserId());
            userEmail.setSyncEnabled(true); // 默认启用同步

            User user = userRepository.findById(emailAddInfo.getUserId()).orElse(null);
            if (user == null) {
                return new CommonReturn(50000, "用户不存在");
            }
            userEmail.setUser(user);

            // 3. 保存到数据库
            UserEmail savedEmail = userEmailRepository.save(userEmail);


            return new CommonReturn(20000, "创建成功", savedEmail.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonReturn(50000, "该邮箱已经存在");
        }
    }

    @Override
    public List<AllEmailInfo> getAllEmailInfo(int userId) {
        List<AllEmailInfo> allEmailInfos = new ArrayList<>();
        for (UserEmail userEmail : userEmailRepository.findByUserId(userId)) {
            AllEmailInfo allEmailInfo = new AllEmailInfo();
            allEmailInfo.setEmailAddress(userEmail.getEmailAddress());
            allEmailInfo.setEmailId(userEmail.getId());
            allEmailInfo.setSyncEnabled(userEmail.isSyncEnabled());
            // 检查是否正在监控
            allEmailInfos.add(allEmailInfo);
        }
        return allEmailInfos;
    }

    @Override
    public CommonReturn changeEmailStatus(int emailId) {
        UserEmail userEmail = userEmailRepository.findById(emailId).orElse(null);

        if (userEmail == null) {
            return new CommonReturn(50000, "邮箱不存在");
        }

        // 更新状态
        boolean newStatus = !userEmail.isSyncEnabled();
        userEmail.setSyncEnabled(newStatus);
        userEmailRepository.save(userEmail);

        return new CommonReturn(20000, "修改成功！");
    }

    @Override
    public CommonReturn deleteEmail(int emailId) {
        try {
            userEmailRepository.deleteById(emailId);
        }catch (Exception e){
            return new CommonReturn(50000,"删除邮箱失败！");
        }
        return new CommonReturn(20000,"删除邮箱成功！");
    }

    @Override
    public List<EmailReceiveResumeInfo> fondAllEmailReceiveResumeInfo(int userId) {

        return userEmailRepository.findEmailReceiveInfoByUserId(userId);
    }

    @Override
    public Map<LocalDate, Integer> getEmailCountForLastWeek(int userId) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(6);

        // 使用 JPA 查询符合条件的 EmailMessage
        List<Object[]> emailCounts = emailMessageRepository.getEmailCountsByUserAndDateRange(userId, startDate, endDate);

        // 将查询结果转换为 Map
        Map<LocalDate, Integer> countsMap = emailCounts.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(),
                        row -> ((Long) row[1]).intValue()
                ));

        // 使用 Java Stream 填充结果，确保 7 天内所有日期都有数据，使用 LinkedHashMap 保持顺序
        return Stream.iterate(startDate.toLocalDate(), date -> date.plusDays(1))
                .limit(7)
                .collect(Collectors.toMap(
                        date -> date,
                        date -> countsMap.getOrDefault(date, 0),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new // 保持插入顺序
                ));
    }
}





//package com.ylw.backend.service.impl;
//
//import com.ylw.backend.dto.AllEmailInfo;
//import com.ylw.backend.dto.EmailAddInfo;
//import com.ylw.backend.dto.CommonReturn;
//import com.ylw.backend.model.User;
//import com.ylw.backend.model.UserEmail;
//import com.ylw.backend.repository.UserEmailRepository;
//import com.ylw.backend.repository.UserRepository;
//import com.ylw.backend.service.EmailServiceInterface;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class EmailService implements EmailServiceInterface {
//
//    private final UserEmailRepository userEmailRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public EmailService(UserEmailRepository userEmailRepository, UserRepository userRepository) {
//        this.userEmailRepository = userEmailRepository;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public CommonReturn addEmail(EmailAddInfo emailAddInfo) {
//        int id = 0;
//        try {
//            //创建一个对象
//            UserEmail userEmail = new UserEmail();
//            userEmail.setEmailAddress(emailAddInfo.getEmail());
//            userEmail.setEmailPassword(emailAddInfo.getEmailPassword());
//            userEmail.setUserId(emailAddInfo.getUserId());
//            User user = userRepository.findById(emailAddInfo.getUserId()).orElse(null);
//            userEmail.setUser(user);
//            UserEmail newEmail =  userEmailRepository.save(userEmail);
//            id = newEmail.getId();
//        }catch (Exception e){
//            return new CommonReturn(50000,"该邮箱已经存在");
//        }
//        return new CommonReturn(20000,"创建成功",id);
//    }
//
//    @Override
//    public List<AllEmailInfo> getAllEmailInfo(int userId) {
//        List<AllEmailInfo> allEmailInfos = new ArrayList<AllEmailInfo>();
//        for (UserEmail userEmail : userEmailRepository.findByUserId(userId)) {
//                AllEmailInfo allEmailInfo = new AllEmailInfo();
//                allEmailInfo.setEmailAddress(userEmail.getEmailAddress());
//                allEmailInfo.setEmailId(userEmail.getId());
//                allEmailInfo.setSyncEnabled(userEmail.isSyncEnabled());
//                allEmailInfos.add(allEmailInfo);
//        }
//        return  allEmailInfos;
//    }
//
//    @Override
//    public CommonReturn changeEmailStatus(int emailId) {
//        // 先根据ID查出对应的实体
//        UserEmail userEmail = userEmailRepository.findById(emailId).orElse(null);
//
//        // 更新对应的状态
//       if(userEmail != null) {
//           System.out.println("我不为空");
//           userEmail.setSyncEnabled(!userEmail.isSyncEnabled());
//           userEmailRepository.save(userEmail);
//       }
//
//        return new  CommonReturn(20000,"修改成功！");
//    }
//}
