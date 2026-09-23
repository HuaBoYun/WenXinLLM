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
import org.springframework.http.MediaType;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Claude
 * @date 2026-01-20
 * @description 内部借贷管理Controller
 */
@RestController
@RequestMapping("/internal-loan")
@Api(tags = "内部借贷管理")
public class InternalLoanController {

    private static final Logger log = LoggerFactory.getLogger(InternalLoanController.class);

    @Resource
    private TblInternalLoanService tblInternalLoanService;

    /**
     * 分页查询内部借贷
     */
    @GetMapping("/page")
    @ApiOperation("分页查询内部借贷")
    public String getLoanPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("借贷编号") @RequestParam(required = false) String loanNo,
            @ApiParam("借贷状态") @RequestParam(required = false) String loanStatus,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblInternalLoan> pageInfo = tblInternalLoanService.getLoanPage(pageNo, pageSize, loanNo, loanStatus, null, null);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询内部借贷分页数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询内部借贷
     */
    @GetMapping("/{loanId}")
    @ApiOperation("根据ID查询内部借贷")
    public String getLoanById(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {

        try {
            TblInternalLoan loan = tblInternalLoanService.getLoanById(loanId);
            return JsonBean.success(loan);
        } catch (Exception e) {
            log.error("查询内部借贷详情失败, loanId={}", loanId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建内部借贷申请
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("创建内部借贷申请")
    public String createLoan(
            @ApiParam("内部借贷信息") @FlexibleRequestBody TblInternalLoan loan,
            HttpServletResponse response) throws IOException {

        try {
            TblInternalLoan saved = tblInternalLoanService.saveLoan(loan);
            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建内部借贷申请失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 提交内部借贷申请
     */
    @PostMapping("/{loanId}/submit")
    @ApiOperation("提交内部借贷申请")
    public String submitLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {

        try {
            tblInternalLoanService.submitLoan(loanId);
            return JsonBean.success("提交成功");
        } catch (Exception e) {
            log.error("提交内部借贷申请失败, loanId={}", loanId, e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批内部借贷
     */
    @PostMapping("/{loanId}/approve")
    @ApiOperation("审批内部借贷")
    public String approveLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            @ApiParam("审批结果") @RequestParam String approveResult,
            @ApiParam("审批备注") @RequestParam(required = false) String approveRemark,
            HttpServletResponse response) throws IOException {

        try {
            tblInternalLoanService.approveLoan(loanId, approveResult, approveRemark);
            return JsonBean.success("审批成功");
        } catch (Exception e) {
            log.error("审批内部借贷失败, loanId={}", loanId, e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 放款
     */
    @PostMapping("/{loanId}/disburse")
    @ApiOperation("放款")
    public String disburseLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {

        try {
            String loanNo = tblInternalLoanService.disburseLoan(loanId);
            return JsonBean.success("放款成功", loanNo);
        } catch (Exception e) {
            log.error("放款失败, loanId={}", loanId, e);
            return new JsonBean(0, "放款失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消内部借贷
     */
    @PutMapping("/{loanId}/cancel")
    @ApiOperation("取消内部借贷")
    public String cancelLoan(
            @ApiParam("借贷ID") @PathVariable String loanId,
            HttpServletResponse response) throws IOException {

        try {
            tblInternalLoanService.cancelLoan(loanId);
            return JsonBean.success("取消成功");
        } catch (Exception e) {
            log.error("取消内部借贷失败, loanId={}", loanId, e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取内部借贷统计数据
     */
    @GetMapping("/statistics")
    @ApiOperation("获取内部借贷统计数据")
    public String getLendingStatistics(HttpServletResponse response) throws IOException {
        try {
            Map<String, Object> statistics = new HashMap<>();
            // 获取总申请数
            long totalApplications = tblInternalLoanService.count();
            // 获取待处理申请数
            long pendingApplications = tblInternalLoanService.countByStatus("PENDING");
            // 获取今日申请数
            long todayApplications = tblInternalLoanService.countTodayApplications();
            // 计算成功率
            long successApplications = tblInternalLoanService.countByStatus("DISBURSED");
            double successRate = totalApplications > 0 ? (double) successApplications / totalApplications * 100 : 0;

            statistics.put("totalApplications", totalApplications);
            statistics.put("pendingApplications", pendingApplications);
            statistics.put("todayApplications", todayApplications);
            statistics.put("successRate", Math.round(successRate * 100) / 100.0);

            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取内部借贷统计数据失败", e);
            return new JsonBean(0, "获取统计数据失败: " + e.getMessage(), null).toJson();
        }
    }
}

