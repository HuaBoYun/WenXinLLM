package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblPreviousYearAuditIssuesEntity;
import com.huabo.audit.util.PageResult;

import java.util.List;

public interface TblPreviousYearAuditIssuesService extends IService<TblPreviousYearAuditIssuesEntity> {
    PageResult<TblPreviousYearAuditIssuesEntity> page(Integer pageNumber, Integer pageSize, TblPreviousYearAuditIssuesEntity entity);

    List<TblPreviousYearAuditIssuesEntity> list(TblPreviousYearAuditIssuesEntity entity);
}
