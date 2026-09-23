package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BondIssuanceDTO;
import com.global.treasurer.dto.BondIssuanceQueryDTO;
import com.global.treasurer.entity.TblBondIssuance;

import java.util.List;
import java.util.Map;

/**
 * 债券发行服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface BondIssuanceService {

    PageInfo<TblBondIssuance> getIssuanceList(BondIssuanceQueryDTO queryDTO);

    TblBondIssuance getIssuanceById(Long issuanceId);

    TblBondIssuance saveIssuance(BondIssuanceDTO dto);

    void deleteIssuance(Long issuanceId);

    void batchDeleteIssuances(List<Long> issuanceIds);

    void submitForApproval(Long issuanceId);

    void approve(Long issuanceId, String comments);

    void reject(Long issuanceId, String comments);

    void confirmIssuance(Long issuanceId, Map<String, Object> params);

    List<TblBondIssuance> getExpiringBonds(Integer days);

    Map<String, Object> getIssuanceSummary(Long companyId);

    /**
     * 获取债券类型分布
     */
    List<Map<String, Object>> getBondTypeDistribution(Long companyId);

    /**
     * 获取债券发行趋势
     */
    List<Map<String, Object>> getIssuanceTrend(Long companyId, Integer months);

    /**
     * 获取所有债券用于导出
     */
    List<TblBondIssuance> getAllForExport(BondIssuanceQueryDTO queryDTO);

    /**
     * 批量导入债券
     */
    Map<String, Object> batchImportBonds(List<BondIssuanceDTO> bondList, Long companyId, String companyName, Long createdBy);
}

