package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeNameEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTypeNameEntity;

import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTypeNameService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计类型-NAME
 */
public interface TblYqnsProjectAuditTypeNameService extends IService<TblYqnsProjectAuditTypeNameEntity> {



    /**
     * 根据工程审计类型id查询工程审计类型-NAME子列表
     * @param typeId
     * @return
     */
    JsonBean getListByTypeId(String token, String typeId)  throws Exception;


    /**
     * 根据工程审计类型id查询工程审计类型-NAME子列表
     * @param typeId
     * @return
     * @throws Exception
     */
    List<TblYqnsProjectAuditTypeNameEntity> getListByTypeId(String typeId);


    /**
     * 保存或更新工程审计类型-NAME子列表
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsProjectAuditTypeNameEntity vo)  throws Exception;


    /**
     * 保存或更新工程审计类型List-NAME子列表
     * @param voList
     * @return
     */
    JsonBean saveOrUpdateList(String token, List<TblYqnsProjectAuditTypeNameEntity> voList)  throws Exception;

    /**
     * 删除工程审计类型-NAME子列表(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;

    
}
