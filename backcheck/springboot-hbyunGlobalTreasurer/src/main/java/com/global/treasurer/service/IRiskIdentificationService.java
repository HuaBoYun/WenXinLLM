package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RiskIdentificationDTO;
import com.global.treasurer.dto.RiskIdentificationQueryDTO;
import com.global.treasurer.entity.TblRiskIdentification;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 风险识别Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-22
 */
public interface IRiskIdentificationService extends IService<TblRiskIdentification> {

    /**
     * 分页查询风险识别
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblRiskIdentification> selectRiskIdentificationList(RiskIdentificationQueryDTO queryDTO);

    /**
     * 根据ID查询
     *
     * @param identificationId 识别ID
     * @return 风险识别
     */
    TblRiskIdentification selectRiskIdentificationById(Long identificationId);

    /**
     * 新增风险识别
     *
     * @param dto 风险识别DTO
     * @return 新增的风险识别
     */
    TblRiskIdentification insertRiskIdentification(RiskIdentificationDTO dto);

    /**
     * 更新风险识别
     *
     * @param dto 风险识别DTO
     * @return 更新后的风险识别
     */
    TblRiskIdentification updateRiskIdentification(RiskIdentificationDTO dto);

    /**
     * 删除风险识别
     *
     * @param identificationId 识别ID
     * @return 是否成功
     */
    boolean deleteRiskIdentification(Long identificationId);

    /**
     * 导出风险识别
     *
     * @param queryDTO 查询条件
     * @param response HTTP响应
     */
    void exportRiskIdentification(RiskIdentificationQueryDTO queryDTO, HttpServletResponse response);

    Map<String, Object> batchAssess(List<Long> ids);

    TblRiskIdentification closeRisk(Long identificationId, String reason);

    List<Map<String, Object>> getHistory(Long identificationId);
}

