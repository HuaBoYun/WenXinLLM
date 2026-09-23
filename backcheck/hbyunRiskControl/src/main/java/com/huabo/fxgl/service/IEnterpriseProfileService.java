package com.huabo.fxgl.service;

import com.huabo.fxgl.dto.EnterpriseInfoQueryParam;
import com.huabo.fxgl.dto.FinancialRadarQueryParam;
import com.huabo.fxgl.entity.EnterpriseProfileInfo;
import com.huabo.fxgl.vo.EnterpriseInfoVO;
import com.huabo.fxgl.vo.FinancialRadarVO;
import com.huabo.fxgl.vo.EnterpriseHologramVO;
import com.huabo.fxgl.vo.EnterpriseDetailInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 企业画像服务接口
 *
 * @author AI Agent
 * @since 2025-01-21
 */
public interface IEnterpriseProfileService {

    /**
     * 解析企业ID：支持企业ID、企业编码、企业名称三种方式
     *
     * @param input 输入值（企业ID或编码）
     * @param name  企业名称（可选，当ID查不到时使用）
     * @return 解析后的企业ID，找不到返回null
     */
    String resolveEnterpriseId(String input, String name);

    /**
     * 获取企业基本信息
     *
     * @param param 查询参数
     * @return 企业信息VO
     */
    EnterpriseInfoVO getEnterpriseInfo(EnterpriseInfoQueryParam param);

    /**
     * 获取财务雷达图数据
     *
     * @param param 查询参数
     * @return 财务雷达图VO
     */
    FinancialRadarVO getFinancialRadarData(FinancialRadarQueryParam param);

    /**
     * 获取企业全息画像数据
     *
     * @param enterpriseId 企业ID
     * @return 企业全息画像VO
     */
    EnterpriseHologramVO getEnterpriseHologramData(String enterpriseId);

    /**
     * 获取企业详细信息
     *
     * @param enterpriseId 企业ID
     * @return 企业详细信息VO
     */
    EnterpriseDetailInfoVO getEnterpriseDetailInfo(String enterpriseId);

    /**
     * 获取企业列表
     *
     * @return 企业列表
     */
    List<EnterpriseProfileInfo> getEnterpriseList();

    /**
     * 刷新企业数据
     *
     * @param enterpriseId 企业ID
     * @return 刷新结果
     */
    Boolean refreshEnterpriseData(String enterpriseId);

    /**
     * 检查企业是否存在
     *
     * @param enterpriseId 企业ID
     * @return 是否存在
     */
    Boolean checkEnterpriseExists(String enterpriseId);

    /**
     * 获取人员分析数据
     *
     * @param enterpriseId 企业ID
     * @return 人员分析数据
     */
    Map<String, Object> getPersonnelAnalysisData(String enterpriseId);

    /**
     * 获取审计数据
     *
     * @param enterpriseId 企业ID
     * @return 审计数据
     */
    Map<String, Object> getAuditData(String enterpriseId);

    /**
     * 获取法律案件数据
     *
     * @param enterpriseId 企业ID
     * @return 法律案件数据
     */
    Map<String, Object> getLegalData(String enterpriseId);

    /**
     * 获取指标预警数据
     *
     * @param enterpriseId 企业ID
     * @return 指标预警数据列表
     */
    List<Map<String, Object>> getIndicatorWarnings(String enterpriseId);
}
