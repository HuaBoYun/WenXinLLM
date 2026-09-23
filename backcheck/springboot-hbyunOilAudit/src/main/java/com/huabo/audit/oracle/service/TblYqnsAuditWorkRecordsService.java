package com.huabo.audit.oracle.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsAuditWorkRecordsEntity;

/**
 * @Classname TblYqnsAuditWorkRecordsService
 * @Description TODO  央企内审-审计实施-审计工作记录 service
 * @Date 2023/10/8 21:55
 * @Created by GJ.C
 */
public interface TblYqnsAuditWorkRecordsService {

    /**
     * 查询审计工作记录列表
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    JsonBean getRecordsList(String token,Integer pageNumber, Integer pageSize, TblYqnsAuditWorkRecordsEntity vo) throws Exception;


    /**
     * 根据审计工作记录id查询审计项目记录
     * @param id
     * @return
     */
    JsonBean getRecordsById(String token, Long id)  throws Exception;

    /**
     * 保存或更新审计工作记录
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsAuditWorkRecordsEntity vo,String attids)  throws Exception;

    /**
     * 删除审计工作记录(逻辑删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;


	JsonBean getRecordsListByMyDraft(String token, Integer pageNumber, Integer pageSize,
			TblYqnsAuditWorkRecordsEntity vo) throws Exception;

}
