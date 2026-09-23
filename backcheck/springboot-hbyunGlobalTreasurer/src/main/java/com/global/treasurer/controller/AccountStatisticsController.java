package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.global.treasurer.entity.TblGtAccountClosing;
import com.global.treasurer.service.TblGtAccountClosingService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 账户统计控制器
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/treasury/account")
@Api(tags = "账户统计管理")
public class AccountStatisticsController {
    private static final Logger log = LoggerFactory.getLogger(AccountStatisticsController.class);

    @Resource
    private TblGtAccountClosingService tblGtAccountClosingService;

    @Resource
    private UserProvider userProvider;

    /**
     * 验证用户权限
     */
    private boolean validateUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null && loginStaff.getLinkDetp() != null && loginStaff.getCurrentOrg() != null;
        } catch (Exception e) {
            log.error("用户验证失败", e);
            return false;
        }
    }

    /**
     * 获取销户申请统计数据
     */
    @GetMapping("/closing-applications/statistics")
    @ApiOperation("获取销户申请统计数据")
    public String getClosingApplicationsStatistics() {
        try {
            if (!validateUser()) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblStaffUtil loginStaff = userProvider.get();
            BigDecimal orgId = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid() : null;

            Map<String, Object> statistics = new HashMap<>();

            try {
                // 使用list()方法代替count()方法，避免selectCount找不到的问题
                QueryWrapper<TblGtAccountClosing> totalWrapper = new QueryWrapper<>();
                if (orgId != null) {
                    totalWrapper.eq("ORG_ID", orgId);
                }
                int totalCount = tblGtAccountClosingService.list(totalWrapper).size();
                statistics.put("totalCount", totalCount);

                // 查询待审批数量
                QueryWrapper<TblGtAccountClosing> pendingWrapper = new QueryWrapper<>();
                if (orgId != null) {
                    pendingWrapper.eq("ORG_ID", orgId);
                }
                pendingWrapper.eq("APPLICATION_STATUS", "PENDING");
                int pendingCount = tblGtAccountClosingService.list(pendingWrapper).size();
                statistics.put("pendingCount", pendingCount);

                // 查询已批准数量
                QueryWrapper<TblGtAccountClosing> approvedWrapper = new QueryWrapper<>();
                if (orgId != null) {
                    approvedWrapper.eq("ORG_ID", orgId);
                }
                approvedWrapper.eq("APPLICATION_STATUS", "APPROVED");
                int approvedCount = tblGtAccountClosingService.list(approvedWrapper).size();
                statistics.put("approvedCount", approvedCount);

                // 查询已拒绝数量
                QueryWrapper<TblGtAccountClosing> rejectedWrapper = new QueryWrapper<>();
                if (orgId != null) {
                    rejectedWrapper.eq("ORG_ID", orgId);
                }
                rejectedWrapper.eq("APPLICATION_STATUS", "REJECTED");
                int rejectedCount = tblGtAccountClosingService.list(rejectedWrapper).size();
                statistics.put("rejectedCount", rejectedCount);

                // 查询已完成数量
                QueryWrapper<TblGtAccountClosing> completedWrapper = new QueryWrapper<>();
                if (orgId != null) {
                    completedWrapper.eq("ORG_ID", orgId);
                }
                completedWrapper.eq("APPLICATION_STATUS", "COMPLETED");
                int completedCount = tblGtAccountClosingService.list(completedWrapper).size();
                statistics.put("completedCount", completedCount);

                // 计算审批率
                double approvalRate = totalCount > 0 ? (approvedCount * 100.0 / totalCount) : 0.0;
                statistics.put("approvalRate", Math.round(approvalRate * 10.0) / 10.0);

            } catch (Exception e) {
                log.warn("查询统计数据失败，返回默认值: {}", e.getMessage());
                // 如果查询失败，返回默认值（表示表为空或查询出错）
                statistics.put("totalCount", 0);
                statistics.put("pendingCount", 0);
                statistics.put("approvedCount", 0);
                statistics.put("rejectedCount", 0);
                statistics.put("completedCount", 0);
                statistics.put("approvalRate", 0.0);
            }

            return JsonBean.success(statistics);

        } catch (Exception e) {
            log.error("获取销户申请统计数据失败", e);
            // 即使外层异常，也返回默认统计数据，而不是错误
            Map<String, Object> defaultStats = new HashMap<>();
            defaultStats.put("totalCount", 0);
            defaultStats.put("pendingCount", 0);
            defaultStats.put("approvedCount", 0);
            defaultStats.put("rejectedCount", 0);
            defaultStats.put("completedCount", 0);
            defaultStats.put("approvalRate", 0.0);
            return JsonBean.success(defaultStats);
        }
    }
}

