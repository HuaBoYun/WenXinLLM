package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.budgetPlanning.dto.BudgetAdjustmentQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetAdjustment;
import com.financial.sharing.budgetPlanning.entity.TblBudgetAdjustmentDetail;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import com.financial.sharing.budgetPlanning.mapper.BudgetAdjustmentDetailMapper;
import com.financial.sharing.budgetPlanning.mapper.BudgetAdjustmentMapper;
import com.financial.sharing.budgetPlanning.mapper.BudgetDataMapper;
import com.financial.sharing.budgetPlanning.service.BudgetAdjustmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 预算调整Service实现类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Service
public class BudgetAdjustmentServiceImpl implements BudgetAdjustmentService {

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(BudgetAdjustmentServiceImpl.class);

    @Autowired
    private BudgetAdjustmentMapper adjustmentMapper;

    @Autowired
    private BudgetAdjustmentDetailMapper detailMapper;

    @Autowired
    private BudgetDataMapper dataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAdjustment(TblBudgetAdjustment adjustment) {
        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 生成调整单号
        String adjustmentNo = adjustmentMapper.generateAdjustmentNo(orgId);
        if (adjustmentNo == null || adjustmentNo.isEmpty()) {
            adjustmentNo = "ADJ" + System.currentTimeMillis();
        }

        // 设置主表信息
        adjustment.setAdjustmentId(UUID.randomUUID().toString().replace("-", ""));
        adjustment.setAdjustmentNo(adjustmentNo);
        adjustment.setStatus("DRAFT");
        adjustment.setOrgId(orgId);
        adjustment.setCreateUser(userId);
        adjustment.setCreateTime(now);
        adjustment.setUpdateUser(userId);
        adjustment.setUpdateTime(now);

        // 插入主表
        adjustmentMapper.insert(adjustment);

        // 插入明细
        if (adjustment.getDetailList() != null && !adjustment.getDetailList().isEmpty()) {
            for (TblBudgetAdjustmentDetail detail : adjustment.getDetailList()) {
                detail.setDetailId(UUID.randomUUID().toString().replace("-", ""));
                detail.setAdjustmentId(adjustment.getAdjustmentId());
                detail.setOrgId(orgId);
                detail.setCreateUser(userId);
                detail.setCreateTime(now);
                detail.setUpdateUser(userId);
                detail.setUpdateTime(now);

                // 计算调整后值
                if (detail.getOriginalValue() != null && detail.getAdjustmentValue() != null) {
                    detail.setAdjustedValue(detail.getOriginalValue().add(detail.getAdjustmentValue()));
                }
            }
            detailMapper.batchInsert(adjustment.getDetailList());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdjustment(TblBudgetAdjustment adjustment) {
        TblBudgetAdjustment existing = adjustmentMapper.selectById(adjustment.getAdjustmentId());
        if (existing == null) {
            throw new RuntimeException("调整单不存在");
        }

        // 只有草稿状态才能修改
        if (!"DRAFT".equals(existing.getStatus())) {
            throw new RuntimeException("只有草稿状态的调整单才能修改");
        }

        String orgId = UserUtils.requireOrgId();
        if (!orgId.equals(existing.getOrgId())) {
            throw new RuntimeException("无权限修改该调整单");
        }

        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 更新主表
        adjustment.setUpdateUser(userId);
        adjustment.setUpdateTime(now);
        adjustmentMapper.updateById(adjustment);

        // 删除旧明细
        detailMapper.deleteByAdjustmentId(adjustment.getAdjustmentId());

        // 插入新明细
        if (adjustment.getDetailList() != null && !adjustment.getDetailList().isEmpty()) {
            for (TblBudgetAdjustmentDetail detail : adjustment.getDetailList()) {
                detail.setDetailId(UUID.randomUUID().toString().replace("-", ""));
                detail.setAdjustmentId(adjustment.getAdjustmentId());
                detail.setOrgId(orgId);
                detail.setCreateUser(userId);
                detail.setCreateTime(now);
                detail.setUpdateUser(userId);
                detail.setUpdateTime(now);

                // 计算调整后值
                if (detail.getOriginalValue() != null && detail.getAdjustmentValue() != null) {
                    detail.setAdjustedValue(detail.getOriginalValue().add(detail.getAdjustmentValue()));
                }
            }
            detailMapper.batchInsert(adjustment.getDetailList());
        }
    }

    @Override
    public PageInfo<TblBudgetAdjustment> getAdjustmentList(BudgetAdjustmentQueryParam param) {
        param.setOrgId(UserUtils.requireOrgId());
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetAdjustment> list = adjustmentMapper.selectAdjustmentList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblBudgetAdjustment getAdjustmentById(String adjustmentId) {
        return adjustmentMapper.selectById(adjustmentId);
    }

    @Override
    public TblBudgetAdjustment getAdjustmentWithDetails(String adjustmentId) {
        return adjustmentMapper.selectAdjustmentWithDetails(adjustmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAdjustment(String adjustmentId) {
        TblBudgetAdjustment adjustment = adjustmentMapper.selectById(adjustmentId);
        if (adjustment == null) {
            throw new RuntimeException("调整单不存在");
        }

        // 只有草稿状态才能删除
        if (!"DRAFT".equals(adjustment.getStatus())) {
            throw new RuntimeException("只有草稿状态的调整单才能删除");
        }

        String orgId = UserUtils.requireOrgId();
        if (!orgId.equals(adjustment.getOrgId())) {
            throw new RuntimeException("无权限删除该调整单");
        }

        // 删除明细
        detailMapper.deleteByAdjustmentId(adjustmentId);

        // 删除主表
        adjustmentMapper.deleteById(adjustmentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAdjustment(List<String> adjustmentIds) {
        for (String adjustmentId : adjustmentIds) {
            deleteAdjustment(adjustmentId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAdjustment(String adjustmentId) {
        TblBudgetAdjustment adjustment = adjustmentMapper.selectById(adjustmentId);
        if (adjustment == null) {
            throw new RuntimeException("调整单不存在");
        }

        // 只有草稿状态才能提交
        if (!"DRAFT".equals(adjustment.getStatus())) {
            throw new RuntimeException("只有草稿状态的调整单才能提交");
        }

        String orgId = UserUtils.requireOrgId();
        if (!orgId.equals(adjustment.getOrgId())) {
            throw new RuntimeException("无权限提交该调整单");
        }

        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 更新状态
        adjustment.setStatus("SUBMITTED");
        adjustment.setSubmitUser(userId);
        adjustment.setSubmitTime(now);
        adjustment.setUpdateUser(userId);
        adjustment.setUpdateTime(now);

        adjustmentMapper.updateById(adjustment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveAdjustment(String adjustmentId, boolean approved, String opinion) {
        TblBudgetAdjustment adjustment = adjustmentMapper.selectById(adjustmentId);
        if (adjustment == null) {
            throw new RuntimeException("调整单不存在");
        }

        // 只有已提交状态才能审批
        if (!"SUBMITTED".equals(adjustment.getStatus())) {
            throw new RuntimeException("只有已提交状态的调整单才能审批");
        }

        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 更新状态
        adjustment.setStatus(approved ? "APPROVED" : "REJECTED");
        adjustment.setApproveUser(userId);
        adjustment.setApproveTime(now);
        adjustment.setApproveOpinion(opinion);
        adjustment.setUpdateUser(userId);
        adjustment.setUpdateTime(now);

        adjustmentMapper.updateById(adjustment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeAdjustment(String adjustmentId) {
        TblBudgetAdjustment adjustment = adjustmentMapper.selectById(adjustmentId);
        if (adjustment == null) {
            throw new RuntimeException("调整单不存在");
        }

        // 只有已审批状态才能执行
        if (!"APPROVED".equals(adjustment.getStatus())) {
            throw new RuntimeException("只有已审批状态的调整单才能执行");
        }

        String userId = UserUtils.requireUserId();
        Date now = new Date();

        // 查询明细
        List<TblBudgetAdjustmentDetail> detailList = detailMapper.selectByAdjustmentId(adjustmentId);
        if (detailList == null || detailList.isEmpty()) {
            throw new RuntimeException("调整单明细为空");
        }

        // 更新预算数据
        for (TblBudgetAdjustmentDetail detail : detailList) {
            if (detail.getDataId() != null && !detail.getDataId().isEmpty()) {
                TblBudgetData budgetData = dataMapper.selectById(detail.getDataId());
                if (budgetData != null) {
                    // TODO: 更新预算数据的具体字段
                    // 这里需要根据实际的预算数据结构来更新
                    // 暂时跳过实际更新逻辑
                }
            }
        }

        // 更新状态
        adjustment.setStatus("EXECUTED");
        adjustment.setExecuteUser(userId);
        adjustment.setExecuteTime(now);
        adjustment.setUpdateUser(userId);
        adjustment.setUpdateTime(now);

        adjustmentMapper.updateById(adjustment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawAdjustment(String adjustmentId) {
        TblBudgetAdjustment adjustment = adjustmentMapper.selectById(adjustmentId);
        if (adjustment == null) {
            throw new RuntimeException("调整单不存在");
        }

        // 只有已提交状态才能撤销
        if (!"SUBMITTED".equals(adjustment.getStatus())) {
            throw new RuntimeException("只有已提交状态的调整单才能撤销");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.requireUserId();
        if (!orgId.equals(adjustment.getOrgId()) || !userId.equals(adjustment.getSubmitUser())) {
            throw new RuntimeException("只有提交人才能撤销该调整单");
        }

        Date now = new Date();

        // 更新状态
        adjustment.setStatus("DRAFT");
        adjustment.setUpdateUser(userId);
        adjustment.setUpdateTime(now);

        adjustmentMapper.updateById(adjustment);
    }

    @Override
    public Map<String, Object> getAdjustmentStatistics() {
        String orgId = UserUtils.requireOrgId();
        Map<String, Object> raw = adjustmentMapper.selectAdjustmentStatistics(orgId);
        log.info("[BudgetAdjustment.statistics] orgId={}, raw={}", orgId, raw);
        // 关键: 达梦驱动可能把别名转成大写, 这里做大小写归一化, 同名取非零值优先
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("totalCount", 0);
        result.put("draftCount", 0);
        result.put("submittedCount", 0);
        result.put("approvedCount", 0);
        result.put("rejectedCount", 0);
        result.put("executedCount", 0);
        if (raw != null) {
            for (Map.Entry<String, Object> entry : raw.entrySet()) {
                String camelKey = toCamelLower(entry.getKey());
                Object val = entry.getValue();
                if (val == null) {
                    continue;
                }
                Object existed = result.get(camelKey);
                if (existed == null || isZeroLike(existed)) {
                    result.put(camelKey, val);
                }
            }
        }
        return result;
    }

    /** "TOTALCOUNT" / "totalCount" / "totalcount" 全部归一化成 "totalCount" 等已知 key */
    private static String toCamelLower(String key) {
        if (key == null) return "";
        String low = key.toLowerCase();
        switch (low) {
            case "totalcount":     return "totalCount";
            case "draftcount":     return "draftCount";
            case "submittedcount": return "submittedCount";
            case "approvedcount":  return "approvedCount";
            case "rejectedcount":  return "rejectedCount";
            case "executedcount":  return "executedCount";
            default: return key;
        }
    }

    private static boolean isZeroLike(Object v) {
        if (v == null) return true;
        if (v instanceof Number) return ((Number) v).longValue() == 0L;
        String s = v.toString();
        return s.isEmpty() || "0".equals(s);
    }
}

