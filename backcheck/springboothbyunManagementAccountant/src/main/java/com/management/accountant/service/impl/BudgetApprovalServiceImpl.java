package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetApproval;
import com.management.accountant.oracle.mapper.budget.BudgetApprovalMapper;
import com.management.accountant.service.BudgetApprovalService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预算审批流程Service实现类
 * 
 * @description 预算审批流程业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetApprovalServiceImpl implements BudgetApprovalService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_APPROVED = "approved";
    private static final String STATUS_REJECTED = "rejected";
    private static final String STATUS_CANCELLED = "cancelled";


    @Resource
    private BudgetApprovalMapper approvalMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetApproval create(BudgetApproval approval) {
        // 1. 参数校验
        if (approval == null) {
            throw new ServiceException("审批信息不能为空");
        }
        if (!StringUtils.hasText(approval.getBudgetId())) {
            throw new ServiceException("预算ID不能为空");
        }

        // 2. 设置默认值
        if (approval.getApprovalStatus() == null) {
            approval.setApprovalStatus("pending");
        }
        if (!StringUtils.hasText(approval.getSubmitterName())) {
            approval.setSubmitterName("系统用户");
        }
        if (!StringUtils.hasText(approval.getCurrentNode())) {
            approval.setCurrentNode("发起申请");
        }
        if (!StringUtils.hasText(approval.getCurrentApproverName())) {
            approval.setCurrentApproverName("待分配");
        }
        if (approval.getBudgetAmount() == null) {
            approval.setBudgetAmount(java.math.BigDecimal.ZERO);
        }
        approval.setSubmitTime(new Date());
        approval.setCreateTime(new Date());
        approval.setUpdateTime(new Date());

        // 3. 生成审批编码
        if (!StringUtils.hasText(approval.getApprovalCode())) {
            approval.setApprovalCode(generateApprovalCode());
        }

        // 4. 插入数据库
        int result = approvalMapper.insert(approval);
        if (result <= 0) {
            throw new ServiceException("创建审批流程失败");
        }

        log.info("创建审批流程成功，ID: {}", approval.getApprovalId());
        return approval;
    }

    @Override
    public BudgetApproval getById(String approvalId) {
        if (!StringUtils.hasText(approvalId)) {
            throw new ServiceException("审批ID不能为空");
        }
        
        QueryWrapper<BudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("FLOW_ID", approvalId);
        
        return approvalMapper.selectOne(wrapper);
    }

    @Override
    public PageResult<LinkedHashMap<String, Object>> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (hasValue(params.get("flowName"))) {
            wrapper.like("FLOW_NAME", params.get("flowName"));
        }

        Object flowType = hasValue(params.get("flowType")) ? params.get("flowType") : params.get("approvalType");
        if (hasValue(flowType)) {
            wrapper.eq("FLOW_TYPE", flowType);
        }

        Object approvalStatus = hasValue(params.get("approvalStatus")) ? params.get("approvalStatus") : params.get("status");
        if (hasValue(approvalStatus)) {
            wrapper.eq("STATUS", normalizeStatusValue(approvalStatus.toString()));
        }

        if (hasValue(params.get("applicant"))) {
            wrapper.like("INITIATOR_NAME", params.get("applicant"));
        }

        if (hasValue(params.get("submitterId"))) {
            wrapper.eq("INITIATOR", params.get("submitterId"));
        }

        Object applyDateRange = params.get("applyDateRange");
        if (applyDateRange instanceof List) {
            List<?> dateRange = (List<?>) applyDateRange;
            if (dateRange.size() >= 2 && hasValue(dateRange.get(0)) && hasValue(dateRange.get(1))) {
                wrapper.between("INITIATE_TIME", dateRange.get(0), dateRange.get(1));
            }
        }

        wrapper.orderByDesc("INITIATE_TIME");

        Page<BudgetApproval> page = new Page<>(pageNum, pageSize);
        IPage<BudgetApproval> pageResult = approvalMapper.selectPage(page, wrapper);

        List<LinkedHashMap<String, Object>> records = new ArrayList<>();
        for (BudgetApproval approval : pageResult.getRecords()) {
            records.add(toFlowView(approval));
        }

        PageResult<LinkedHashMap<String, Object>> result = new PageResult<>();
        result.setTlist(records);
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(String approvalId, String comment) {
        if (!StringUtils.hasText(approvalId)) {
            throw new ServiceException("审批ID不能为空");
        }

        BudgetApproval approval = getById(approvalId);
        if (approval == null) {
            throw new ServiceException("审批流程不存在");
        }

        if (!STATUS_PENDING.equalsIgnoreCase(approval.getApprovalStatus())) {
            throw new ServiceException("只有待审批状态才能审批");
        }

        BudgetApproval update = new BudgetApproval();
        update.setApprovalId(approvalId);
        update.setApprovalStatus(STATUS_APPROVED);
        update.setApprovalStatus2("APPROVED");
        update.setApprovalComment(comment);
        update.setApprovalTime(new Date());
        update.setUpdateTime(new Date());

        int result = approvalMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("审批通过失败");
        }

        log.info("审批通过成功，ID: {}", approvalId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(String approvalId, String reason) {
        if (!StringUtils.hasText(approvalId)) {
            throw new ServiceException("审批ID不能为空");
        }
        if (!StringUtils.hasText(reason)) {
            throw new ServiceException("拒绝原因不能为空");
        }

        BudgetApproval approval = getById(approvalId);
        if (approval == null) {
            throw new ServiceException("审批流程不存在");
        }

        if (!STATUS_PENDING.equalsIgnoreCase(approval.getApprovalStatus())) {
            throw new ServiceException("只有待审批状态才能拒绝");
        }

        BudgetApproval update = new BudgetApproval();
        update.setApprovalId(approvalId);
        update.setApprovalStatus(STATUS_REJECTED);
        update.setApprovalStatus2("REJECTED");
        update.setApprovalComment(reason);
        update.setApprovalTime(new Date());
        update.setUpdateTime(new Date());

        int result = approvalMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("审批拒绝失败");
        }

        log.info("审批拒绝成功，ID: {}", approvalId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdraw(String approvalId) {
        if (!StringUtils.hasText(approvalId)) {
            throw new ServiceException("审批ID不能为空");
        }

        BudgetApproval approval = getById(approvalId);
        if (approval == null) {
            throw new ServiceException("审批流程不存在");
        }

        if (!STATUS_PENDING.equalsIgnoreCase(approval.getApprovalStatus())) {
            throw new ServiceException("只有待审批状态才能撤回");
        }

        BudgetApproval update = new BudgetApproval();
        update.setApprovalId(approvalId);
        update.setApprovalStatus(STATUS_CANCELLED);
        update.setApprovalStatus2("CANCELLED");
        update.setUpdateTime(new Date());

        int result = approvalMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("撤回审批失败");
        }

        log.info("撤回审批成功，ID: {}", approvalId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchApprove(List<String> ids, String comment) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要审批的记录");
        }

        for (String id : ids) {
            try {
                approve(id, comment);
            } catch (Exception e) {
                log.error("批量审批失败，ID: {}", id, e);
            }
        }

        log.info("批量审批通过完成，数量: {}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchReject(List<String> ids, String reason) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("请选择要拒绝的记录");
        }
        if (!StringUtils.hasText(reason)) {
            throw new ServiceException("拒绝原因不能为空");
        }

        for (String id : ids) {
            try {
                reject(id, reason);
            } catch (Exception e) {
                log.error("批量拒绝失败，ID: {}", id, e);
            }
        }

        log.info("批量审批拒绝完成，数量: {}", ids.size());
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        QueryWrapper<BudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        Integer totalCount = approvalMapper.selectCount(wrapper).intValue();
        statistics.put("totalCount", totalCount);

        wrapper.eq("STATUS", STATUS_PENDING);
        Integer pendingCount = approvalMapper.selectCount(wrapper).intValue();
        statistics.put("pendingCount", pendingCount);

        wrapper = new QueryWrapper<BudgetApproval>();
        wrapper.eq("DEL_FLAG", 0).eq("STATUS", STATUS_APPROVED);
        Integer approvedCount = approvalMapper.selectCount(wrapper).intValue();
        statistics.put("approvedCount", approvedCount);

        wrapper = new QueryWrapper<BudgetApproval>();
        wrapper.eq("DEL_FLAG", 0).eq("STATUS", STATUS_REJECTED);
        Integer rejectedCount = approvalMapper.selectCount(wrapper).intValue();
        statistics.put("rejectedCount", rejectedCount);

        return statistics;
    }

    @Override
    public List<BudgetApproval> getMyPendingApprovals(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new ServiceException("用户ID不能为空");
        }

        QueryWrapper<BudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0)
               .eq("STATUS", STATUS_PENDING)
               .eq("INITIATOR", userId)
               .orderByDesc("INITIATE_TIME");

        return approvalMapper.selectList(wrapper);
    }

    @Override
    public List<BudgetApproval> getMyApprovedList(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new ServiceException("用户ID不能为空");
        }

        QueryWrapper<BudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0)
               .in("STATUS", STATUS_APPROVED, STATUS_REJECTED)
               .eq("INITIATOR", userId)
               .orderByDesc("COMPLETE_TIME");

        return approvalMapper.selectList(wrapper);
    }

    /**
     * 生成审批编码
     */
    private String generateApprovalCode() {
        return "AP" + System.currentTimeMillis();
    }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }

    private LinkedHashMap<String, Object> toFlowView(BudgetApproval approval) {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        row.put("flowId", approval.getApprovalId());
        row.put("approvalId", approval.getApprovalId());
        row.put("flowName", approval.getFlowName());
        row.put("flowType", approval.getApprovalType());
        row.put("applicant", approval.getSubmitterName());
        row.put("budgetId", approval.getBudgetId());
        row.put("budgetName", approval.getBudgetName());
        row.put("budgetAmount", approval.getBudgetAmount() != null ? approval.getBudgetAmount() : 0);
        row.put("currentNode", defaultString(approval.getCurrentNode()));
        row.put("currentApprover", approval.getCurrentApproverName());
        row.put("approvalStatus", toUpperSnake(approval.getApprovalStatus()));
        row.put("priority", normalizePriority(approval.getPriority()));
        row.put("applyTime", formatDateTime(approval.getSubmitTime()));
        row.put("deadline", formatDateTime(approval.getDueDate()));
        row.put("approvalComment", approval.getApprovalComment());
        row.put("description", approval.getRemark());
        row.put("companyName", approval.getCompanyName());
        return row;
    }

    private String normalizeStatusValue(String status) {
        return status == null ? null : status.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizePriority(String priority) {
        if (!StringUtils.hasText(priority)) {
            return "MEDIUM";
        }
        String normalized = priority.trim().toUpperCase(Locale.ROOT);
        if ("URGENT".equals(normalized) || "HIGH".equals(normalized)) {
            return "HIGH";
        }
        if ("NORMAL".equals(normalized) || "MEDIUM".equals(normalized)) {
            return "MEDIUM";
        }
        if ("LOW".equals(normalized)) {
            return "LOW";
        }
        return normalized;
    }

    private String toUpperSnake(String value) {
        return StringUtils.hasText(value) ? value.trim().toUpperCase(Locale.ROOT) : "PENDING";
    }

    private String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private String defaultString(String value) {
        return StringUtils.hasText(value) ? value : "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetApproval update(BudgetApproval approval) {
        if (approval == null || !StringUtils.hasText(approval.getApprovalId())) {
            throw new ServiceException("审批ID不能为空");
        }

        BudgetApproval existing = getById(approval.getApprovalId());
        if (existing == null) {
            throw new ServiceException("审批流程不存在");
        }

        approval.setUpdateTime(new Date());
        int result = approvalMapper.updateById(approval);
        if (result <= 0) {
            throw new ServiceException("更新审批流程失败");
        }

        log.info("更新审批流程成功，ID: {}", approval.getApprovalId());
        return getById(approval.getApprovalId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String approvalId) {
        if (!StringUtils.hasText(approvalId)) {
            throw new ServiceException("审批ID不能为空");
        }

        BudgetApproval existing = getById(approvalId);
        if (existing == null) {
            throw new ServiceException("审批流程不存在");
        }

        BudgetApproval update = new BudgetApproval();
        update.setApprovalId(approvalId);
        update.setDelFlag(1);
        update.setUpdateTime(new Date());

        int result = approvalMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("删除审批流程失败");
        }

        log.info("删除审批流程成功，ID: {}", approvalId);
    }
}

