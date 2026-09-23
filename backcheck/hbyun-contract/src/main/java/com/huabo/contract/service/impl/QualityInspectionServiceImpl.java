package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.contract.entity.QualityInspection;
import com.huabo.contract.mapper.QualityInspectionMapper;
import com.huabo.contract.service.QualityInspectionService;
import com.huabo.contract.vo.QualityInspectionQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 质量检查管理服务实现类
 * 根据达梦数据库表结构重新实现
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Service
public class QualityInspectionServiceImpl extends ServiceImpl<QualityInspectionMapper, QualityInspection> implements QualityInspectionService {

    @Override
    public IPage<QualityInspection> getQualityInspectionPage(QualityInspectionQueryParam queryParam) {
        Page<QualityInspection> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();

        // 根据项目ID查询
        if (queryParam.getProjectId() != null) {
            queryWrapper.eq("project_id", queryParam.getProjectId());
        }

        // 根据检查类型查询
        if (queryParam.getInspectionType() != null) {
            queryWrapper.eq("inspection_type", queryParam.getInspectionType());
        }

        // 根据检查结果查询
        if (queryParam.getCheckResult() != null) {
            queryWrapper.eq("check_result", queryParam.getCheckResult());
        }

        // 根据整改状态查询
        if (queryParam.getRectificationStatus() != null) {
            queryWrapper.eq("rectification_status", queryParam.getRectificationStatus());
        }

        // 根据检查名称模糊查询
        if (StringUtils.hasText(queryParam.getInspectionName())) {
            queryWrapper.like("inspection_name", queryParam.getInspectionName());
        }

        // 根据检查编号查询
        if (StringUtils.hasText(queryParam.getInspectionNo())) {
            queryWrapper.eq("inspection_no", queryParam.getInspectionNo());
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");

        return this.page(page, queryWrapper);
    }

    @Override
    public List<QualityInspection> getByProjectId(Long projectId) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        queryWrapper.orderByDesc("create_time");
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByInspectionType(Integer inspectionType) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_type", inspectionType);
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByInspectionStage(Integer inspectionStage) {
        // 达梦数据库表中没有inspection_stage字段，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<QualityInspection> getByCheckResult(Integer checkResult) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_result", checkResult);
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByInspectorLeaderId(Long inspectorLeaderId) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspector_id", inspectorLeaderId);
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByInspectedUnit(String inspectedUnit) {
        // 达梦数据库表中没有inspected_unit字段，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<QualityInspection> getByRectificationStatus(Integer rectificationStatus) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rectification_status", rectificationStatus);
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByRectificationResponsibleId(Long rectificationResponsibleId) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rectification_person_id", rectificationResponsibleId);
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByRecheckResult(Integer recheckResult) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recheck_result", recheckResult);
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByQualityGrade(Integer qualityGrade) {
        // 达梦数据库表中没有quality_grade字段，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<QualityInspection> getByCreateBy(Long createBy) {
        // 达梦数据库表中没有create_by字段，返回空列表
        return new ArrayList<>();
    }

    @Override
    public Integer countByCheckResult(Integer checkResult) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_result", checkResult);
        return Math.toIntExact(this.count(queryWrapper));
    }

    @Override
    public Integer countByRectificationStatus(Integer rectificationStatus) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rectification_status", rectificationStatus);
        return Math.toIntExact(this.count(queryWrapper));
    }

    @Override
    public Integer countByQualityGrade(Integer qualityGrade) {
        // 达梦数据库表中没有quality_grade字段，返回0
        return 0;
    }

    @Override
    public List<QualityInspection> getUnqualified() {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_result", 2); // 不合格
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getWithSeriousIssues() {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("identified_issues");
        queryWrapper.ne("identified_issues", "");
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getPendingRectification() {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rectification_status", 1); // 待整改
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getRectificationOverdue() {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("rectification_deadline", new Date());
        queryWrapper.in("rectification_status", 1, 2); // 待整改或整改中
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getExcellentGrade() {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_result", 1); // 合格
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateRectificationStatus(List<Long> ids, Integer rectificationStatus, Long updateBy) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Long id : ids) {
            QualityInspection inspection = this.getById(id);
            if (inspection != null) {
                inspection.setRectificationStatus(rectificationStatus.shortValue());
                inspection.setUpdateTime(new Date());
                if (this.updateById(inspection)) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchUpdateCheckResult(List<Long> ids, Integer checkResult, Long updateBy) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Long id : ids) {
            QualityInspection inspection = this.getById(id);
            if (inspection != null) {
                inspection.setCheckResult(checkResult.shortValue());
                inspection.setUpdateTime(new Date());
                if (this.updateById(inspection)) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public List<QualityInspection> getByDateRange(Date startDate, Date endDate) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge("inspection_date", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("inspection_date", endDate);
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<QualityInspection> getByScoreRange(Integer minScore, Integer maxScore) {
        // 达梦数据库表中没有score字段，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<QualityInspection> getQualityInspectionStatistics() {
        // 返回所有质量检查记录用于统计
        return this.list();
    }

    @Override
    public List<QualityInspection> searchByKeyword(String keyword, Integer limit) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                .like("inspection_name", keyword)
                .or()
                .like("inspection_no", keyword)
                .or()
                .like("inspection_scope", keyword)
            );
        }
        if (limit != null && limit > 0) {
            queryWrapper.last("LIMIT " + limit);
        }
        return this.list(queryWrapper);
    }

    @Override
    public Double calculateAverageScore(Long projectId) {
        // 达梦数据库表中没有score字段，返回0.0
        return 0.0;
    }

    @Override
    public Double calculatePassRate(Long projectId) {
        QueryWrapper<QualityInspection> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("project_id", projectId);
        long total = this.count(totalWrapper);

        if (total == 0) {
            return 0.0;
        }

        QueryWrapper<QualityInspection> passWrapper = new QueryWrapper<>();
        passWrapper.eq("project_id", projectId);
        passWrapper.eq("check_result", 1); // 合格
        long passed = this.count(passWrapper);

        return (double) passed / total * 100;
    }

    @Override
    public List<QualityInspection> getUpcomingRectificationDeadlines(Integer days) {
        QueryWrapper<QualityInspection> queryWrapper = new QueryWrapper<>();
        Date futureDate = new Date(System.currentTimeMillis() + days * 24 * 60 * 60 * 1000L);
        queryWrapper.between("rectification_deadline", new Date(), futureDate);
        queryWrapper.in("rectification_status", 1, 2); // 待整改或整改中
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rectifyIssues(Long id, String rectificationDescription, Long rectificationPersonId, Long updateBy) {
        QualityInspection inspection = getById(id);
        if (inspection == null) {
            return false;
        }

        inspection.setRectificationStatus((short) 3); // 已整改
        inspection.setRectificationDescription(rectificationDescription);
        inspection.setRectificationPersonId(rectificationPersonId);
        inspection.setRectificationCompletionDate(new Date());
        inspection.setUpdateTime(new Date());
        
        return updateById(inspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean verifyRectification(Long id, Integer recheckResult, String recheckComments, Long updateBy) {
        QualityInspection inspection = getById(id);
        if (inspection == null) {
            return false;
        }

        inspection.setRecheckResult(recheckResult.shortValue());
        inspection.setRecheckComments(recheckComments);
        inspection.setRecheckDate(new Date());
        inspection.setUpdateTime(new Date());

        // 根据复查结果更新整改状态
        if (recheckResult == 1) {
            inspection.setRectificationStatus((short) 4); // 已验收
        } else {
            inspection.setRectificationStatus((short) 2); // 整改中，需要重新整改
        }

        return updateById(inspection);
    }

    @Override
    public String generateInspectionNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        long timestamp = System.currentTimeMillis() % 10000;
        return "QI" + dateStr + String.format("%04d", timestamp);
    }

    @Override
    public Boolean validateInspectionInfo(QualityInspection qualityInspection) {
        if (qualityInspection == null) {
            return false;
        }

        // 验证必填字段
        if (qualityInspection.getProjectId() == null ||
            !StringUtils.hasText(qualityInspection.getInspectionName()) ||
            qualityInspection.getInspectionType() == null ||
            qualityInspection.getInspectionDate() == null ||
            qualityInspection.getInspectorId() == null) {
            return false;
        }

        // 验证检查类型范围
        if (qualityInspection.getInspectionType() < 1 || qualityInspection.getInspectionType() > 4) {
            return false;
        }

        // 验证检查结果范围
        if (qualityInspection.getCheckResult() != null) {
            if (qualityInspection.getCheckResult() < 1 || qualityInspection.getCheckResult() > 3) {
                return false;
            }
        }

        // 验证整改状态范围
        if (qualityInspection.getRectificationStatus() != null) {
            if (qualityInspection.getRectificationStatus() < 1 || qualityInspection.getRectificationStatus() > 4) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> getProjectQualityReport(Long projectId) {
        List<String> report = new ArrayList<>();

        // 查询项目质量检查记录
        List<QualityInspection> inspections = getByProjectId(projectId);

        if (inspections.isEmpty()) {
            report.add("该项目暂无质量检查记录");
            return report;
        }

        // 统计质量检查情况
        int totalCount = inspections.size();
        int qualifiedCount = 0;
        int unqualifiedCount = 0;
        int pendingRectificationCount = 0;
        int overdueRectificationCount = 0;
        int hasIssuesCount = 0;

        for (QualityInspection inspection : inspections) {
            // 统计检查结果
            if (inspection.getCheckResult() != null) {
                if (inspection.getCheckResult() == 1) {
                    qualifiedCount++;
                } else if (inspection.getCheckResult() == 2) {
                    unqualifiedCount++;
                }
            }

            // 统计整改情况
            if (inspection.getRectificationStatus() != null && inspection.getRectificationStatus() == 1) {
                pendingRectificationCount++;
            }

            // 统计逾期整改
            if (inspection.isRectificationOverdue()) {
                overdueRectificationCount++;
            }

            // 统计有问题的检查
            if (StringUtils.hasText(inspection.getIdentifiedIssues())) {
                hasIssuesCount++;
            }
        }

        // 生成报告
        report.add("=== 项目质量检查报告 ===");
        report.add("检查总数: " + totalCount);
        report.add("合格数量: " + qualifiedCount);
        report.add("不合格数量: " + unqualifiedCount);
        report.add("合格率: " + String.format("%.2f%%", (double) qualifiedCount / totalCount * 100));
        report.add("发现问题检查数: " + hasIssuesCount);
        report.add("待整改数量: " + pendingRectificationCount);
        report.add("逾期整改数量: " + overdueRectificationCount);

        return report;
    }
}
