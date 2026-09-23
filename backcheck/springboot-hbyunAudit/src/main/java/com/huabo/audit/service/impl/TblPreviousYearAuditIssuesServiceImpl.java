package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblPreviousYearAuditIssuesEntity;
import com.huabo.audit.oracle.mapper.TblPreviousYearAuditIssuesMapper;
import com.huabo.audit.service.TblPreviousYearAuditIssuesService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TblPreviousYearAuditIssuesServiceImpl extends ServiceImpl<TblPreviousYearAuditIssuesMapper, TblPreviousYearAuditIssuesEntity> implements TblPreviousYearAuditIssuesService {

    @Autowired
    private TblPreviousYearAuditIssuesMapper tblPreviousYearAuditIssuesMapper;

    @Override
    public PageResult<TblPreviousYearAuditIssuesEntity> page(Integer pageNumber, Integer pageSize, TblPreviousYearAuditIssuesEntity entity) {
        //分页查询
        PageInfo<TblPreviousYearAuditIssuesEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.list(entity));
        return new PageResult<TblPreviousYearAuditIssuesEntity>().build(pageInfo);
    }


    /**
     * 按照条件查询列表
     * @param entity
     * @return
     */
    @Override
    public List<TblPreviousYearAuditIssuesEntity> list(TblPreviousYearAuditIssuesEntity entity) {
        //设置查询条件
        LambdaQueryWrapper<TblPreviousYearAuditIssuesEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(entity.getRectificationResponsibleUnit())) {
            //整改责任单位
            lambdaQueryWrapper.like(TblPreviousYearAuditIssuesEntity::getRectificationResponsibleUnit,entity.getRectificationResponsibleUnit());
        }
        if (StringUtils.isNotBlank(entity.getAuditProjectName())) {
            //审计项目名称
            lambdaQueryWrapper.like(TblPreviousYearAuditIssuesEntity::getAuditProjectName,entity.getAuditProjectName());
        }
        if (StringUtils.isNotBlank(entity.getAuditRectificationType())) {
            //审计整改类型
            lambdaQueryWrapper.like(TblPreviousYearAuditIssuesEntity::getAuditRectificationType,entity.getAuditRectificationType());
        }
        if (StringUtils.isNotBlank(entity.getHasSubmittedRectificationApplication())) {
            //是否提交整改销号申请
            lambdaQueryWrapper.eq(TblPreviousYearAuditIssuesEntity::getHasSubmittedRectificationApplication,entity.getHasSubmittedRectificationApplication());
        }
        if (StringUtils.isNotBlank(entity.getIsRectificationCompleted())) {
            //是否已完成整改销号
            lambdaQueryWrapper.eq(TblPreviousYearAuditIssuesEntity::getIsRectificationCompleted,entity.getIsRectificationCompleted());
        }
        if (StringUtils.isNotBlank(entity.getPrimaryRectificationResponsiblePerson())) {
            //整改第一责任人
            lambdaQueryWrapper.eq(TblPreviousYearAuditIssuesEntity::getPrimaryRectificationResponsiblePerson,entity.getPrimaryRectificationResponsiblePerson());
        }
        if (StringUtils.isNotBlank(entity.getAssistingRectificationLeader())) {
            //协助整改工作的领导
            lambdaQueryWrapper.eq(TblPreviousYearAuditIssuesEntity::getAssistingRectificationLeader,entity.getAssistingRectificationLeader());
        }

        if (Objects.nonNull(entity.getRectificationDeadlineStart()) && Objects.nonNull(entity.getRectificationDeadlineEnd())) {
            lambdaQueryWrapper.le(TblPreviousYearAuditIssuesEntity::getRectificationDeadline,entity.getRectificationDeadlineEnd());
            lambdaQueryWrapper.ge(TblPreviousYearAuditIssuesEntity::getRectificationDeadline,entity.getRectificationDeadlineStart());
        }

        //创建时间倒序
        lambdaQueryWrapper.orderByDesc(TblPreviousYearAuditIssuesEntity::getCreateTime);

        return tblPreviousYearAuditIssuesMapper.selectList(lambdaQueryWrapper);
    }
}
