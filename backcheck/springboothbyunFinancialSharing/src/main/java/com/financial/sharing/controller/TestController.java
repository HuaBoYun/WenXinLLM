package com.financial.sharing.controller;

import com.financial.sharing.util.MyJsonBean;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于验证服务启动
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {

    @GetMapping("/hello")
    public MyJsonBean hello() {
        return MyJsonBean.successData("Hello, Financial Sharing Service is running!");
    }

    @GetMapping("/health")
    public MyJsonBean health() {
        return MyJsonBean.successData("Service is healthy");
    }

    @GetMapping("/localdatetime")
    public MyJsonBean testLocalDateTime() {
        Map<String, Object> data = new HashMap<>();
        data.put("currentTime", LocalDateTime.now());
        data.put("message", "Jackson LocalDateTime序列化测试");
        data.put("testTime", LocalDateTime.of(2025, 12, 9, 13, 23, 45));
        return MyJsonBean.successData(data);
    }

    @GetMapping("/accountsubject/test")
    public MyJsonBean testAccountSubjectLocalDateTime() {
        // 模拟AccountSubjectVO数据，包含LocalDateTime字段
        Map<String, Object> accountSubject = new HashMap<>();
        accountSubject.put("subjectId", 1001L);
        accountSubject.put("subjectCode", "1001");
        accountSubject.put("subjectName", "测试会计科目");
        accountSubject.put("createTime", LocalDateTime.now());
        accountSubject.put("updateTime", LocalDateTime.of(2025, 12, 9, 10, 30, 45));
        accountSubject.put("isEnabled", 1);
        accountSubject.put("tenantId", 1L);
        accountSubject.put("bookId", 1L);

        Map<String, Object> data = new HashMap<>();
        data.put("accountSubject", accountSubject);
        data.put("message", "会计科目LocalDateTime序列化测试");

        return MyJsonBean.successData(data);
    }
}