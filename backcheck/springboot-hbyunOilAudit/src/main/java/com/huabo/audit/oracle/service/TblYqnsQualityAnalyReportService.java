package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsQualityAnalyReportEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditOverseeRecordsService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-审计实施-质量分析报告
 */
public interface TblYqnsQualityAnalyReportService extends IService<TblYqnsQualityAnalyReportEntity> {


    /**
     * 查询质量分析报告列表
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    JsonBean getReportList(String token, Integer pageNumber, Integer pageSize, TblYqnsQualityAnalyReportEntity vo) throws Exception;


    /**
     * 根据质量分析报告id查询质量分析报告
     * @param id
     * @return
     */
    JsonBean getReportById(String token, Long id)  throws Exception;

    /**
     * 保存或更新质量分析报告
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsQualityAnalyReportEntity vo)  throws Exception;

    /**
     * 删除质量分析报告(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;



    /**
     * 删除质量分析报告 附件
     * @param token
     * @param attId
     * @return
     * @throws Exception
     */
    JsonBean deleteFileAttach(String token,String attId) throws Exception;

}
