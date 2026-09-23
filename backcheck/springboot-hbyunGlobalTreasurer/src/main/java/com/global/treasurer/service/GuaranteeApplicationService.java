package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.GuaranteeApplicationDTO;
import com.global.treasurer.dto.GuaranteeApplicationQueryDTO;
import com.global.treasurer.entity.TblGuaranteeApplication;

import java.util.List;
import java.util.Map;

/**
 * 担保申请服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface GuaranteeApplicationService {

    PageInfo<TblGuaranteeApplication> getApplicationList(GuaranteeApplicationQueryDTO queryDTO);

    TblGuaranteeApplication getApplicationById(Long applicationId);

    TblGuaranteeApplication saveApplication(GuaranteeApplicationDTO dto);

    void deleteApplication(Long applicationId);

    void batchDeleteApplications(List<Long> applicationIds);

    void submitForApproval(Long applicationId);

    void approve(Long applicationId, String comments);

    void reject(Long applicationId, String comments);

    Map<String, Object> getApplicationSummary(Long companyId);
}

