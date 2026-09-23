package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskAssessmentDTO;
import com.global.treasurer.dto.RiskAssessmentQueryDTO;
import com.global.treasurer.entity.TblRiskAssessment;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * 风险评估Service接口
 * 对应已存在的 TBL_RISK_ASSESSMENT 表
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
public interface IRiskAssessmentService extends IService<TblRiskAssessment> {

    /**
     * 分页查询风险评估
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblRiskAssessment> selectRiskAssessmentList(RiskAssessmentQueryDTO queryDTO);

    /**
     * 根据ID查询
     *
     * @param riskId 风险ID
     * @return 风险评估
     */
    TblRiskAssessment selectRiskAssessmentById(String riskId);

    /**
     * 新增风险评估
     *
     * @param dto 风险评估DTO
     * @return 新增的风险评估
     */
    TblRiskAssessment insertRiskAssessment(RiskAssessmentDTO dto);

    /**
     * 更新风险评估
     *
     * @param dto 风险评估DTO
     * @return 更新后的风险评估
     */
    TblRiskAssessment updateRiskAssessment(RiskAssessmentDTO dto);

    /**
     * 删除风险评估
     *
     * @param riskId 风险ID
     * @return 是否成功
     */
    boolean deleteRiskAssessment(String riskId);

    /**
     * 获取风险分布分析
     *
     * @param enterpriseId 企业ID
     * @return 分析数据
     */
    Map<String, Object> getDistributionAnalysis(String enterpriseId);

    /**
     * 导出风险评估
     *
     * @param queryDTO 查询条件
     * @param response HTTP响应
     */
    void exportRiskAssessment(RiskAssessmentQueryDTO queryDTO, HttpServletResponse response);

    Map<String, Object> importRiskAssessment(MultipartFile file);

    void generateReport(String riskId, HttpServletResponse response);

    List<Map<String, Object>> getHistory(String riskId);
}

