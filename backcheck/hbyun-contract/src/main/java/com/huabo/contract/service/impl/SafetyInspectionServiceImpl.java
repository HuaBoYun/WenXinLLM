package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.SafetyInspection;
import com.huabo.contract.mapper.SafetyInspectionMapper;
import com.huabo.contract.service.SafetyInspectionService;
import com.huabo.contract.vo.SafetyInspectionQueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 安全检查管理服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Service
@RequiredArgsConstructor
public class SafetyInspectionServiceImpl extends ServiceImpl<SafetyInspectionMapper, SafetyInspection> implements SafetyInspectionService {

    private final SafetyInspectionMapper safetyInspectionMapper;

    @Override
    public IPage<SafetyInspection> getSafetyInspectionPage(SafetyInspectionQueryParam queryParam) {
        // 创建分页对象
        Page<SafetyInspection> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());

        // 构建查询条件
        QueryWrapper<SafetyInspection> queryWrapper = new QueryWrapper<>();

        // 项目ID
        if (queryParam.getProjectId() != null) {
            queryWrapper.eq("project_id", queryParam.getProjectId());
        }

        // 检查编号
        if (StringUtils.hasText(queryParam.getInspectionNo())) {
            queryWrapper.like("inspection_no", queryParam.getInspectionNo());
        }

        // 检查名称
        if (StringUtils.hasText(queryParam.getInspectionName())) {
            queryWrapper.like("inspection_name", queryParam.getInspectionName());
        }

        // 检查类型
        if (queryParam.getInspectionType() != null) {
            queryWrapper.eq("inspection_type", queryParam.getInspectionType());
        }

        // 检查日期范围
        if (queryParam.getInspectionDateStart() != null) {
            queryWrapper.ge("inspection_date", queryParam.getInspectionDateStart());
        }
        if (queryParam.getInspectionDateEnd() != null) {
            queryWrapper.le("inspection_date", queryParam.getInspectionDateEnd());
        }

        // 检查人ID
        if (queryParam.getInspectorId() != null) {
            queryWrapper.eq("inspector_id", queryParam.getInspectorId());
        }

        // 检查地点
        if (StringUtils.hasText(queryParam.getInspectionLocation())) {
            queryWrapper.like("inspection_location", queryParam.getInspectionLocation());
        }

        // 隐患等级
        if (queryParam.getHazardLevel() != null) {
            queryWrapper.eq("hazard_level", queryParam.getHazardLevel());
        }

        // 整改负责人ID
        if (queryParam.getRectificationPersonId() != null) {
            queryWrapper.eq("rectification_person_id", queryParam.getRectificationPersonId());
        }

        // 整改状态
        if (queryParam.getRectificationStatus() != null) {
            queryWrapper.eq("rectification_status", queryParam.getRectificationStatus());
        }

        // 整改期限范围
        if (queryParam.getRectificationDeadlineStart() != null) {
            queryWrapper.ge("rectification_deadline", queryParam.getRectificationDeadlineStart());
        }
        if (queryParam.getRectificationDeadlineEnd() != null) {
            queryWrapper.le("rectification_deadline", queryParam.getRectificationDeadlineEnd());
        }

        // 紧急程度
        if (queryParam.getEmergencyLevel() != null) {
            queryWrapper.eq("emergency_level", queryParam.getEmergencyLevel());
        }

        // 验证结果
        if (queryParam.getVerificationResult() != null) {
            queryWrapper.eq("verification_result", queryParam.getVerificationResult());
        }

        // 验证人ID
        if (queryParam.getVerificationPersonId() != null) {
            queryWrapper.eq("verification_person_id", queryParam.getVerificationPersonId());
        }

        // 创建时间范围
        if (queryParam.getCreateStartTime() != null) {
            queryWrapper.ge("create_time", queryParam.getCreateStartTime());
        }
        if (queryParam.getCreateEndTime() != null) {
            queryWrapper.le("create_time", queryParam.getCreateEndTime());
        }

        // 关键词搜索
        if (StringUtils.hasText(queryParam.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper
                .like("inspection_name", queryParam.getKeyword())
                .or()
                .like("inspection_no", queryParam.getKeyword())
                .or()
                .like("inspection_location", queryParam.getKeyword())
                .or()
                .like("inspection_findings", queryParam.getKeyword())
            );
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");

        // 执行分页查询
        return safetyInspectionMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean validateInspectionInfo(SafetyInspection safetyInspection) {
        // 验证必填字段
        if (safetyInspection.getProjectId() == null) {
            return false;
        }
        if (!StringUtils.hasText(safetyInspection.getInspectionName())) {
            return false;
        }
        if (safetyInspection.getInspectionType() == null) {
            return false;
        }
        if (safetyInspection.getInspectionDate() == null) {
            return false;
        }
        if (safetyInspection.getInspectorId() == null) {
            return false;
        }
        return true;
    }

    @Override
    public String generateInspectionNo() {
        // 生成检查编号：AQJC + 年月日 + 4位序号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());

        // 查询当天已有的检查记录数量
        QueryWrapper<SafetyInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("inspection_no", "AQJC" + dateStr);
        Long count = safetyInspectionMapper.selectCount(queryWrapper);

        // 生成序号
        String sequence = String.format("%04d", count + 1);

        return "AQJC" + dateStr + sequence;
    }

    @Override
    public Boolean rectifyHazards(Long id, String rectificationMeasures, Long rectificationPersonId, Long updateBy) {
        SafetyInspection safetyInspection = new SafetyInspection();
        safetyInspection.setId(id);
        safetyInspection.setRectificationMeasures(rectificationMeasures);
        safetyInspection.setRectificationPersonId(rectificationPersonId);
        safetyInspection.setRectificationStatus((short) 2); // 整改中
        safetyInspection.setUpdateTime(new Date());

        return this.updateById(safetyInspection);
    }

    @Override
    public Boolean verifyInspection(Long id, Short verificationResult, String verificationComments, Long updateBy) {
        SafetyInspection safetyInspection = new SafetyInspection();
        safetyInspection.setId(id);
        safetyInspection.setVerificationResult(verificationResult);
        safetyInspection.setVerificationComments(verificationComments);
        safetyInspection.setVerificationDate(new Date());
        safetyInspection.setVerificationPersonId(updateBy);
        safetyInspection.setUpdateTime(new Date());

        // 如果验证合格，更新整改状态为已验收
        if (verificationResult != null && verificationResult == 1) {
            safetyInspection.setRectificationStatus((short) 4); // 已验收
        }

        return this.updateById(safetyInspection);
    }
}
