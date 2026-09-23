package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblRiskReportingEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TblRiskReportingService {


    /**
     * 相关风险事件填报列表
     * smf 2024-10-31
     * @param token
     * @param pageNumber
     * @param pageSize
     */
    JsonBean queryRiskReportingAll(String token, Integer pageNumber, Integer pageSize,String entName ) throws Exception;

    /**
     * 相关风险事件填报新增
     * @param tblRiskReporting
     * @return
     */
    JsonBean insertOrUpdateReporting(TblRiskReportingEntity tblRiskReporting,String token,String attIds) throws Exception;

    /**
     * 相关风险事件填报详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    JsonBean riskReportingDetails(String id, String token) throws Exception;

    /**
     * 相关风险事件填报删除
     * @param id
     * @return
     */
    void riskReportingDelete(String id);

    /**
     * 相关风险事件填报的附件
     * @param token
     * @param id
     * @return
     */
    JsonBean getRiskReportingAttInfo(String token, BigDecimal id) throws Exception;
    
    
    List<TblRiskReportingEntity>  exportRiskReport(String entname,String id) throws Exception;
}
