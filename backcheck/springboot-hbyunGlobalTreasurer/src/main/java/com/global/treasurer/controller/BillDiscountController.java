package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillDiscountDTO;
import com.global.treasurer.dto.BillDiscountQueryDTO;
import com.global.treasurer.entity.TblBillDiscount;
import com.global.treasurer.service.IBillDiscountService;
import com.global.treasurer.vo.BillDiscountVO;
import com.hbfk.util.BizException;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.List;

/**
 * 票据贴现管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@RestController
@RequestMapping("/bill/discount")
@Api(tags = "票据贴现管理")
public class BillDiscountController {
    private static final Logger log = LoggerFactory.getLogger(BillDiscountController.class);

    @Autowired
    private IBillDiscountService billDiscountService;

    @PostMapping("/list")
    @ApiOperation(value = "查询票据贴现列表", notes = "分页查询票据贴现列表")
    public String list(BillDiscountQueryDTO queryDTO) {
        try {
            PageInfo<BillDiscountVO> pageInfo = billDiscountService.selectBillDiscountList(queryDTO);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据贴现列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询票据贴现", notes = "分页查询票据贴现列表")
    public String page(BillDiscountQueryDTO queryDTO) {
        try {
            PageInfo<BillDiscountVO> pageInfo = billDiscountService.selectBillDiscountList(queryDTO);
            // 构造符合前端要求的数据格式
            Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("tlist", pageInfo.getList());
            resultMap.put("totalRecord", pageInfo.getTotal());
            return JsonBean.success(resultMap, pageInfo.getList());
        } catch (Exception e) {
            log.error("查询票据贴现列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/detail/{discountId}")
    @ApiOperation(value = "获取票据贴现详情", notes = "根据贴现ID获取详细信息")
    public String detail(@PathVariable Long discountId) {
        try {
            BillDiscountVO vo = billDiscountService.selectBillDiscountById(discountId);
            if (vo == null) {
                return JsonBean.error("贴现记录不存在");
            }
            return JsonBean.success(vo);
        } catch (BizException e) {
            log.warn("获取票据贴现详情失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取票据贴现详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增票据贴现", notes = "新增票据贴现申请")
    public String save(@Valid @FlexibleRequestBody BillDiscountDTO dto) {
        try {
            TblBillDiscount discount = billDiscountService.insertBillDiscount(dto);
            return JsonBean.success("贴现申请提交成功", discount);
        } catch (BizException e) {
            log.warn("新增票据贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("新增票据贴现失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改票据贴现", notes = "修改票据贴现信息")
    public String update(@Valid @FlexibleRequestBody BillDiscountDTO dto) {
        try {
            if (dto.getDiscountId() == null) {
                return JsonBean.error("贴现ID不能为空");
            }
            TblBillDiscount discount = billDiscountService.updateBillDiscount(dto);
            return JsonBean.success("贴现修改成功", discount);
        } catch (BizException e) {
            log.warn("修改票据贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("修改票据贴现失败", e);
            return JsonBean.error("修改失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除票据贴现", notes = "批量删除票据贴现(逻辑删除)")
    public String delete(@RequestParam(value = "discountIds", required = false) Long[] discountIds) {
        try {
            boolean result = billDiscountService.deleteBillDiscountByIds(discountIds);
            return result ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (BizException e) {
            log.warn("删除票据贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("删除票据贴现失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{discountId}")
    @ApiOperation(value = "审批票据贴现", notes = "审批票据贴现申请")
    public String approve(@PathVariable Long discountId, @RequestParam(required = false) Map<String, Object> approvalData) {
        try {
            boolean result = billDiscountService.approveBillDiscount(discountId, approvalData);
            return result ? JsonBean.success("审批成功") : JsonBean.error("审批失败");
        } catch (BizException e) {
            log.warn("审批票据贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("审批票据贴现失败", e);
            return JsonBean.error("审批失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute/{discountId}")
    @ApiOperation(value = "执行票据贴现", notes = "执行已审批的票据贴现")
    public String execute(@PathVariable Long discountId) {
        try {
            boolean result = billDiscountService.executeBillDiscount(discountId);
            return result ? JsonBean.success("执行成功") : JsonBean.error("执行失败");
        } catch (BizException e) {
            log.warn("执行票据贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("执行票据贴现失败", e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculate-interest")
    @ApiOperation(value = "计算贴现利息", notes = "根据票据金额、贴现利率和贴现天数计算贴现利息和实付金额")
    public String calculateInterest(@RequestParam Map<String, Object> params) {
        try {
            java.math.BigDecimal billAmount = new java.math.BigDecimal(params.get("billAmount").toString());
            java.math.BigDecimal discountRate = new java.math.BigDecimal(params.get("discountRate").toString());
            Integer discountPeriod = Integer.valueOf(params.get("discountPeriod").toString());

            // 贴现利息 = 票据金额 × 贴现利率 × 贴现天数 / 360
            java.math.BigDecimal interest = billAmount
                    .multiply(discountRate)
                    .multiply(new java.math.BigDecimal(discountPeriod))
                    .divide(new java.math.BigDecimal(36000), 2, java.math.RoundingMode.HALF_UP);

            // 实付金额 = 票据金额 - 贴现利息
            java.math.BigDecimal actualAmount = billAmount.subtract(interest);

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("billAmount", billAmount);
            result.put("discountRate", discountRate);
            result.put("discountPeriod", discountPeriod);
            result.put("discountInterest", interest);
            result.put("actualAmount", actualAmount);

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("计算贴现利息失败", e);
            return JsonBean.error("计算失败: " + e.getMessage());
        }
    }

    /**
     * 批量审批贴现申请
     */
    @PostMapping("/batch-approve")
    @ApiOperation(value = "批量审批贴现", notes = "批量审批票据贴现申请")
    public String batchApprove(@RequestParam Map<String, Object> batchData) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<Long> discountIds = (java.util.List<Long>) batchData.get("discountIds");
            String approvalStatus = (String) batchData.get("approvalStatus");
            String approvalComment = (String) batchData.get("approvalComment");

            int successCount = billDiscountService.batchApproveBillDiscount(discountIds, approvalStatus, approvalComment);
            return JsonBean.success("批量审批成功，共处理 " + successCount + " 条记录");
        } catch (BizException e) {
            log.warn("批量审批贴现失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("批量审批贴现失败", e);
            return JsonBean.error("批量审批失败: " + e.getMessage());
        }
    }

    /**
     * 撤销贴现申请
     */
    @PostMapping("/cancel/{discountId}")
    @ApiOperation(value = "撤销贴现申请", notes = "撤销票据贴现申请")
    public String cancel(@PathVariable Long discountId) {
        try {
            boolean result = billDiscountService.cancelBillDiscount(discountId);
            return result ? JsonBean.success("撤销成功") : JsonBean.error("撤销失败");
        } catch (BizException e) {
            log.warn("撤销贴现申请失败: {}", e.getMessage());
            return JsonBean.error(e.getMessage());
        } catch (Exception e) {
            log.error("撤销贴现申请失败", e);
            return JsonBean.error("撤销失败: " + e.getMessage());
        }
    }

    /**
     * 导出贴现数据
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出贴现数据", notes = "导出票据贴现数据到Excel")
    public void export(BillDiscountQueryDTO queryDTO, javax.servlet.http.HttpServletResponse response) {
        try {
            billDiscountService.exportBillDiscount(queryDTO, response);
        } catch (Exception e) {
            log.error("导出贴现数据失败", e);
        }
    }

    /**
     * 获取贴现统计数据
     */
    @PostMapping("/statistics")
    @ApiOperation(value = "获取贴现统计", notes = "获取票据贴现统计数据")
    public String statistics(BillDiscountQueryDTO queryDTO) {
        try {
            Map<String, Object> statistics = billDiscountService.getBillDiscountStatistics(queryDTO);
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取贴现统计数据失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取可贴现票据列表
     */
    @PostMapping("/available-bills")
    @ApiOperation(value = "获取可贴现票据", notes = "获取可用于贴现的票据列表")
    public String getAvailableBills(@RequestParam(required = false) Map<String, Object> params) {
        try {
            java.util.List<Map<String, Object>> bills = billDiscountService.getAvailableBillsForDiscount(params);
            return JsonBean.success(bills);
        } catch (Exception e) {
            log.error("获取可贴现票据列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}

