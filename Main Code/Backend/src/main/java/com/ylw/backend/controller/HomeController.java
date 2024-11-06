package com.ylw.backend.controller;

import com.ylw.backend.dto.InfoForHomeModelClass;
import com.ylw.backend.dto.WebSentUserId;
import com.ylw.backend.service.CompanyServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final CompanyServiceInterface companyService;

    @Autowired
    public HomeController(CompanyServiceInterface companyService) {
        this.companyService = companyService;
    }

    //首页信息接口
    @PostMapping("/statistics")
    public InfoForHomeModelClass uploadJobInfo(@RequestBody WebSentUserId webSentUserId) {
        int userId = webSentUserId.getUserId(); // 用户ID
        // 查数据库返回所有岗位的名称以及ID；

        return companyService.getInfoForHome(userId);
    }

}
