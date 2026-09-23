package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblCurrentYearAuditIssuesEntity;
import com.huabo.audit.oracle.mapper.TblCurrentYearAuditIssuesMapper;
import com.huabo.audit.service.TblCurrentYearAuditIssuesService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TblCurrentYearAuditIssuesServiceImpl extends ServiceImpl<TblCurrentYearAuditIssuesMapper, TblCurrentYearAuditIssuesEntity> implements TblCurrentYearAuditIssuesService {

    @Autowired
    private TblCurrentYearAuditIssuesMapper tblCurrentYearAuditIssuesMapper;

    @Override
    public PageResult<TblCurrentYearAuditIssuesEntity> page(Integer pageNumber, Integer pageSize, TblCurrentYearAuditIssuesEntity entity) {
        //分页查询
        PageInfo<TblCurrentYearAuditIssuesEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.list(entity));
        return new PageResult<TblCurrentYearAuditIssuesEntity>().build(pageInfo);
    }


    /**
     * 按照条件查询列表
     * @param entity
     * @return
     */
    @Override
    public List<TblCurrentYearAuditIssuesEntity> list(TblCurrentYearAuditIssuesEntity entity) {
        //设置查询条件
        LambdaQueryWrapper<TblCurrentYearAuditIssuesEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(entity.getAuditImplementingUnit())) {
            //审计实施单位名称
            lambdaQueryWrapper.like(TblCurrentYearAuditIssuesEntity::getAuditImplementingUnit,entity.getAuditImplementingUnit());
        }
        if (StringUtils.isNotBlank(entity.getAuditProjectName())) {
            //审计项目名称
            lambdaQueryWrapper.like(TblCurrentYearAuditIssuesEntity::getAuditProjectName,entity.getAuditProjectName());
        }
        if (StringUtils.isNotBlank(entity.getIssueResponsibleUnit())) {
            //整改责任单位
            lambdaQueryWrapper.like(TblCurrentYearAuditIssuesEntity::getIssueResponsibleUnit,entity.getIssueResponsibleUnit());
        }
        if (StringUtils.isNotBlank(entity.getAuditProjectType())) {
            //审计项目类型
            lambdaQueryWrapper.eq(TblCurrentYearAuditIssuesEntity::getAuditProjectType,entity.getAuditProjectType());
        }
        if (StringUtils.isNotBlank(entity.getHasSubmittedRectificationApplication())) {
            //是否提交整改销号申请
            lambdaQueryWrapper.eq(TblCurrentYearAuditIssuesEntity::getHasSubmittedRectificationApplication,entity.getHasSubmittedRectificationApplication());
        }
        if (StringUtils.isNotBlank(entity.getIsRectificationCompleted())) {
            //是否已完成整改销号
            lambdaQueryWrapper.eq(TblCurrentYearAuditIssuesEntity::getIsRectificationCompleted,entity.getIsRectificationCompleted());
        }
        if (StringUtils.isNotBlank(entity.getPrimaryRectificationResponsiblePerson())) {
            //整改第一责任人
            lambdaQueryWrapper.eq(TblCurrentYearAuditIssuesEntity::getPrimaryRectificationResponsiblePerson,entity.getPrimaryRectificationResponsiblePerson());
        }
        if (StringUtils.isNotBlank(entity.getAssistingRectificationLeader())) {
            //协助整改工作的领导
            lambdaQueryWrapper.eq(TblCurrentYearAuditIssuesEntity::getAssistingRectificationLeader,entity.getAssistingRectificationLeader());
        }

        if (Objects.nonNull(entity.getRectificationDeadlineStart()) && Objects.nonNull(entity.getRectificationDeadlineEnd())) {
            lambdaQueryWrapper.le(TblCurrentYearAuditIssuesEntity::getRectificationDeadline,entity.getRectificationDeadlineEnd());
            lambdaQueryWrapper.ge(TblCurrentYearAuditIssuesEntity::getRectificationDeadline,entity.getRectificationDeadlineStart());
        }

        //创建时间倒序
        lambdaQueryWrapper.orderByDesc(TblCurrentYearAuditIssuesEntity::getCreateTime);

        return tblCurrentYearAuditIssuesMapper.selectList(lambdaQueryWrapper);
    }
}
