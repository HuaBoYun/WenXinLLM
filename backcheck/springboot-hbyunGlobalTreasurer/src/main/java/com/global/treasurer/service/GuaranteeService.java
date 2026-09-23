package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.GuaranteeQueryDTO;
import com.global.treasurer.entity.TblGuaranteeApplication;

import java.util.Map;

/**
 * 担保业务Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
public interface GuaranteeService {

    /**
     * 分页查询担保申请列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblGuaranteeApplication> getGuaranteeApplicationPage(GuaranteeQueryDTO queryDTO);

    /**
     * 根据ID获取担保申请详情
     *
     * @param applicationId 申请ID
     * @return 申请详情
     */
    TblGuaranteeApplication getGuaranteeApplicationById(Long applicationId);

    /**
     * 获取担保申请统计信息
     *
     * @param companyId 公司ID
     * @return 统计信息
     */
    Map<String, Object> getGuaranteeStatistics(Long companyId);
}
