package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingRepaymentDTO;
import com.global.treasurer.dto.FinancingRepaymentQueryDTO;
import com.global.treasurer.entity.TblFinancingRepayment;
import com.global.treasurer.mapper.FinancingRepaymentMapper;
import com.global.treasurer.service.FinancingRepaymentService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class FinancingRepaymentServiceImpl implements FinancingRepaymentService {
    @Autowired
    private FinancingRepaymentMapper financingRepaymentMapper;

    @Override
    public PageInfo<TblFinancingRepayment> getRepaymentList(FinancingRepaymentQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("repaymentType", queryDTO.getRepaymentType());
        params.put("repaymentStatus", queryDTO.getRepaymentStatus());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("financingId", queryDTO.getFinancingId());
        List<TblFinancingRepayment> list = financingRepaymentMapper.selectRepaymentList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblFinancingRepayment getRepaymentById(Long repaymentId) {
        TblFinancingRepayment repayment = financingRepaymentMapper.selectRepaymentById(repaymentId);
        if (repayment == null) {
            throw new ServiceException(404, "融资还款不存在");
        }
        return repayment;
    }

    @Override
    public List<TblFinancingRepayment> getRepaymentsByFinancingId(Long financingId) {
        return financingRepaymentMapper.selectRepaymentsByFinancingId(financingId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingRepayment saveRepayment(FinancingRepaymentDTO dto) {
        TblFinancingRepayment repayment = new TblFinancingRepayment();
        BeanUtils.copyProperties(dto, repayment);
        
        if (dto.getRepaymentId() == null) {
            repayment.setRepaymentNo("RP" + System.currentTimeMillis());
            repayment.setRepaymentStatus("PENDING");
            repayment.setActualAmount(BigDecimal.ZERO);
            repayment.setDeleteFlag(0);
            repayment.setCreatedTime(new Date());
            financingRepaymentMapper.insert(repayment);
        } else {
            repayment.setUpdatedTime(new Date());
            financingRepaymentMapper.updateById(repayment);
        }
        return repayment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRepayment(Long repaymentId) {
        TblFinancingRepayment repayment = getRepaymentById(repaymentId);
        if (!"PENDING".equals(repayment.getRepaymentStatus())) {
            throw new ServiceException(400, "只能删除待还款状态的记录");
        }
        repayment.setDeleteFlag(1);
        repayment.setUpdatedTime(new Date());
        financingRepaymentMapper.updateById(repayment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRepayments(List<Long> repaymentIds) {
        financingRepaymentMapper.batchDeleteByIds(repaymentIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long repaymentId) {
        financingRepaymentMapper.updateRepaymentStatus(repaymentId, "APPROVING");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long repaymentId, String comments) {
        TblFinancingRepayment repayment = getRepaymentById(repaymentId);
        repayment.setRepaymentStatus("APPROVED");
        repayment.setUpdatedTime(new Date());
        financingRepaymentMapper.updateById(repayment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long repaymentId, String comments) {
        TblFinancingRepayment repayment = getRepaymentById(repaymentId);
        repayment.setRepaymentStatus("REJECTED");
        repayment.setUpdatedTime(new Date());
        financingRepaymentMapper.updateById(repayment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmRepayment(Long repaymentId, BigDecimal actualAmount) {
        TblFinancingRepayment repayment = getRepaymentById(repaymentId);
        repayment.setRepaymentStatus("COMPLETED");
        repayment.setActualAmount(actualAmount);
        repayment.setActualRepaymentDate(new Date());
        repayment.setUpdatedTime(new Date());
        financingRepaymentMapper.updateById(repayment);
    }

    @Override
    public List<TblFinancingRepayment> getUpcomingRepayments(Integer days) {
        return financingRepaymentMapper.selectUpcomingRepayments(days);
    }

    @Override
    public List<TblFinancingRepayment> getOverdueRepayments() {
        return financingRepaymentMapper.selectOverdueRepayments();
    }

    @Override
    public Map<String, Object> getRepaymentSummary(Long companyId) {
        return financingRepaymentMapper.selectRepaymentSummary(companyId);
    }
}

