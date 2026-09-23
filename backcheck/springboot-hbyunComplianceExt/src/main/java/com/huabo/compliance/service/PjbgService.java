package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblAttachment;
import com.huabo.compliance.entity.TblReport;

import java.math.BigDecimal;
import java.util.List;

public interface PjbgService {
    IPage<TblReport> findAll(Integer pageNumber, String name, String startDate, String endDate, String type, BigDecimal orgid,Integer pageSize);

    void saveReport(TblReport report, String attids);

    List<TblAttachment> getRepAttByReportId(BigDecimal reportid);

    void saveRepAtt(TblAttachment a, String reportid);

    void delAttAndRepAtt(BigDecimal attid, String reportid);

    void delReport(String ids);
}
