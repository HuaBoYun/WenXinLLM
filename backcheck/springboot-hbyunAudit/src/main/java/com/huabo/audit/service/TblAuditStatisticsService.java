package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblAuditStatisticsEntity;
import com.huabo.audit.util.PageResult;

import java.util.List;

public interface TblAuditStatisticsService extends IService<TblAuditStatisticsEntity> {
    PageResult<TblAuditStatisticsEntity> page(Integer pageNumber, Integer pageSize, TblAuditStatisticsEntity entity);

    List<TblAuditStatisticsEntity> list(TblAuditStatisticsEntity entity);
}
