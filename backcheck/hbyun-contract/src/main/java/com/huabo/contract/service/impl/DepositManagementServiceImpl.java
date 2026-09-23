package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.DepositManagement;
import com.huabo.contract.mapper.DepositManagementMapper;
import com.huabo.contract.service.DepositManagementService;
import com.huabo.contract.vo.DepositManagementQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 保证金管理服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Service
public class DepositManagementServiceImpl extends ServiceImpl<DepositManagementMapper, DepositManagement> implements DepositManagementService {

    @Override
    public IPage<DepositManagement> getDepositManagementPage(DepositManagementQueryParam queryParam) {
        Page<DepositManagement> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        return baseMapper.selectDepositManagementPage(page, queryParam);
    }

    @Override
    public List<DepositManagement> getByBiddingProjectId(Long biddingProjectId) {
        return baseMapper.selectByBiddingProjectId(biddingProjectId);
    }

    @Override
    public List<DepositManagement> getByDepositStatus(Integer depositStatus) {
        return baseMapper.selectByDepositStatus(depositStatus);
    }

    @Override
    public List<DepositManagement> getExpiringSoon() {
        return baseMapper.selectExpiringSoon();
    }

    @Override
    public List<DepositManagement> getOverdue() {
        return baseMapper.selectOverdue();
    }

    @Override
    public BigDecimal sumDepositAmountByType(Integer depositType) {
        return baseMapper.sumDepositAmountByType(depositType);
    }

    @Override
    public BigDecimal sumDepositAmountByProject(Long biddingProjectId) {
        return baseMapper.sumDepositAmountByProject(biddingProjectId);
    }

    @Override
    public Integer countByDepositStatus(Integer depositStatus) {
        return baseMapper.countByDepositStatus(depositStatus);
    }

    @Override
    public List<DepositManagement> getByManagerId(Long managerId) {
        return baseMapper.selectByManagerId(managerId);
    }

    @Override
    public List<DepositManagement> getByBankName(String bankName) {
        return baseMapper.selectByBankName(bankName);
    }

    @Override
    public DepositManagement getByGuaranteeNo(String guaranteeNo) {
        return baseMapper.selectByGuaranteeNo(guaranteeNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateStatus(List<Long> ids, Integer depositStatus, Long updateBy) {
        return baseMapper.batchUpdateStatus(ids, depositStatus, updateBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean refundDeposit(Long id, BigDecimal refundAmount, String refundReason, Long updateBy) {
        DepositManagement deposit = getById(id);
        if (deposit == null) {
            return false;
        }
        
        deposit.setDepositStatus(2); // 已退还
        deposit.setRefundAmount(refundAmount);
        deposit.setRefundReason(refundReason);
        deposit.setRefundTime(new Date());
        deposit.setUpdateBy(updateBy);
        deposit.setUpdateTime(new Date());
        
        return updateById(deposit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean confiscateDeposit(Long id, String confiscationReason, Long updateBy) {
        DepositManagement deposit = getById(id);
        if (deposit == null) {
            return false;
        }
        
        deposit.setDepositStatus(3); // 已没收
        deposit.setConfiscationReason(confiscationReason);
        deposit.setUpdateBy(updateBy);
        deposit.setUpdateTime(new Date());
        
        return updateById(deposit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean convertToPerformance(Long id, Long updateBy) {
        DepositManagement deposit = getById(id);
        if (deposit == null) {
            return false;
        }
        
        deposit.setDepositStatus(4); // 已转履约
        deposit.setUpdateBy(updateBy);
        deposit.setUpdateTime(new Date());
        
        return updateById(deposit);
    }

    @Override
    public List<DepositManagement> getDepositStatistics() {
        return baseMapper.selectDepositStatistics();
    }

    @Override
    public List<DepositManagement> getExpiryReminders() {
        return getExpiringSoon();
    }

    @Override
    public Boolean validateDepositInfo(DepositManagement depositManagement) {
        if (depositManagement == null) {
            return false;
        }
        
        // 验证必填字段
        if (depositManagement.getBiddingProjectId() == null || 
            depositManagement.getDepositType() == null ||
            depositManagement.getDepositAmount() == null ||
            depositManagement.getPaymentMethod() == null) {
            return false;
        }
        
        // 验证金额
        if (depositManagement.getDepositAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        return true;
    }
}
