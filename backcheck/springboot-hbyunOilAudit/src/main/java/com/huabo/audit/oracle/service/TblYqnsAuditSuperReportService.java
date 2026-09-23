package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsAuditSuperReport;

public interface TblYqnsAuditSuperReportService extends IService<TblYqnsAuditSuperReport> {


    /**
     * 查询列表
     * @param entity
     * @return
     */
    JsonBean selectReportList(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditSuperReport entity) throws Exception;

    /**
     * 获取单条报告详情
     * @param id
     * @return
     */
    JsonBean selectReportById(Long id) throws Exception;

    /**
     * 新增一条审计督导报告
     * @param tblYqnsAuditSuperReport
     * @return
     */
    JsonBean saveReport(String token, TblYqnsAuditSuperReport tblYqnsAuditSuperReport) throws Exception;

    /**
     * 修改审计督导报告
     * @param tblYqnsAuditSuperReport
     * @return
     */
    JsonBean updateReport(String token, TblYqnsAuditSuperReport tblYqnsAuditSuperReport) throws Exception;

    /**
     * 删除审计督导报告
     * @param ids
     * @return
     */
    JsonBean deleteReoprt(String token,String ids) throws Exception;


    /**
     * 删除质量分析报告 附件
     * @param token
     * @param attId
     * @return
     * @throws Exception
     */
    JsonBean deleteFileAttach(String token,String attId) throws Exception;


}
