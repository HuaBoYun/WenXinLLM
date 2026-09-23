package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblLeaseReturn;
import com.global.treasurer.entity.TblLeasePayment;
import com.global.treasurer.mapper.LeaseReturnMapper;
import com.global.treasurer.mapper.LeasePaymentMapper;
import com.global.treasurer.service.LeaseReturnService;
import com.global.treasurer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 退租申请服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@Service
public class LeaseReturnServiceImpl implements LeaseReturnService {

    private static final Logger log = LoggerFactory.getLogger(LeaseReturnServiceImpl.class);

    @Autowired
    private LeaseReturnMapper leaseReturnMapper;

    @Autowired
    private LeasePaymentMapper leasePaymentMapper;

    @Override
    public TblLeaseReturn getReturnByLeaseId(Long leaseId) {
        return leaseReturnMapper.selectByLeaseId(leaseId);
    }

    @Override
    public TblLeaseReturn getReturnById(Long returnId) {
        TblLeaseReturn leaseReturn = leaseReturnMapper.selectByReturnId(returnId);
        if (leaseReturn == null) {
            throw new ServiceException(404, "退租申请不存在");
        }
        return leaseReturn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblLeaseReturn saveReturn(TblLeaseReturn leaseReturn) {
        if (leaseReturn.getReturnId() == null) {
            leaseReturn.setStatus("DRAFT");
            leaseReturn.setCreatedTime(new Date());
            leaseReturnMapper.insert(leaseReturn);
            log.info("新增退租申请成功, returnId: {}", leaseReturn.getReturnId());
        } else {
            leaseReturn.setUpdatedTime(new Date());
            leaseReturnMapper.updateById(leaseReturn);
            log.info("更新退租申请成功, returnId: {}", leaseReturn.getReturnId());
        }
        return leaseReturn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitReturn(Long returnId) {
        TblLeaseReturn leaseReturn = getReturnById(returnId);
        if (!"DRAFT".equals(leaseReturn.getStatus())) {
            throw new ServiceException(400, "只有草稿状态的退租申请可以提交");
        }
        leaseReturnMapper.updateReturnStatus(returnId, "SUBMITTED", null, null);
        log.info("提交退租申请成功, returnId: {}", returnId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveReturn(Long returnId, String comments, Long approvedBy) {
        TblLeaseReturn leaseReturn = getReturnById(returnId);
        if (!"SUBMITTED".equals(leaseReturn.getStatus())) {
            throw new ServiceException(400, "只有已提交状态的退租申请可以审批");
        }
        leaseReturnMapper.updateReturnStatus(returnId, "APPROVED", comments, approvedBy);
        log.info("审批通过退租申请, returnId: {}", returnId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectReturn(Long returnId, String comments, Long approvedBy) {
        TblLeaseReturn leaseReturn = getReturnById(returnId);
        if (!"SUBMITTED".equals(leaseReturn.getStatus())) {
            throw new ServiceException(400, "只有已提交状态的退租申请可以拒绝");
        }
        leaseReturnMapper.updateReturnStatus(returnId, "REJECTED", comments, approvedBy);
        log.info("拒绝退租申请, returnId: {}", returnId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeReturn(Long returnId) {
        TblLeaseReturn leaseReturn = getReturnById(returnId);
        if (!"APPROVED".equals(leaseReturn.getStatus())) {
            throw new ServiceException(400, "只有已审批状态的退租申请可以完成");
        }
        leaseReturnMapper.updateReturnStatus(returnId, "COMPLETED", null, null);
        log.info("完成退租, returnId: {}", returnId);
    }

    @Override
    public List<TblLeaseReturn> getPendingApprovalList() {
        return leaseReturnMapper.selectPendingApprovalList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReturnByLeaseId(Long leaseId) {
        leaseReturnMapper.deleteByLeaseId(leaseId);
        log.info("删除租赁退租申请成功, leaseId: {}", leaseId);
    }

    @Override
    public Map<String, Object> calculateReturnFees(Long leaseId, String returnType, String assetDisposal) {
        Map<String, Object> fees = new HashMap<>();
        
        // 获取未付租金
        Map<String, Object> summary = leasePaymentMapper.selectPaymentSummary(leaseId);
        BigDecimal unpaidAmount = summary != null ? 
            (BigDecimal) summary.getOrDefault("unpaidAmount", BigDecimal.ZERO) : BigDecimal.ZERO;
        
        fees.put("unpaidRent", unpaidAmount);
        
        // 计算违约金（提前退租按未付租金的5%计算）
        BigDecimal penaltyAmount = BigDecimal.ZERO;
        if ("EARLY".equals(returnType)) {
            penaltyAmount = unpaidAmount.multiply(new BigDecimal("0.05"));
        } else if ("BREACH".equals(returnType)) {
            penaltyAmount = unpaidAmount.multiply(new BigDecimal("0.10"));
        }
        fees.put("penaltyAmount", penaltyAmount);
        
        // 资产残值（模拟计算）
        BigDecimal residualValue = new BigDecimal("50000");
        fees.put("residualValue", residualValue);
        
        // 结算金额
        BigDecimal settlementAmount = unpaidAmount.add(penaltyAmount).subtract(residualValue);
        if (settlementAmount.compareTo(BigDecimal.ZERO) < 0) {
            settlementAmount = BigDecimal.ZERO;
        }
        fees.put("settlementAmount", settlementAmount);
        
        return fees;
    }
}

