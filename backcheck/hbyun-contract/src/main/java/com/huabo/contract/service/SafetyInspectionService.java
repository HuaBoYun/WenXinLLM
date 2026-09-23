package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.SafetyInspection;
import com.huabo.contract.vo.SafetyInspectionQueryParam;

/**
 * 安全检查管理服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface SafetyInspectionService extends IService<SafetyInspection> {

    /**
     * 分页查询安全检查记录
     */
    IPage<SafetyInspection> getSafetyInspectionPage(SafetyInspectionQueryParam queryParam);

    /**
     * 验证安全检查记录信息
     */
    boolean validateInspectionInfo(SafetyInspection safetyInspection);

    /**
     * 生成检查编号
     */
    String generateInspectionNo();

    /**
     * 安全隐患整改
     */
    Boolean rectifyHazards(Long id, String rectificationMeasures, Long rectificationPersonId, Long updateBy);

    /**
     * 安全检查验证
     */
    Boolean verifyInspection(Long id, Short verificationResult, String verificationComments, Long updateBy);
}
