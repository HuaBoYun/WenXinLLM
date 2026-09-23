package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsAuditMethodMaintainEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMethodMaintainService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-基础配置-审计方法维护
 */
public interface TblYqnsAuditMethodMaintainService extends IService<TblYqnsAuditMethodMaintainEntity> {


    /**
     * 查询审计方法维护列表-分页 - 模板 zero
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    JsonBean getMethodMaintainPage(String token, Integer pageNumber, Integer pageSize, TblYqnsAuditMethodMaintainEntity vo) throws Exception;


    /**
     * 查询审计方法维护列表 - 模板 zero
     * @param vo
     * @return
     */
    JsonBean getMethodMaintainList(String token, TblYqnsAuditMethodMaintainEntity vo) throws Exception;

    /**
     * 根据审计方法维护id查询审计方法维护
     * @param id
     * @return
     */
    JsonBean getMethodMaintainById(String token, Long id)  throws Exception;



    /**
     * 根据审计方法维护id和TypeId 查询审计方法维护
     * @param id
     * @return
     */
    TblYqnsAuditMethodMaintainEntity getMethodMaintainById(Long id);

    /**
     * 保存或更新审计方法维护
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsAuditMethodMaintainEntity vo)  throws Exception;

    /**
     * 删除审计方法维护(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;

    
}
