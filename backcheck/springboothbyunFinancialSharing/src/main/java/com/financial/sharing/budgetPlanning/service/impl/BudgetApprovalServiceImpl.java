package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.budgetPlanning.dto.BudgetApprovalQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetApproval;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import com.financial.sharing.budgetPlanning.mapper.BudgetApprovalMapper;
import com.financial.sharing.budgetPlanning.mapper.BudgetDataMapper;
import com.financial.sharing.budgetPlanning.service.BudgetApprovalService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预算数据审批Service实现类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Service
public class BudgetApprovalServiceImpl implements BudgetApprovalService {

    @Autowired
    private BudgetApprovalMapper approvalMapper;

    @Autowired
    private BudgetDataMapper dataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitApproval(String dataId, String approverIds, String comment) {
        // 检查数据是否存在
        TblBudgetData data = dataMapper.selectById(dataId);
        if (data == null) {
            throw new RuntimeException("预算数据不存在");
        }

        // 检查数据状态
        if (!"SUBMITTED".equals(data.getStatus())) {
            throw new RuntimeException("只有已提交状态的数据才能发起审批");
        }

        // 检查是否已有待审批记录
        TblBudgetApproval pending = approvalMapper.selectPendingByDataId(dataId);
        if (pending != null) {
            throw new RuntimeException("该数据已有待审批记录,请勿重复提交");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.requireUserId();
        String userName = UserUtils.getUser().getRealname();
        Date now = new Date();

        // 创建审批记录
        String[] approverIdArray = approverIds.split(",");
        for (int i = 0; i < approverIdArray.length; i++) {
            TblBudgetApproval approval = new TblBudgetApproval();
            approval.setApprovalId(UUID.randomUUID().toString().replace("-", ""));
            approval.setDataId(dataId);
            approval.setWorkflowId(UUID.randomUUID().toString().replace("-", ""));
            approval.setNodeCode("NODE_" + (i + 1));
            approval.setNodeName("审批节点" + (i + 1));
            approval.setApproverId(approverIdArray[i].trim());
            approval.setApprovalStatus(i == 0 ? "PENDING" : "WAITING");
            approval.setApprovalComment(comment);
            approval.setOrgId(orgId);
            approval.setCreateUser(userId);
            approval.setCreateTime(now);
            approval.setUpdateUser(userId);
            approval.setUpdateTime(now);

            approvalMapper.insert(approval);
        }

        // 更新数据状态为审批中
        data.setStatus("APPROVING");
        data.setUpdateUser(userId);
        data.setUpdateTime(now);
        dataMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveData(String approvalId, String comment) {
        // 查询审批记录
        TblBudgetApproval approval = approvalMapper.selectById(approvalId);
        if (approval == null) {
            throw new RuntimeException("审批记录不存在");
        }

        // 检查审批状态
        if (!"PENDING".equals(approval.getApprovalStatus())) {
            throw new RuntimeException("该审批记录不是待审批状态");
        }

        // 检查审批人权限
        String userId = UserUtils.requireUserId();
        if (!userId.equals(approval.getApproverId())) {
            throw new RuntimeException("您没有权限审批该数据");
        }

        Date now = new Date();

        // 更新审批记录
        approval.setApprovalStatus("APPROVED");
        approval.setApprovalComment(comment);
        approval.setApprovalTime(now);
        approval.setUpdateUser(userId);
        approval.setUpdateTime(now);
        approvalMapper.updateById(approval);

        // 查询是否还有待审批节点
        QueryWrapper<TblBudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("DATA_ID", approval.getDataId());
        wrapper.eq("APPROVAL_STATUS", "WAITING");
        wrapper.orderByAsc("NODE_CODE");
        List<TblBudgetApproval> waitingList = approvalMapper.selectList(wrapper);

        if (!waitingList.isEmpty()) {
            // 激活下一个审批节点
            TblBudgetApproval nextApproval = waitingList.get(0);
            nextApproval.setApprovalStatus("PENDING");
            nextApproval.setUpdateUser(userId);
            nextApproval.setUpdateTime(now);
            approvalMapper.updateById(nextApproval);
        } else {
            // 所有节点都已审批通过,更新数据状态为已审批
            TblBudgetData data = dataMapper.selectById(approval.getDataId());
            data.setStatus("APPROVED");
            data.setUpdateUser(userId);
            data.setUpdateTime(now);
            dataMapper.updateById(data);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectData(String approvalId, String comment) {
        // 查询审批记录
        TblBudgetApproval approval = approvalMapper.selectById(approvalId);
        if (approval == null) {
            throw new RuntimeException("审批记录不存在");
        }

        // 检查审批状态
        if (!"PENDING".equals(approval.getApprovalStatus())) {
            throw new RuntimeException("该审批记录不是待审批状态");
        }

        // 检查审批人权限
        String userId = UserUtils.requireUserId();
        if (!userId.equals(approval.getApproverId())) {
            throw new RuntimeException("您没有权限审批该数据");
        }

        Date now = new Date();

        // 更新审批记录
        approval.setApprovalStatus("REJECTED");
        approval.setApprovalComment(comment);
        approval.setApprovalTime(now);
        approval.setUpdateUser(userId);
        approval.setUpdateTime(now);
        approvalMapper.updateById(approval);

        // 更新数据状态为已驳回
        TblBudgetData data = dataMapper.selectById(approval.getDataId());
        data.setStatus("REJECTED");
        data.setUpdateUser(userId);
        data.setUpdateTime(now);
        dataMapper.updateById(data);

        // 取消其他待审批节点
        QueryWrapper<TblBudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("DATA_ID", approval.getDataId());
        wrapper.in("APPROVAL_STATUS", Arrays.asList("PENDING", "WAITING"));
        wrapper.ne("APPROVAL_ID", approvalId);
        List<TblBudgetApproval> pendingList = approvalMapper.selectList(wrapper);
        for (TblBudgetApproval pending : pendingList) {
            pending.setApprovalStatus("CANCELLED");
            pending.setUpdateUser(userId);
            pending.setUpdateTime(now);
            approvalMapper.updateById(pending);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelApproval(String dataId, String comment) {
        // 检查数据是否存在
        TblBudgetData data = dataMapper.selectById(dataId);
        if (data == null) {
            throw new RuntimeException("预算数据不存在");
        }

        // 检查数据状态
        if (!"APPROVING".equals(data.getStatus())) {
            throw new RuntimeException("只有审批中的数据才能撤销");
        }

        // 检查是否是提交人
        String userId = UserUtils.requireUserId();
        if (!userId.equals(data.getSubmitUser())) {
            throw new RuntimeException("只有提交人才能撤销审批");
        }

        Date now = new Date();

        // 取消所有审批记录
        QueryWrapper<TblBudgetApproval> wrapper = new QueryWrapper<>();
        wrapper.eq("DATA_ID", dataId);
        wrapper.in("APPROVAL_STATUS", Arrays.asList("PENDING", "WAITING"));
        List<TblBudgetApproval> pendingList = approvalMapper.selectList(wrapper);
        for (TblBudgetApproval approval : pendingList) {
            approval.setApprovalStatus("CANCELLED");
            approval.setApprovalComment(comment);
            approval.setUpdateUser(userId);
            approval.setUpdateTime(now);
            approvalMapper.updateById(approval);
        }

        // 更新数据状态为已提交
        data.setStatus("SUBMITTED");
        data.setUpdateUser(userId);
        data.setUpdateTime(now);
        dataMapper.updateById(data);
    }

    @Override
    public PageInfo<TblBudgetApproval> getApprovalList(BudgetApprovalQueryParam param) {
        param.setOrgId(UserUtils.requireOrgId());
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetApproval> list = approvalMapper.selectApprovalList(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblBudgetApproval> getApprovalHistory(String dataId) {
        return approvalMapper.selectByDataId(dataId);
    }

    @Override
    public PageInfo<TblBudgetApproval> getMyPendingList(BudgetApprovalQueryParam param) {
        param.setApproverId(UserUtils.requireUserId());
        param.setOrgId(UserUtils.requireOrgId());
        param.setApprovalStatus("PENDING");
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetApproval> list = approvalMapper.selectMyPendingList(param);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<TblBudgetApproval> getMyApprovedList(BudgetApprovalQueryParam param) {
        param.setApproverId(UserUtils.requireUserId());
        param.setOrgId(UserUtils.requireOrgId());
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetApproval> list = approvalMapper.selectMyApprovedList(param);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchApprove(List<String> approvalIds, String comment) {
        for (String approvalId : approvalIds) {
            approveData(approvalId, comment);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchReject(List<String> approvalIds, String comment) {
        for (String approvalId : approvalIds) {
            rejectData(approvalId, comment);
        }
    }

    @Override
    public Map<String, Object> getApprovalStatistics() {
        String userId = UserUtils.requireUserId();
        String orgId = UserUtils.requireOrgId();

        Map<String, Object> result = new HashMap<>();

        // 统计我的待审批数量
        QueryWrapper<TblBudgetApproval> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("APPROVER_ID", userId);
        pendingWrapper.eq("APPROVAL_STATUS", "PENDING");
        pendingWrapper.eq("ORG_ID", orgId);
        Integer pendingCount = approvalMapper.selectCount(pendingWrapper);
        result.put("pendingCount", pendingCount);

        // 统计我的已审批数量(本月)
        QueryWrapper<TblBudgetApproval> approvedWrapper = new QueryWrapper<>();
        approvedWrapper.eq("APPROVER_ID", userId);
        approvedWrapper.in("APPROVAL_STATUS", Arrays.asList("APPROVED", "REJECTED"));
        approvedWrapper.eq("ORG_ID", orgId);
        approvedWrapper.ge("APPROVAL_TIME", getMonthStartDate());
        Integer approvedCount = approvalMapper.selectCount(approvedWrapper);
        result.put("approvedCount", approvedCount);

        // 统计我提交的审批数量(本月)
        QueryWrapper<TblBudgetApproval> submitWrapper = new QueryWrapper<>();
        submitWrapper.eq("CREATE_USER", userId);
        submitWrapper.eq("ORG_ID", orgId);
        submitWrapper.ge("CREATE_TIME", getMonthStartDate());
        Integer submitCount = approvalMapper.selectCount(submitWrapper);
        result.put("submitCount", submitCount);

        return result;
    }

    /**
     * 获取本月开始日期
     */
    private Date getMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}

