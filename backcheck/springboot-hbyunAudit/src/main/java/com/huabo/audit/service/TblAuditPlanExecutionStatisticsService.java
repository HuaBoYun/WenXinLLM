package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblAuditPlanExecutionStatisticsEntity;
import com.huabo.audit.util.PageResult;

import java.util.List;

public interface TblAuditPlanExecutionStatisticsService extends IService<TblAuditPlanExecutionStatisticsEntity> {
    PageResult<TblAuditPlanExecutionStatisticsEntity> page(Integer pageNumber, Integer pageSize, TblAuditPlanExecutionStatisticsEntity entity);

    List<TblAuditPlanExecutionStatisticsEntity> list(TblAuditPlanExecutionStatisticsEntity entity);
}
