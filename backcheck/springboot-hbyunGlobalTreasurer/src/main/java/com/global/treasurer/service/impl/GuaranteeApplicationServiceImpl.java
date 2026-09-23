package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.GuaranteeApplicationDTO;
import com.global.treasurer.dto.GuaranteeApplicationQueryDTO;
import com.global.treasurer.entity.TblGuaranteeApplication;
import com.global.treasurer.mapper.GuaranteeApplicationMapper;
import com.global.treasurer.service.GuaranteeApplicationService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GuaranteeApplicationServiceImpl implements GuaranteeApplicationService {
    @Autowired
    private GuaranteeApplicationMapper guaranteeApplicationMapper;

    @Override
    public PageInfo<TblGuaranteeApplication> getApplicationList(GuaranteeApplicationQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("guaranteeType", queryDTO.getGuaranteeType());
        params.put("status", queryDTO.getStatus());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        List<TblGuaranteeApplication> list = guaranteeApplicationMapper.selectApplicationList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblGuaranteeApplication getApplicationById(Long applicationId) {
        TblGuaranteeApplication application = guaranteeApplicationMapper.selectApplicationById(applicationId);
        if (application == null) {
            throw new ServiceException(404, "担保申请不存在");
        }
        return application;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblGuaranteeApplication saveApplication(GuaranteeApplicationDTO dto) {
        TblGuaranteeApplication application = new TblGuaranteeApplication();
        BeanUtils.copyProperties(dto, application);

        if (dto.getApplicationId() == null) {
            application.setApplicationNo("GA" + System.currentTimeMillis());
            application.setStatus("DRAFT");
            application.setApplicationDate(new Date());
            application.setDeleteFlag(0);
            application.setCreatedTime(new Date());
            guaranteeApplicationMapper.insert(application);
        } else {
            application.setUpdatedTime(new Date());
            // 使用自定义的 updateApplication 方法，避免与 MyBatis-Plus 的 updateById 冲突
            guaranteeApplicationMapper.updateApplication(application);
        }
        return application;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteApplication(Long applicationId) {
        TblGuaranteeApplication application = getApplicationById(applicationId);
        if (!"DRAFT".equals(application.getStatus())) {
            throw new ServiceException(400, "只能删除草稿状态的担保申请");
        }
        application.setDeleteFlag(1);
        application.setUpdatedTime(new Date());
        guaranteeApplicationMapper.updateApplication(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteApplications(List<Long> applicationIds) {
        guaranteeApplicationMapper.batchDeleteByIds(applicationIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long applicationId) {
        TblGuaranteeApplication application = getApplicationById(applicationId);
        if (!"DRAFT".equals(application.getStatus())) {
            throw new ServiceException(400, "只能提交草稿状态的担保申请");
        }
        guaranteeApplicationMapper.updateApplicationStatus(applicationId, "PENDING");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long applicationId, String comments) {
        TblGuaranteeApplication application = getApplicationById(applicationId);
        if (!"PENDING".equals(application.getStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的担保申请");
        }
        application.setStatus("APPROVED");
        application.setUpdatedTime(new Date());
        guaranteeApplicationMapper.updateApplication(application);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long applicationId, String comments) {
        TblGuaranteeApplication application = getApplicationById(applicationId);
        if (!"PENDING".equals(application.getStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的担保申请");
        }
        application.setStatus("REJECTED");
        application.setUpdatedTime(new Date());
        guaranteeApplicationMapper.updateApplication(application);
    }

    @Override
    public Map<String, Object> getApplicationSummary(Long companyId) {
        return guaranteeApplicationMapper.selectApplicationSummary(companyId);
    }
}

