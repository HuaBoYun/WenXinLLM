package com.financial.sharing.controller;

import com.financial.sharing.service.DataFixService;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据修复控制器
 * 用于修复外键约束违反等数据问题
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/data-fix")
public class DataFixController {

    @Autowired
    private DataFixService dataFixService;

    /**
     * 修复缺失的责任中心数据
     */
    @PostMapping("/fix-responsibility-centers")
    public MyJsonBean<Object> fixResponsibilityCenters() {
        try {
            log.info("开始执行责任中心数据修复...");
            dataFixService.fixMissingResponsibilityCenters();

            Map<String, Object> result = new HashMap<>();
            result.put("message", "数据修复任务已执行，请查看日志了解详细结果");
            result.put("success", true);

            return MyJsonBean.successData("数据修复任务已执行，请查看日志了解详细结果", result);
        } catch (Exception e) {
            log.error("执行数据修复时发生错误", e);
            Map<String, Object> result = new HashMap<>();
            result.put("message", "数据修复失败: " + e.getMessage());
            result.put("success", false);

            return MyJsonBean.errorData("数据修复失败: " + e.getMessage());
        }
    }

    /**
     * 检查外键约束状态
     */
    @GetMapping("/check-constraints")
    public MyJsonBean<Object> checkConstraints() {
        try {
            boolean isHealthy = dataFixService.checkForeignKeyConstraints();

            Map<String, Object> result = new HashMap<>();
            result.put("constraintsHealthy", isHealthy);
            result.put("message", isHealthy ? "外键约束检查通过" : "存在外键约束违反问题");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("检查外键约束状态时发生错误", e);
            return MyJsonBean.errorData("检查外键约束状态失败: " + e.getMessage());
        }
    }
}