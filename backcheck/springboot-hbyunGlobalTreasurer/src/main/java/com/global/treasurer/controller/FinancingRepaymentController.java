package com.global.treasurer.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingRepaymentDTO;
import com.global.treasurer.dto.FinancingRepaymentQueryDTO;
import com.global.treasurer.entity.TblFinancingRepayment;
import com.global.treasurer.mapper.FinancingRepaymentMapper;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 融资还款管理Controller
 * 提供融资还款计划的CRUD操作和业务功能接口
 *
 * @author 华博云开发团队
 * @since 2025-02-05
 */
@RestController
@RequestMapping("/financial/rzgl/financing-repayment")
@Api(tags = "融资还款管理")
public class FinancingRepaymentController {

    private static final Logger log = LoggerFactory.getLogger(FinancingRepaymentController.class);

    @Autowired
    private FinancingRepaymentMapper financingRepaymentMapper;

    /**
     * 分页查询还款列表
     */
    @PostMapping("/list")
    @ApiOperation("分页查询还款列表")
    public String getRepaymentList(FinancingRepaymentQueryDTO queryDTO) {
        try {
            log.info("分页查询还款列表, queryDTO: {}", queryDTO);
            if (queryDTO == null) {
                queryDTO = new FinancingRepaymentQueryDTO();
            }
            int pageNum = queryDTO.getPage() != null ? queryDTO.getPage() : 1;
            int pageSize = queryDTO.getLimit() != null ? queryDTO.getLimit() : 10;
            
            PageHelper.startPage(pageNum, pageSize);
            Map<String, Object> params = new HashMap<>();
            params.put("repaymentStatus", queryDTO.getRepaymentStatus());
            params.put("financingType", queryDTO.getFinancingType());
            
            List<TblFinancingRepayment> list = financingRepaymentMapper.selectRepaymentList(params);
            PageInfo<TblFinancingRepayment> pageInfo = new PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("分页查询还款列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 获取还款详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("获取还款详情")
    public String getRepaymentDetail(@PathVariable Long id) {
        try {
            log.info("获取还款详情, id: {}", id);
            TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(id);
            if (repayment != null) {
                return new JsonBean(1, "查询成功", repayment).toString();
            } else {
                return new JsonBean(0, "未找到对应的还款记录", null).toString();
            }
        } catch (Exception e) {
            log.error("获取还款详情失败, id: {}", id, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 创建还款计划
     */
    @PostMapping("/create")
    @ApiOperation("创建还款计划")
    public String createRepayment(@FlexibleRequestBody FinancingRepaymentDTO dto) {
        try {
            log.info("创建还款计划, dto: {}", dto);
            TblFinancingRepayment repayment = new TblFinancingRepayment();

            // 手动映射字段（避免类型不匹配问题）
            repayment.setFinancingId(dto.getFinancingId());
            repayment.setDrawdownId(dto.getDrawdownId());
            repayment.setDrawdownNo(dto.getDrawdownNo());
            repayment.setFinancingType(dto.getFinancingType());
            repayment.setRepaymentType(dto.getRepaymentType());
            repayment.setRepaymentAmount(dto.getRepaymentAmount());
            repayment.setActualAmount(dto.getActualAmount());
            repayment.setTotalAmount(dto.getTotalAmount());
            repayment.setCurrencyCode(dto.getCurrencyCode());
            repayment.setRepaymentStatus(dto.getRepaymentStatus());
            repayment.setPrincipalAmount(dto.getPrincipalAmount());
            repayment.setInterestAmount(dto.getInterestAmount());
            repayment.setPenaltyAmount(dto.getPenaltyAmount());
            repayment.setPaymentMethod(dto.getPaymentMethod());
            repayment.setPaymentAccount(dto.getPaymentAccount());
            repayment.setCompanyId(dto.getCompanyId());
            repayment.setCompanyName(dto.getCompanyName());
            repayment.setRemark(dto.getRemark());

            // 日期转换（兼容时间戳和字符串格式）
            if (dto.getPlanDate() != null && !dto.getPlanDate().isEmpty()) {
                repayment.setPlanDate(parseDateFlexible(dto.getPlanDate()));
            }
            if (dto.getActualRepaymentDate() != null && !dto.getActualRepaymentDate().isEmpty()) {
                repayment.setActualRepaymentDate(parseDateFlexible(dto.getActualRepaymentDate()));
            }

            // 生成还款编号
            repayment.setRepaymentNo("HK" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + String.format("%04d", (int)(Math.random() * 10000)));
            if (repayment.getRepaymentStatus() == null || repayment.getRepaymentStatus().isEmpty()) {
                repayment.setRepaymentStatus("PENDING");
            }
            repayment.setDeleteFlag(0);
            repayment.setCreatedTime(new Date());

            financingRepaymentMapper.insert(repayment);
            return new JsonBean(1, "创建成功", repayment).toString();
        } catch (Exception e) {
            log.error("创建还款计划失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 更新还款计划
     */
    @PutMapping("/update")
    @ApiOperation("更新还款计划")
    public String updateRepayment(@FlexibleRequestBody FinancingRepaymentDTO dto) {
        try {
            log.info("更新还款计划, dto: {}", dto);
            if (dto.getRepaymentId() == null) {
                return new JsonBean(0, "还款ID不能为空", null).toString();
            }
            TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(dto.getRepaymentId());
            if (repayment == null) {
                return new JsonBean(0, "还款记录不存在", null).toString();
            }

            // 手动映射字段
            if (dto.getFinancingType() != null) repayment.setFinancingType(dto.getFinancingType());
            if (dto.getRepaymentType() != null) repayment.setRepaymentType(dto.getRepaymentType());
            if (dto.getRepaymentAmount() != null) repayment.setRepaymentAmount(dto.getRepaymentAmount());
            if (dto.getActualAmount() != null) repayment.setActualAmount(dto.getActualAmount());
            if (dto.getTotalAmount() != null) repayment.setTotalAmount(dto.getTotalAmount());
            if (dto.getCurrencyCode() != null) repayment.setCurrencyCode(dto.getCurrencyCode());
            if (dto.getRepaymentStatus() != null) repayment.setRepaymentStatus(dto.getRepaymentStatus());
            if (dto.getPrincipalAmount() != null) repayment.setPrincipalAmount(dto.getPrincipalAmount());
            if (dto.getInterestAmount() != null) repayment.setInterestAmount(dto.getInterestAmount());
            if (dto.getPenaltyAmount() != null) repayment.setPenaltyAmount(dto.getPenaltyAmount());
            if (dto.getPaymentMethod() != null) repayment.setPaymentMethod(dto.getPaymentMethod());
            if (dto.getPaymentAccount() != null) repayment.setPaymentAccount(dto.getPaymentAccount());
            if (dto.getCompanyId() != null) repayment.setCompanyId(dto.getCompanyId());
            if (dto.getCompanyName() != null) repayment.setCompanyName(dto.getCompanyName());
            if (dto.getRemark() != null) repayment.setRemark(dto.getRemark());

            // 日期转换（兼容时间戳和字符串格式）
            if (dto.getPlanDate() != null && !dto.getPlanDate().isEmpty()) {
                repayment.setPlanDate(parseDateFlexible(dto.getPlanDate()));
            }
            if (dto.getActualRepaymentDate() != null && !dto.getActualRepaymentDate().isEmpty()) {
                repayment.setActualRepaymentDate(parseDateFlexible(dto.getActualRepaymentDate()));
            }

            repayment.setUpdatedTime(new Date());
            financingRepaymentMapper.updateById(repayment);
            return new JsonBean(1, "更新成功", repayment).toString();
        } catch (Exception e) {
            log.error("更新还款计划失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 执行还款
     */
    @PostMapping("/execute")
    @ApiOperation("执行还款")
    public String executeRepayment(@RequestParam Map<String, Object> params) {
        try {
            Long repaymentId = Long.valueOf(params.get("repaymentId").toString());
            BigDecimal actualAmount = new BigDecimal(params.get("actualAmount").toString());
            log.info("执行还款, repaymentId: {}, actualAmount: {}", repaymentId, actualAmount);

            TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(repaymentId);
            if (repayment == null) {
                return new JsonBean(0, "还款记录不存在", null).toString();
            }

            repayment.setActualAmount(actualAmount);
            repayment.setActualRepaymentDate(new Date());
            repayment.setRepaymentStatus("COMPLETED");
            repayment.setUpdatedTime(new Date());
            financingRepaymentMapper.updateById(repayment);

            return new JsonBean(1, "还款成功", repayment).toString();
        } catch (Exception e) {
            log.error("执行还款失败", e);
            return new JsonBean(0, "还款失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 部分还款
     */
    @PostMapping("/partial")
    @ApiOperation("部分还款")
    public String partialRepayment(@RequestParam Map<String, Object> params) {
        try {
            Long repaymentId = Long.valueOf(params.get("repaymentId").toString());
            BigDecimal partialAmount = new BigDecimal(params.get("partialAmount").toString());
            log.info("部分还款, repaymentId: {}, partialAmount: {}", repaymentId, partialAmount);

            TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(repaymentId);
            if (repayment == null) {
                return new JsonBean(0, "还款记录不存在", null).toString();
            }

            BigDecimal currentActual = repayment.getActualAmount() != null ? repayment.getActualAmount() : BigDecimal.ZERO;
            repayment.setActualAmount(currentActual.add(partialAmount));
            repayment.setRepaymentStatus("PARTIAL");
            repayment.setUpdatedTime(new Date());
            financingRepaymentMapper.updateById(repayment);

            return new JsonBean(1, "部分还款成功", repayment).toString();
        } catch (Exception e) {
            log.error("部分还款失败", e);
            return new JsonBean(0, "部分还款失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 还款历史
     */
    @PostMapping("/history")
    @ApiOperation("还款历史")
    public String getRepaymentHistory(@RequestParam Map<String, Object> params) {
        try {
            Long financingId = params.get("financingId") != null ? Long.valueOf(params.get("financingId").toString()) : null;
            log.info("查询还款历史, financingId: {}", financingId);

            List<TblFinancingRepayment> list = financingRepaymentMapper.selectRepaymentsByFinancingId(financingId);
            return new JsonBean(1, "查询成功", list).toString();
        } catch (Exception e) {
            log.error("查询还款历史失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 展期申请
     */
    @PostMapping("/extension")
    @ApiOperation("展期申请")
    public String applyExtension(@RequestParam Map<String, Object> params) {
        try {
            Long repaymentId = Long.valueOf(params.get("repaymentId").toString());
            String reason = params.get("reason") != null ? params.get("reason").toString() : "";

            TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(repaymentId);
            if (repayment == null) {
                return new JsonBean(0, "还款记录不存在", null).toString();
            }

            // 支持两种方式：newPlanDate（新日期字符串）或 extensionDays（展期天数）
            Date newDate = null;
            if (params.get("newPlanDate") != null) {
                // 方式1：直接传入新的计划日期
                String newPlanDate = params.get("newPlanDate").toString();
                log.info("展期申请, repaymentId: {}, newPlanDate: {}, reason: {}", repaymentId, newPlanDate, reason);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                newDate = sdf.parse(newPlanDate);
            } else if (params.get("extensionDays") != null) {
                // 方式2：传入展期天数
                Integer extensionDays = Integer.valueOf(params.get("extensionDays").toString());
                log.info("展期申请, repaymentId: {}, extensionDays: {}, reason: {}", repaymentId, extensionDays, reason);
                Calendar cal = Calendar.getInstance();
                cal.setTime(repayment.getPlanDate() != null ? repayment.getPlanDate() : new Date());
                cal.add(Calendar.DAY_OF_MONTH, extensionDays);
                newDate = cal.getTime();
            } else {
                return new JsonBean(0, "请提供新的计划日期或展期天数", null).toString();
            }

            // 更新计划还款日期
            repayment.setPlanDate(newDate);
            String remarkPrefix = repayment.getRemark() != null ? repayment.getRemark() + "; " : "";
            repayment.setRemark(remarkPrefix + "展期原因: " + reason);
            repayment.setUpdatedTime(new Date());
            financingRepaymentMapper.updateById(repayment);

            return new JsonBean(1, "展期申请成功", repayment).toString();
        } catch (Exception e) {
            log.error("展期申请失败", e);
            return new JsonBean(0, "展期申请失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 即将到期还款列表
     */
    @PostMapping("/upcoming")
    @ApiOperation("即将到期还款列表")
    public String getUpcomingRepayments(@RequestParam(required = false) Map<String, Object> params) {
        try {
            Integer days = 7;
            if (params != null && params.get("days") != null) {
                days = Integer.valueOf(params.get("days").toString());
            }
            log.info("查询即将到期还款, days: {}", days);

            List<TblFinancingRepayment> list = financingRepaymentMapper.selectUpcomingRepayments(days);
            return new JsonBean(1, "查询成功", list).toString();
        } catch (Exception e) {
            log.error("查询即将到期还款失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 逾期还款列表
     */
    @PostMapping("/overdue")
    @ApiOperation("逾期还款列表")
    public String getOverdueRepayments(@RequestParam(required = false) Map<String, Object> params) {
        try {
            log.info("查询逾期还款列表");
            List<TblFinancingRepayment> list = financingRepaymentMapper.selectOverdueRepayments();
            return new JsonBean(1, "查询成功", list).toString();
        } catch (Exception e) {
            log.error("查询逾期还款列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 还款统计分析
     */
    @PostMapping("/statistics")
    @ApiOperation("还款统计分析")
    public String getRepaymentStatistics(@RequestParam(required = false) Map<String, Object> params) {
        try {
            Long companyId = null;
            if (params != null && params.get("companyId") != null) {
                companyId = Long.valueOf(params.get("companyId").toString());
            }
            log.info("查询还款统计, companyId: {}", companyId);

            Map<String, Object> summary = financingRepaymentMapper.selectRepaymentSummary(companyId);
            if (summary == null) {
                summary = new HashMap<>();
                summary.put("totalCount", 0);
                summary.put("totalAmount", BigDecimal.ZERO);
                summary.put("completedAmount", BigDecimal.ZERO);
                summary.put("pendingAmount", BigDecimal.ZERO);
            }
            return new JsonBean(1, "查询成功", summary).toString();
        } catch (Exception e) {
            log.error("查询还款统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 删除还款记录
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除还款记录")
    public String deleteRepayment(@PathVariable Long id) {
        try {
            log.info("删除还款记录, id: {}", id);
            TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(id);
            if (repayment == null) {
                return new JsonBean(0, "还款记录不存在", null).toString();
            }
            repayment.setDeleteFlag(1);
            repayment.setUpdatedTime(new Date());
            financingRepaymentMapper.updateById(repayment);
            return new JsonBean(1, "删除成功").toString();
        } catch (Exception e) {
            log.error("删除还款记录失败, id: {}", id, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 获取还款凭证信息
     */
    @PostMapping("/receipt/{id}")
    @ApiOperation("获取还款凭证")
    public String getRepaymentReceipt(@PathVariable Long id) {
        try {
            log.info("获取还款凭证, id: {}", id);
            TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(id);
            if (repayment == null) {
                return new JsonBean(0, "还款记录不存在", null).toString();
            }

            // 构建凭证信息
            Map<String, Object> receipt = new HashMap<>();
            receipt.put("repaymentId", repayment.getRepaymentId());
            receipt.put("repaymentNo", repayment.getRepaymentNo());
            receipt.put("companyName", repayment.getCompanyName());
            receipt.put("financingType", repayment.getFinancingType());
            receipt.put("repaymentType", repayment.getRepaymentType());
            receipt.put("repaymentAmount", repayment.getRepaymentAmount());
            receipt.put("actualAmount", repayment.getActualAmount());
            receipt.put("planDate", repayment.getPlanDate());
            receipt.put("actualRepaymentDate", repayment.getActualRepaymentDate());
            receipt.put("repaymentStatus", repayment.getRepaymentStatus());
            receipt.put("paymentMethod", repayment.getPaymentMethod());
            receipt.put("paymentAccount", repayment.getPaymentAccount());
            receipt.put("remark", repayment.getRemark());
            // 凭证编号（可以是还款编号+时间戳）
            receipt.put("receiptNo", "PZ" + repayment.getRepaymentNo().substring(2));
            receipt.put("generateTime", new Date());

            return new JsonBean(1, "查询成功", receipt).toString();
        } catch (Exception e) {
            log.error("获取还款凭证失败, id: {}", id, e);
            return new JsonBean(0, "获取凭证失败: " + e.getMessage(), null).toString();
        }
    }

    /**
     * 灵活解析日期（支持时间戳和字符串格式）
     * @param dateStr 日期字符串，可以是时间戳（毫秒）或 yyyy-MM-dd 格式
     * @return Date 对象
     */
    private Date parseDateFlexible(String dateStr) throws Exception {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        // 尝试解析为时间戳（纯数字）
        if (dateStr.matches("^\\d+$")) {
            long timestamp = Long.parseLong(dateStr);
            return new Date(timestamp);
        }
        // 尝试解析为日期字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }
}