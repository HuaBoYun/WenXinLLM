package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblCurrentYearAuditIssuesEntity;
import com.huabo.audit.util.PageResult;

import java.util.List;

public interface TblCurrentYearAuditIssuesService extends IService<TblCurrentYearAuditIssuesEntity> {
    PageResult<TblCurrentYearAuditIssuesEntity> page(Integer pageNumber, Integer pageSize, TblCurrentYearAuditIssuesEntity entity);

    List<TblCurrentYearAuditIssuesEntity> list(TblCurrentYearAuditIssuesEntity entity);
}
