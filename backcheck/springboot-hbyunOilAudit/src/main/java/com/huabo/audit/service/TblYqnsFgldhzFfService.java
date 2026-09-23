package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsFgldhzFf;

import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsFgldhzFfService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-计划编制-分管领导汇总表-分发表
 */
public interface TblYqnsFgldhzFfService extends IService<TblYqnsFgldhzFf> {


    /**
     * 根据管领导汇总表-分发表id查询管领导汇总表-分发表
     *
     * @param fgldhzId
     * @return
     * @throws Exception
     */
    JsonBean getListByFgldhzId(String token, String fgldhzId) throws Exception;


    /**
     * 保存或更新管领导汇总表-分发表
     *
     * @param fgldhzId 1
     * @param userIds 1,2,
     * @return
     */
    JsonBean saveOrUpdate(String token,  String fgldhzId , String userIds) throws Exception;


    /**
     * 删除所有分发人员
     *
     * @param fgldhzId 1
     * @return
     */
    JsonBean delete(String token,  String fgldhzId ) throws Exception;


}
