package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditOverseeRecordsService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-审计实施-审计督导记录
 */
public interface TblYqnsAuditOverseeRecordsService extends IService<TblYqnsAuditOverseeRecordsEntity> {


    /**
     * 查询审计督导记录列表
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @param staffId 
     * @param xmnd 
     * @return
     */
    JsonBean getRecordsList(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditOverseeRecordsEntity vo, Integer xmnd, BigDecimal staffId) throws Exception;


    /**
     * 根据审计督导记录id查询审计督导记录
     * @param id
     * @return
     */
    JsonBean getRecordsById(String token, Long id)  throws Exception;

    /**
     * 保存或更新审计督导记录
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsAuditOverseeRecordsEntity vo,String attids)  throws Exception;

    /**
     * 删除审计督导记录(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;

    
}
