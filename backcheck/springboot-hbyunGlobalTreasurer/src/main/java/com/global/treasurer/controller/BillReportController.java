package com.global.treasurer.controller;

import com.global.treasurer.service.IBillReportService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 票据报表管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@RestController
@RequestMapping("/bill/report")
@Api(tags = "票据报表管理")
public class BillReportController {
    private static final Logger log = LoggerFactory.getLogger(BillReportController.class);

    @Autowired
    private IBillReportService billReportService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/generate")
    @ApiOperation(value = "生成票据报告", notes = "根据条件生成票据报告")
    public String generate(Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> report = billReportService.generateBillReport(params, loginStaff);
            return JsonBean.success("报告生成成功", report);
        } catch (BizException e) {
            log.warn("生成票据报告失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("生成票据报告失败", e);
            return JsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取票据报告列表", notes = "分页查询票据报告列表")
    public String list(Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> result = billReportService.getBillReports(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询票据报告列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/download/{reportId}")
    @ApiOperation(value = "下载票据报告", notes = "下载指定的票据报告")
    public void download(@PathVariable Long reportId, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }
            billReportService.downloadBillReport(reportId, response);
        } catch (Exception e) {
            log.error("下载票据报告失败", e);
            response.setStatus(500);
        }
    }

    @PostMapping("/detail/{reportId}")
    @ApiOperation(value = "获取报告详情", notes = "获取票据报告详细信息")
    public String detail(@PathVariable Long reportId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }
            Map<String, Object> report = billReportService.getBillReportDetail(reportId);
            if (report == null) {
                return JsonBean.error("报告不存在");
            }
            return JsonBean.success(report);
        } catch (BizException e) {
            log.warn("获取报告详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取报告详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除票据报告", notes = "批量删除票据报告")
    public String delete(Long[] reportIds) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }
            boolean result = billReportService.deleteBillReports(reportIds);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (BizException e) {
            log.warn("删除票据报告失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除票据报告失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }
}

