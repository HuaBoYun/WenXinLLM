package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTypeService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计类型
 */
public interface TblYqnsProjectAuditTypeService extends IService<TblYqnsProjectAuditTypeEntity> {


    /**
     * 查询工程审计类型列表 分页
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    JsonBean getAuditTypePage(String token, Integer pageNumber, Integer pageSize, TblYqnsProjectAuditTypeEntity vo) throws Exception;

    /**
     * 查询工程审计类型列表 不分页 获取模板 Status=0
     * @param vo
     * @return
     */
    JsonBean getAuditTypeList(String token, TblYqnsProjectAuditTypeEntity vo) throws Exception;


    /**
     * 根据工程审计类型id查询工程审计类型
     * @param id
     * @return
     */
    JsonBean getAuditTypeById(String token, Long id)  throws Exception;

    /**
     * 根据工程审计类型id查询工程审计类型
     * @param ids
     * @return
     */
    JsonBean getAuditTypeByIds(String token, String ids)  throws Exception;


    /**
     * 保存或更新工程审计类型
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsProjectAuditTypeEntity vo)  throws Exception;

    /**
     * 删除工程审计类型(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;



    /**
     * 通过审计类型 查询他的子集
     * @param entity
     * @return
     */
    TblYqnsProjectAuditTypeEntity packageEntity(TblYqnsProjectAuditTypeEntity entity) ;


    /**
     * 查询工程审计类型列表-通过List<Id> ids 【获取模板】
     * @return
     * @throws Exception
     */
    List<TblYqnsProjectAuditTypeEntity> getAuditTypeListByIds(String ids) throws Exception;


    /**
     * 获取同一模板下面的工程审计类型列表-通过templateId  【获取模板】
     * @param templateId
     * @return
     * @throws Exception
     */
    List<TblYqnsProjectAuditTypeEntity> getAuditTypeListByTemplateId(String templateId) throws Exception;





    /**
     * 保存复制工程审计类型及关联
     * @param token
     * @param addDiffTypeEntityList
     * @return
     * @throws Exception
     */
    List<TblYqnsProjectAuditTypeEntity>  saveOrUpdateCP(String token,List<TblYqnsProjectAuditTypeEntity>  addDiffTypeEntityList)  throws Exception;

    /**
     * 删除 复制无关联-工程审计类型及关联-
     * @param token
     * @param deleteDiffTypeEntityList
     * @return
     * @throws Exception
     */
    JsonBean deleteCP(String token,List<TblYqnsProjectAuditTypeEntity>  deleteDiffTypeEntityList) throws Exception;
    
    
    /**
     * 查询工程审计类型列表-通过List<Id> ids 【获取模板】
     * @return
     * @throws Exception
     */
    List<TblYqnsProjectAuditTypeEntity> gettaskListByIds(String ids,Long templateId,BigDecimal projectId) throws Exception;
    
    /**
     * 督导任务查询模板内容
     * @param ids
     * @param typeid
     * @param projectId
     * @return
     * @throws Exception
     */
    List<TblYqnsProjectAuditTypeEntity> getddtaskListByIds(String ids,BigDecimal projectId) throws Exception;

}
