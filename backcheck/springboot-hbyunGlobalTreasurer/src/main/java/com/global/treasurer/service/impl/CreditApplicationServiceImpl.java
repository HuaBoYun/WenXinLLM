package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CreditApplicationDTO;
import com.global.treasurer.dto.CreditApplicationQueryDTO;
import com.global.treasurer.entity.TblCreditApplication;
import com.global.treasurer.mapper.CreditApplicationMapper;
import com.global.treasurer.service.CreditApplicationService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 授信申请服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class CreditApplicationServiceImpl implements CreditApplicationService {
    @Autowired
    private CreditApplicationMapper creditApplicationMapper;

    @Override
    public PageInfo<TblCreditApplication> getApplicationList(CreditApplicationQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", queryDTO.getCompanyId());
        params.put("creditType", queryDTO.getCreditType());
        params.put("applicationStatus", queryDTO.getApplicationStatus());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        List<TblCreditApplication> list = creditApplicationMapper.selectApplicationList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblCreditApplication getApplicationById(Long applicationId) {
        TblCreditApplication application = creditApplicationMapper.selectApplicationById(applicationId);
        if (application == null) {
            throw new ServiceException(404, "授信申请不存在");
        }
        return application;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblCreditApplication saveApplication(CreditApplicationDTO dto) {
        TblCreditApplication application = new TblCreditApplication();
        BeanUtils.copyProperties(dto, application);

        // 如果提供了companyName，直接使用；否则可以根据companyId查询
        if (dto.getCompanyName() != null && !dto.getCompanyName().isEmpty()) {
            application.setCompanyName(dto.getCompanyName());
        }

        if (dto.getApplicationId() == null) {
            application.setApplicationNo(generateApplicationNo());
            application.setApplicationStatus("DRAFT");
            application.setApplicationDate(new Date());
            application.setDeleteFlag(0);
            application.setCreatedTime(new Date());
            creditApplicationMapper.insert(application);
        } else {
            application.setUpdatedTime(new Date());
            creditApplicationMapper.updateById(application);
        }
        return application;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteApplication(Long applicationId) {
        TblCreditApplication application = getApplicationById(applicationId);
        if (!"DRAFT".equals(application.getApplicationStatus())) {
            throw new ServiceException(400, "只能删除草稿状态的授信申请");
        }
        application.setDeleteFlag(1);
        application.setUpdatedTime(new Date());
        creditApplicationMapper.updateById(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteApplications(List<Long> applicationIds) {
        creditApplicationMapper.batchDeleteByIds(applicationIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long applicationId) {
        TblCreditApplication application = getApplicationById(applicationId);
        if (!"DRAFT".equals(application.getApplicationStatus())) {
            throw new ServiceException(400, "只能提交草稿状态的授信申请");
        }
        creditApplicationMapper.updateApplicationStatus(applicationId, "PENDING");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long applicationId, java.math.BigDecimal approvedAmount, String comments) {
        TblCreditApplication application = getApplicationById(applicationId);
        if (!"PENDING".equals(application.getApplicationStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的授信申请");
        }
        application.setApplicationStatus("APPROVED");
        application.setApprovedAmount(approvedAmount);
        application.setApprovalComments(comments);
        application.setUpdatedTime(new Date());
        creditApplicationMapper.updateById(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long applicationId, String rejectReason) {
        TblCreditApplication application = getApplicationById(applicationId);
        if (!"PENDING".equals(application.getApplicationStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的授信申请");
        }
        application.setApplicationStatus("REJECTED");
        application.setApprovalComments(rejectReason);
        application.setUpdatedTime(new Date());
        creditApplicationMapper.updateById(application);
    }

    @Override
    public Map<String, Object> getApplicationSummary(Long companyId) {
        return creditApplicationMapper.selectApplicationSummary(companyId);
    }

    private String generateApplicationNo() {
        return "CA" + System.currentTimeMillis();
    }
}

