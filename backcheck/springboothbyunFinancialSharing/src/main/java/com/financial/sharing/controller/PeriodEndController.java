package com.financial.sharing.controller;

import com.financial.sharing.service.PeriodEndService;
import com.financial.sharing.vo.param.PeriodEndCheckParam;
import com.financial.sharing.vo.param.PeriodEndClosingParam;
import com.financial.sharing.vo.result.PeriodEndCheckResult;
import com.financial.sharing.vo.result.PeriodEndClosingResult;
import com.financial.sharing.vo.result.PeriodEndStatusResult;
import com.hbfk.util.JsonBean;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 期末处理控制器
 *
 * @author system
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "期末处理管理")
@RestController
@RequestMapping("/general-ledger/period-end")
@CrossOrigin
public class PeriodEndController {

    @Resource
    private PeriodEndService periodEndService;

    @ApiOperation("获取期末处理检查项")
    @GetMapping("/check-items")
    public String getPeriodEndCheckItems(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @ApiParam(value = "会计期间", required = true) @RequestParam String accountingPeriod,
                                         @ApiParam(value = "账簿ID") @RequestParam(required = false) Long bookId,
                                         @ApiParam(value = "租户ID") @RequestParam(required = false) Long tenantId) {
        try {
            log.info("获取期末处理检查项，期间：{}，账簿ID：{}，租户ID：{}", accountingPeriod, bookId, tenantId);

            PeriodEndCheckParam param = new PeriodEndCheckParam();
            param.setAccountingPeriod(accountingPeriod);
            param.setBookId(bookId);
            param.setTenantId(tenantId);

            List<PeriodEndCheckResult> checkItems = periodEndService.getPeriodEndCheckItems(param);

            Map<String, Object> result = new HashMap<>();
            result.put("period", accountingPeriod);
            result.put("checkItems", checkItems);
            result.put("totalItems", checkItems.size());

            long passedCount = checkItems.stream().filter(item -> "PASSED".equals(item.getCheckResult())).count();
            long warningCount = checkItems.stream().filter(item -> "WARNING".equals(item.getCheckResult())).count();
            long failedCount = checkItems.stream().filter(item -> "FAILED".equals(item.getCheckResult())).count();

            result.put("passedCount", passedCount);
            result.put("warningCount", warningCount);
            result.put("failedCount", failedCount);
            result.put("canProceed", failedCount == 0);

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("获取期末处理检查项失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行期末处理检查")
    @PostMapping("/check")
    public String executePeriodEndCheck(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody PeriodEndCheckParam param) {
        try {
            log.info("执行期末处理检查，参数：{}", param);

            PeriodEndCheckResult result = periodEndService.executePeriodEndCheck(param);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("taskId", "CHECK_" + System.currentTimeMillis());
            responseData.put("period", param.getAccountingPeriod());
            responseData.put("status", "COMPLETED");
            responseData.put("startTime", new java.util.Date());
            responseData.put("checkResult", result);

            return createSuccessResponse("检查完成", responseData);
        } catch (Exception e) {
            log.error("执行期末处理检查失败", e);
            return createErrorResponse("检查失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行期末结账")
    @PostMapping("/closing")
    public String executePeriodEndClosing(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody PeriodEndClosingParam param) {
        try {
            log.info("执行期末结账，参数：{}", param);

            PeriodEndClosingResult result = periodEndService.executePeriodEndClosing(param);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("taskId", result.getClosingId());
            responseData.put("period", param.getClosingPeriod());
            responseData.put("status", result.getClosingStatus());
            responseData.put("startTime", result.getStartTime());
            responseData.put("closingResult", result);

            return createSuccessResponse("期末结账完成", responseData);
        } catch (Exception e) {
            log.error("执行期末结账失败", e);
            return createErrorResponse("期末结账失败: " + e.getMessage());
        }
    }

    @ApiOperation("反向期末结账")
    @PostMapping("/reverse-closing")
    public String reversePeriodEndClosing(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestBody PeriodEndClosingParam param) {
        try {
            log.info("反向期末结账，参数：{}", param);

            PeriodEndClosingResult result = periodEndService.reversePeriodEndClosing(param);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("taskId", result.getClosingId());
            responseData.put("period", param.getClosingPeriod());
            responseData.put("status", "COMPLETED");
            responseData.put("reverseTime", result.getClosingTime());
            responseData.put("reverseResult", result);

            return createSuccessResponse("反结账完成", responseData);
        } catch (Exception e) {
            log.error("反向期末结账失败", e);
            return createErrorResponse("反结账失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取期末结账历史")
    @GetMapping("/closing-history")
    public String getPeriodEndClosingHistory(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @ApiParam(value = "账簿ID", required = true) @RequestParam Long bookId,
                                            @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId,
                                            @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String accountingPeriod,
                                            @ApiParam(value = "页码", required = false) @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                            @ApiParam(value = "每页大小", required = false) @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        try {
            log.info("查询期末结账历史，bookId：{}，tenantId：{}，期间：{}", bookId, tenantId, accountingPeriod);

            List<PeriodEndClosingResult> history = periodEndService.getClosingHistory(bookId, tenantId, accountingPeriod, pageNumber, pageSize);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", history);
            data.put("totalRecord", history.size()); // 实际应该从数据库获取总数
            data.put("pageNo", pageNumber);
            data.put("pageSize", pageSize);

            return createSuccessResponse("查询成功", data);
        } catch (Exception e) {
            log.error("获取期末结账历史失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行自动转账")
    @PostMapping("/auto-transfer")
    public String executeAutoTransfer(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody Map<String, Object> param) {
        try {
            log.info("执行自动转账，参数：{}", param);

            String period = (String) param.get("transferPeriod");
            String transferType = (String) param.get("transferType");

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "TRANSFER_" + System.currentTimeMillis());
            result.put("period", period);
            result.put("status", "PROCESSING");
            result.put("startTime", new java.util.Date());
            result.put("transferType", transferType);

            return createSuccessResponse("自动转账任务已启动", result);
        } catch (Exception e) {
            log.error("执行自动转账失败", e);
            return createErrorResponse("自动转账失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取期末处理状态")
    @GetMapping("/status")
    public String getPeriodEndStatus(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @ApiParam(value = "会计期间", required = true) @RequestParam String accountingPeriod,
                                     @ApiParam(value = "账簿ID", required = true) @RequestParam Long bookId,
                                     @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId) {
        try {
            log.info("获取期末处理状态，期间：{}，账簿ID：{}，租户ID：{}", accountingPeriod, bookId, tenantId);

            PeriodEndCheckParam param = new PeriodEndCheckParam();
            param.setAccountingPeriod(accountingPeriod);
            param.setBookId(bookId);
            param.setTenantId(tenantId);

            PeriodEndStatusResult status = periodEndService.getPeriodEndStatus(param);

            return createSuccessResponse("查询成功", status);
        } catch (Exception e) {
            log.error("获取期末处理状态失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取可用会计期间")
    @GetMapping("/periods")
    public String getAvailablePeriods(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @ApiParam(value = "账簿ID", required = true) @RequestParam Long bookId,
                                      @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId) {
        try {
            log.info("获取可用会计期间，bookId：{}，tenantId：{}", bookId, tenantId);

            List<String> periods = periodEndService.getAvailablePeriods(bookId, tenantId);

            return createSuccessResponse("查询成功", periods);
        } catch (Exception e) {
            log.error("获取可用会计期间失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建成功响应
     */
    private String createSuccessResponse(String message, Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg(message);
        json.setData(data);
        return JsonMapper.nonNullMapper().toJson(json);
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}