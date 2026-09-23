package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.global.treasurer.entity.*;
import com.global.treasurer.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Claude
 * @date 2026-01-20
 * @description 资金下拨管理Controller
 */
@RestController
@RequestMapping("/fund-allocation")
@Api(tags = "资金下拨管理")
public class FundAllocationController {

    private static final Logger log = LoggerFactory.getLogger(FundAllocationController.class);

    @Resource
    private TblFundAllocationService tblFundAllocationService;

    /**
     * 获取资金下拨统计数据
     */
    @GetMapping("/statistics")
    @ApiOperation("获取资金下拨统计数据")
    public String getAllocationStatistics(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> statistics = new HashMap<>();
            // 获取总申请数
            long totalApplications = tblFundAllocationService.count();
            // 获取待审批数
            long pendingApproval = tblFundAllocationService.countByStatus("PENDING");
            // 获取今日执行数
            long todayExecutions = tblFundAllocationService.countTodayExecutions();
            // 计算成功率
            long successExecutions = tblFundAllocationService.countByStatus("COMPLETED");
            double successRate = totalApplications > 0 ? (double) successExecutions / totalApplications * 100 : 0;

            statistics.put("totalApplications", totalApplications);
            statistics.put("pendingApproval", pendingApproval);
            statistics.put("todayExecutions", todayExecutions);
            statistics.put("successRate", Math.round(successRate * 100) / 100.0);

            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取资金下拨统计数据失败", e);
            return new JsonBean(0, "获取统计数据失败: " + e.getMessage(), null).toJson();
        }
    }
}

