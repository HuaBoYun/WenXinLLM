package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectXFEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProposalNoticeXfService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企模块-计划编制-央企模块-计划编制-工程审计项目 下发-分发表
 */
public interface TblYqnsEnginAuditProjectXFEntityService extends IService<TblYqnsEnginAuditProjectXFEntity> {


    /**
     * 根据审计立项建议通知-分发表id查询审计立项建议通知-分发表
     *
     * @param enginId
     * @return
     * @throws Exception
     */
    JsonBean getListByEnginId(String token, String enginId) throws Exception;


    /**
     * 保存或更新审计立项建议通知-分发表
     *
     * @param enginId 1
     * @param userIds 1,2,
     * @return
     */
    JsonBean saveOrUpdate(String token,  String enginId , String userIds) throws Exception;


    /**
     * 删除所有分发人员
     *
     * @param enginId 1
     * @return
     */
    JsonBean delete(String token,  String enginId ) throws Exception;


}
