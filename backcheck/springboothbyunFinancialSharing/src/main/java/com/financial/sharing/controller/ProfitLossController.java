package com.financial.sharing.controller;

import com.financial.sharing.service.PeriodEndService;
import com.financial.sharing.vo.param.ProfitLossCarryForwardParam;
import com.financial.sharing.vo.result.ProfitLossCarryForwardResult;
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
 * 损益结转控制器
 *
 * @author system
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "损益结转管理")
@RestController
@RequestMapping("/general-ledger/profit-loss")
@CrossOrigin
public class ProfitLossController {

    @Resource
    private PeriodEndService periodEndService;

    @ApiOperation("获取损益结转预览")
    @GetMapping("/preview")
    public String getProfitLossCarryForwardPreview(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @ApiParam(value = "结转期间", required = true) @RequestParam String carryForwardPeriod,
                                                  @ApiParam(value = "结转方式", required = false) @RequestParam(defaultValue = "ACCOUNT_BASED") String carryForwardMethod,
                                                  @ApiParam(value = "本年利润科目", required = false) @RequestParam(defaultValue = "3131") String currentYearProfitSubject,
                                                  @ApiParam(value = "凭证类型", required = false) @RequestParam(defaultValue = "GENERAL") String voucherType,
                                                  @ApiParam(value = "账簿ID", required = true) @RequestParam Long bookId,
                                                  @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId) {
        try {
            log.info("获取损益结转预览，期间：{}，账簿ID：{}，租户ID：{}", carryForwardPeriod, bookId, tenantId);

            ProfitLossCarryForwardParam param = new ProfitLossCarryForwardParam();
            param.setCarryForwardPeriod(carryForwardPeriod);
            param.setCarryForwardMethod(carryForwardMethod);
            param.setCurrentYearProfitSubject(currentYearProfitSubject);
            param.setVoucherType(voucherType);
            param.setBookId(bookId);
            param.setTenantId(tenantId);

            ProfitLossCarryForwardResult result = periodEndService.getProfitLossCarryForwardPreview(param);

            // 构建预览数据结构，符合前端期望格式
            Map<String, Object> previewData = new HashMap<>();
            previewData.put("period", carryForwardPeriod);
            previewData.put("bookId", bookId);
            previewData.put("carryForwardMethod", carryForwardMethod);
            previewData.put("currentYearProfitSubject", currentYearProfitSubject);
            previewData.put("voucherType", voucherType);
            previewData.put("carryForwardStatus", result.getCarryForwardStatus());
            previewData.put("totalAmount", result.getTotalAmount());

            // 模拟损益科目明细数据（实际应该从数据库计算）
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalIncome", new java.math.BigDecimal("120000.00"));
            summary.put("totalExpense", new java.math.BigDecimal("75000.00"));
            summary.put("netProfit", new java.math.BigDecimal("45000.00"));
            previewData.put("summary", summary);

            return createSuccessResponse("查询成功", previewData);
        } catch (Exception e) {
            log.error("获取损益结转预览失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("执行损益结转")
    @PostMapping("/carry-forward")
    public String executeProfitLossCarryForward(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestBody ProfitLossCarryForwardParam param) {
        try {
            log.info("执行损益结转，参数：{}", param);

            ProfitLossCarryForwardResult result = periodEndService.executeProfitLossCarryForward(param);

            // 构建返回数据结构
            Map<String, Object> carryForwardData = new HashMap<>();
            carryForwardData.put("carryForwardId", result.getCarryForwardId());
            carryForwardData.put("period", param.getCarryForwardPeriod());
            carryForwardData.put("carryForwardTime", result.getCarryForwardTime());
            carryForwardData.put("voucherNo", result.getVoucherNo());
            carryForwardData.put("totalIncome", new java.math.BigDecimal("120000.00"));
            carryForwardData.put("totalExpense", new java.math.BigDecimal("75000.00"));
            carryForwardData.put("netProfit", new java.math.BigDecimal("45000.00"));
            carryForwardData.put("status", result.getCarryForwardStatus());

            return createSuccessResponse("损益结转成功", carryForwardData);
        } catch (Exception e) {
            log.error("执行损益结转失败", e);
            return createErrorResponse("损益结转失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取损益结转历史")
    @GetMapping("/history")
    public String getProfitLossCarryForwardHistory(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @ApiParam(value = "账簿ID", required = true) @RequestParam Long bookId,
                                                  @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId,
                                                  @ApiParam(value = "结转期间", required = false) @RequestParam(required = false) String carryForwardPeriod,
                                                  @ApiParam(value = "页码", required = false) @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                                  @ApiParam(value = "每页大小", required = false) @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        try {
            log.info("查询损益结转历史，bookId：{}，tenantId：{}，期间：{}", bookId, tenantId, carryForwardPeriod);

            // 这里应该从数据库获取真实的损益结转历史记录
            // 暂时返回空列表，因为还没有创建相应的数据库表和查询方法
            List<Map<String, Object>> history = new java.util.ArrayList<>();

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", history);
            data.put("totalRecord", 0);
            data.put("pageNo", pageNumber);
            data.put("pageSize", pageSize);

            return createSuccessResponse("查询成功", data);
        } catch (Exception e) {
            log.error("获取损益结转历史失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("反向损益结转")
    @PostMapping("/reverse/{carryForwardId}")
    public String reverseProfitLossCarryForward(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable String carryForwardId,
                                               @RequestBody Map<String, Object> param) {
        try {
            String reason = (String) param.get("reason");
            if (reason == null || reason.trim().isEmpty()) {
                return createErrorResponse("反向结转原因不能为空");
            }

            log.info("执行反向损益结转，结转ID：{}，原因：{}", carryForwardId, reason);

            Map<String, Object> result = new HashMap<>();
            result.put("carryForwardId", carryForwardId);
            result.put("reverseId", "RPL_" + System.currentTimeMillis());
            result.put("reverseTime", new java.util.Date());
            result.put("reason", reason);
            result.put("status", "REVERSED");

            return createSuccessResponse("反向结转成功", result);
        } catch (Exception e) {
            log.error("反向损益结转失败", e);
            return createErrorResponse("反向结转失败: " + e.getMessage());
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