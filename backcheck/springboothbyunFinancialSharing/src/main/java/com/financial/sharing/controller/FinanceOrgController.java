package com.financial.sharing.controller;

import com.financial.sharing.service.FinanceOrgService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 财务组织管理控制器
 * 处理财务组织、财务组织树等功能
 *
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/orgs")
@CrossOrigin
public class FinanceOrgController {

    @Autowired
    private FinanceOrgService financeOrgService;

    // ==================== 财务组织管理 ====================

    /**
     * 财务组织列表
     */
    @GetMapping("/getList")
    public MyJsonBean getCwzzList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeOrgService.getCwzzList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取财务组织列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 财务组织关联公司
     */
    @PostMapping("/setCompanyInfo")
    public MyJsonBean relateCompany(@RequestBody Map<String, Object> data) {
        try {
            boolean result = financeOrgService.relateCompany(data);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("财务组织关联公司失败", e);
            return MyJsonBean.errorData("关联失败: " + e.getMessage());
        }
    }

    /**
     * 财务组织列表
     */
    @GetMapping("/getTreeList")
    public MyJsonBean getCwzzTreeList(@RequestParam Map<String, Object> params) {
        try {
            PageResult<Map<String, Object>> result = financeOrgService.getCwzzTreeList(params);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取财务组织树列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}