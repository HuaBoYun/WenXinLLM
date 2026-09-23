package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblAuditPlanExecutionStatisticsEntity;
import com.huabo.audit.oracle.mapper.TblAuditPlanExecutionStatisticsMapper;
import com.huabo.audit.service.TblAuditPlanExecutionStatisticsService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TblAuditPlanExecutionStatisticsServiceImpl extends ServiceImpl<TblAuditPlanExecutionStatisticsMapper, TblAuditPlanExecutionStatisticsEntity> implements TblAuditPlanExecutionStatisticsService {

    @Autowired
    private TblAuditPlanExecutionStatisticsMapper tblAuditPlanExecutionStatisticsMapper;

    @Override
    public PageResult<TblAuditPlanExecutionStatisticsEntity> page(Integer pageNumber, Integer pageSize, TblAuditPlanExecutionStatisticsEntity entity) {
        //分页查询
        PageInfo<TblAuditPlanExecutionStatisticsEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.list(entity));
        return new PageResult<TblAuditPlanExecutionStatisticsEntity>().build(pageInfo);
    }


    /**
     * 按照条件查询列表
     * @param entity
     * @return
     */
    @Override
    public List<TblAuditPlanExecutionStatisticsEntity> list(TblAuditPlanExecutionStatisticsEntity entity) {
        //设置查询条件
        LambdaQueryWrapper<TblAuditPlanExecutionStatisticsEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(entity.getUnitName())) {
            //单位名称
            lambdaQueryWrapper.like(TblAuditPlanExecutionStatisticsEntity::getUnitName,entity.getUnitName());
        }
        if (StringUtils.isNotBlank(entity.getAuditProjectName())) {
            //审计项目名称
            lambdaQueryWrapper.like(TblAuditPlanExecutionStatisticsEntity::getAuditProjectName,entity.getAuditProjectName());
        }
        if (StringUtils.isNotBlank(entity.getAuditedEntityName())) {
            //被审计单位全称
            lambdaQueryWrapper.like(TblAuditPlanExecutionStatisticsEntity::getAuditedEntityName,entity.getAuditedEntityName());
        }
        if (StringUtils.isNotBlank(entity.getAuditProjectType())) {
            //审计项目类型
            lambdaQueryWrapper.eq(TblAuditPlanExecutionStatisticsEntity::getAuditProjectType,entity.getAuditProjectType());
        }
        if (StringUtils.isNotBlank(entity.getTeamLeader())) {
            //组长
            lambdaQueryWrapper.eq(TblAuditPlanExecutionStatisticsEntity::getTeamLeader,entity.getTeamLeader());
        }
        if (StringUtils.isNotBlank(entity.getPrincipalAuditor())) {
            //主审
            lambdaQueryWrapper.eq(TblAuditPlanExecutionStatisticsEntity::getPrincipalAuditor,entity.getPrincipalAuditor());
        }
        if (StringUtils.isNotBlank(entity.getIsOnSiteAuditCompleted())) {
            //是否已完成现场审计
            lambdaQueryWrapper.eq(TblAuditPlanExecutionStatisticsEntity::getIsOnSiteAuditCompleted,entity.getIsOnSiteAuditCompleted());
        }
        if (StringUtils.isNotBlank(entity.getIsAuditReportIssued())) {
            //是否已出具审计报告
            lambdaQueryWrapper.eq(TblAuditPlanExecutionStatisticsEntity::getIsAuditReportIssued,entity.getIsAuditReportIssued());
        }

        if (Objects.nonNull(entity.getPlannedOnSiteAuditCompletionDateStart()) && Objects.nonNull(entity.getPlannedOnSiteAuditCompletionDateEnd())) {
            lambdaQueryWrapper.le(TblAuditPlanExecutionStatisticsEntity::getPlannedOnSiteAuditCompletionDate,entity.getPlannedOnSiteAuditCompletionDateEnd());
            lambdaQueryWrapper.ge(TblAuditPlanExecutionStatisticsEntity::getPlannedOnSiteAuditCompletionDate,entity.getPlannedOnSiteAuditCompletionDateStart());
        }
        if (Objects.nonNull(entity.getPlannedAuditReportIssueDateStart()) && Objects.nonNull(entity.getPlannedAuditReportIssueDateEnd())) {
            lambdaQueryWrapper.le(TblAuditPlanExecutionStatisticsEntity::getPlannedAuditReportIssueDate,entity.getPlannedAuditReportIssueDateEnd());
            lambdaQueryWrapper.ge(TblAuditPlanExecutionStatisticsEntity::getPlannedAuditReportIssueDate,entity.getPlannedAuditReportIssueDateStart());
        }

        //创建时间倒序
        lambdaQueryWrapper.orderByDesc(TblAuditPlanExecutionStatisticsEntity::getCreateTime);

        return tblAuditPlanExecutionStatisticsMapper.selectList(lambdaQueryWrapper);
    }
}
