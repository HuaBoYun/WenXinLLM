package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.global.treasurer.entity.TblPendingData;
import com.global.treasurer.mapper.TblPendingDataMapper;
import com.global.treasurer.service.PendingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 待结算数据服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Service
public class PendingDataServiceImpl implements PendingDataService {
    @Autowired
    private TblPendingDataMapper pendingDataMapper;

    @Override
    public Map<String, Object> getPendingDataPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNum 和 pageSize
        Integer page = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) : 1);
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblPendingData> list = pendingDataMapper.selectPendingDataPage(params);
        int total = pendingDataMapper.countPendingDataList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblPendingData getPendingDataById(Long pendingId) {
        return pendingDataMapper.selectById(pendingId);
    }

    @Override
    public int createPendingData(TblPendingData pendingData) {
        pendingData.setBusinessNo(generateBusinessNo());
        pendingData.setSettlementStatus("PENDING");
        pendingData.setApprovalStatus("DRAFT");
        pendingData.setDeleteFlag(0);
        pendingData.setRetryCount(0);
        pendingData.setCreatedTime(new Date());
        return pendingDataMapper.insert(pendingData);
    }

    @Override
    public int updatePendingData(TblPendingData pendingData) {
        pendingData.setUpdatedTime(new Date());
        return pendingDataMapper.updateById(pendingData);
    }

    @Override
    public int deletePendingData(List<Long> pendingIds) {
        if (pendingIds == null || pendingIds.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Long pendingId : pendingIds) {
            TblPendingData pendingData = getPendingDataById(pendingId);
            if (pendingData != null) {
                // 只能删除草稿或已拒绝状态的数据
                if ("DRAFT".equals(pendingData.getApprovalStatus()) || "REJECTED".equals(pendingData.getApprovalStatus())) {
                    pendingData.setDeleteFlag(1);
                    pendingData.setUpdatedTime(new Date());
                    count += pendingDataMapper.updateById(pendingData);
                }
            }
        }
        return count;
    }

    @Override
    public int batchUpdateStatus(List<Long> ids, String status) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return pendingDataMapper.batchUpdateStatus(ids, status);
    }

    @Override
    public int approvePendingData(Long pendingId, String approvalStatus, Long approvalBy, String approvalOpinion) {
        TblPendingData pendingData = getPendingDataById(pendingId);
        if (pendingData == null) {
            return 0;
        }

        if (!"PENDING".equals(pendingData.getApprovalStatus())) {
            throw new RuntimeException("只能审批待审批状态的数据");
        }

        return pendingDataMapper.updateApprovalStatus(pendingId, approvalStatus, approvalBy, approvalOpinion);
    }

    @Override
    public List<TblPendingData> getHighPriorityPending(Long orgId) {
        return pendingDataMapper.selectHighPriorityPending(orgId);
    }

    @Override
    public List<TblPendingData> getOverduePending(Long orgId) {
        return pendingDataMapper.selectOverduePending(orgId);
    }

    @Override
    public List<TblPendingData> getLargeAmountPending(Map<String, Object> params) {
        return pendingDataMapper.selectLargeAmountPending(params);
    }

    @Override
    public Map<String, Object> getPendingDataSummary(Long orgId) {
        return pendingDataMapper.selectPendingDataSummary(orgId);
    }

    @Override
    public Map<String, Object> assessPendingDataRisk(Long pendingId) {
        Map<String, Object> result = new HashMap<>();
        TblPendingData pendingData = getPendingDataById(pendingId);

        if (pendingData == null) {
            return result;
        }

        // 风险评估逻辑
        int riskScore = 0;
        String riskLevel = "LOW";
        List<String> riskFactors = new ArrayList<>();

        // 金额风险评估
        if (pendingData.getSettlementAmount() != null) {
            if (pendingData.getSettlementAmount().compareTo(new java.math.BigDecimal("1000000")) > 0) {
                riskScore += 30;
                riskFactors.add("大额交易");
            } else if (pendingData.getSettlementAmount().compareTo(new java.math.BigDecimal("100000")) > 0) {
                riskScore += 10;
            }
        }

        // 优先级风险评估
        if ("HIGH".equals(pendingData.getPriority())) {
            riskScore += 20;
            riskFactors.add("高优先级");
        }

        // 逾期风险评估
        if (pendingData.getExpectedSettlementDate() != null &&
            pendingData.getExpectedSettlementDate().before(new Date())) {
            riskScore += 40;
            riskFactors.add("已逾期");
        }

        // 重试次数风险评估
        if (pendingData.getRetryCount() != null && pendingData.getRetryCount() > 3) {
            riskScore += 20;
            riskFactors.add("多次失败");
        }

        // 确定风险等级
        if (riskScore >= 70) {
            riskLevel = "HIGH";
        } else if (riskScore >= 40) {
            riskLevel = "MEDIUM";
        }

        result.put("riskScore", riskScore);
        result.put("riskLevel", riskLevel);
        result.put("riskFactors", riskFactors);
        result.put("suggestions", getSuggestions(riskLevel));

        return result;
    }

    /**
     * 生成业务编号
     */
    private String generateBusinessNo() {
        return "PEND-" + System.currentTimeMillis();
    }

    /**
     * 根据风险等级获取建议
     */
    private List<String> getSuggestions(String riskLevel) {
        List<String> suggestions = new ArrayList<>();
        if ("HIGH".equals(riskLevel)) {
            suggestions.add("建议立即人工审核");
            suggestions.add("建议加强监控");
            suggestions.add("准备应急预案");
        } else if ("MEDIUM".equals(riskLevel)) {
            suggestions.add("建议优先处理");
            suggestions.add("关注处理进度");
        } else {
            suggestions.add("正常处理");
        }
        return suggestions;
    }

    /**
     * 导出待结算数据
     */
    @Override
    public List<TblPendingData> exportPendingData(Map<String, Object> params) {
        QueryWrapper<TblPendingData> wrapper = new QueryWrapper<>();

        // 构建查询条件
        if (params != null) {
            String businessType = (String) params.get("businessType");
            if (businessType != null && !"".equals(businessType)) {
                wrapper.eq("BUSINESS_TYPE", businessType);
            }

            String settlementStatus = (String) params.get("settlementStatus");
            if (settlementStatus != null && !"".equals(settlementStatus)) {
                wrapper.eq("SETTLEMENT_STATUS", settlementStatus);
            }

            String priority = (String) params.get("priority");
            if (priority != null && !"".equals(priority)) {
                wrapper.eq("PRIORITY", priority);
            }

            String currencyCode = (String) params.get("currencyCode");
            if (currencyCode != null && !"".equals(currencyCode)) {
                wrapper.eq("CURRENCY_CODE", currencyCode);
            }

            // 日期范围查询
            String beginTime = (String) params.get("beginTime");
            String endTime = (String) params.get("endTime");
            if (beginTime != null && !"".equals(beginTime)) {
                wrapper.ge("CREATE_TIME", beginTime + " 00:00:00");
            }
            if (endTime != null && !"".equals(endTime)) {
                wrapper.le("CREATE_TIME", endTime + " 23:59:59");
            }
        }

        // 只查询未删除的数据
        wrapper.eq("DELETE_FLAG", 0);
        wrapper.orderByDesc("CREATED_TIME");

        return pendingDataMapper.selectList(wrapper);
    }
}
