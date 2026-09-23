package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTemplateEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTemplateService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计模板
 */
public interface TblYqnsProjectAuditTemplateService extends IService<TblYqnsProjectAuditTemplateEntity> {


    /**
     * 查询工程审计模板列表-分页
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    JsonBean getAuditTemplatePage(String token, Integer pageNumber, Integer pageSize, TblYqnsProjectAuditTemplateEntity vo) throws Exception;

    /**
     * 查询工程审计模板列表-明细
     * @param vo
     * @return
     */
    JsonBean getAuditTemplateList(String token, TblYqnsProjectAuditTemplateEntity vo) throws Exception;



    /**
     * 根据工程审计模板id查询工程审计模板
     * @param id
     * @return
     */
    JsonBean getAuditTemplateById(String token, Long id)  throws Exception;

    /**
     * 保存或更新工程审计模板
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsProjectAuditTemplateEntity vo)  throws Exception;

    /**
     * 复制整套工程审计模板
     * @param templateId 原工程模板id
     * @return
     */
    JsonBean cpTemplateByTemplateId(String token,String templateId)  throws Exception;


    /**
     * 删除工程审计模板(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;


    /**
     * 获取单一工程审计模板下的所有子集 -复制数据
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    JsonBean previewTemplateById(String token,Long id) throws Exception;
    
    
    /**
     * 我的任务-获取模板内容
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    JsonBean mytaskTemplateById(String token,Long id,Long templateId,BigDecimal projectId) throws Exception;
    
    
    /**
     * 督导任务-获取模版内容
     * @param token
     * @param id
     * @param templateId
     * @param projectId
     * @return
     * @throws Exception
     */
    public JsonBean ddtaskTemplateById(String token,Long id,BigDecimal projectId)throws Exception;

}
