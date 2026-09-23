package com.management.accountant.oracle.service.budget.impl;

import com.management.accountant.oracle.entity.budget.BudgetAdjustment;
import com.management.accountant.oracle.service.budget.BudgetAdjustmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Adjustment Service实现类 - Oracle包
 *
 * @description Adjustment业务逻辑实现，委托给base包的Service
 * @author AI Assistant
 * @date 2026-02-04
 */
@Service("adjustmentServiceOracle")
public class BudgetAdjustmentServiceImpl implements BudgetAdjustmentService {

    private static final Logger log = LoggerFactory.getLogger(BudgetAdjustmentServiceImpl.class);

    @Resource
    private com.management.accountant.service.BudgetAdjustmentService baseBudgetAdjustmentService;

    @Override
    public Map<String, Object> createBudgetAdjustment(Map<String, Object> params, String companyId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 转换Map参数为BudgetAdjustment实体
            BudgetAdjustment adjustment = convertToBudgetAdjustment(params, companyId, userId);

            // 调用基础Service
            BudgetAdjustment created = baseBudgetAdjustmentService.create(adjustment);

            result.put("success", true);
            result.put("message", "创建预算调整申请成功");
            result.put("data", created);
        } catch (Exception e) {
            log.error("创建预算调整申请失败", e);
            result.put("success", false);
            result.put("message", "创建预算调整申请失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 将Map参数转换为BudgetAdjustment实体
     */
    private BudgetAdjustment convertToBudgetAdjustment(Map<String, Object> params, String companyId, String userId) {
        BudgetAdjustment adjustment = new BudgetAdjustment();

        // 基本信息
        if (params.get("adjustmentCode") != null) {
            adjustment.setAdjustmentCode((String) params.get("adjustmentCode"));
        }
        if (params.get("adjustmentType") != null) {
            adjustment.setAdjustmentType((String) params.get("adjustmentType"));
        }
        if (params.get("adjustmentAmount") != null) {
            adjustment.setAdjustmentAmount(new BigDecimal(params.get("adjustmentAmount").toString()));
        }

        // 预算相关信息
        if (params.get("originalBudgetId") != null) {
            adjustment.setOriginalBudgetId((String) params.get("originalBudgetId"));
        }

        // 申请人信息
        if (params.get("applicantId") != null) {
            adjustment.setApplicantId((String) params.get("applicantId"));
        }
        if (params.get("applicantName") != null) {
            adjustment.setApplicantName((String) params.get("applicantName"));
        }
        if (StringUtils.hasText(userId)) {
            adjustment.setApplicantId(userId);
        }

        // 调整原因
        if (params.get("adjustmentReason") != null) {
            adjustment.setAdjustmentReason((String) params.get("adjustmentReason"));
        }

        // 备注
        if (params.get("remark") != null) {
            adjustment.setRemark((String) params.get("remark"));
        }

        // 租户信息
        if (StringUtils.hasText(companyId)) {
            adjustment.setTenantId(companyId);
        }

        // 审批意见
        if (params.get("approveComment") != null) {
            adjustment.setApproveComment((String) params.get("approveComment"));
        }

        // 附件
        if (params.get("attachments") != null) {
            adjustment.setAttachments((String) params.get("attachments"));
        }

        return adjustment;
    }
}
