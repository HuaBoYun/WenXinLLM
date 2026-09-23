package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuVerifyEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuVerifyEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuVerifyEntity;

import java.util.List;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditMyManuVerifyService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的底稿-下方审计查证事实
 */
public interface TblYqnsAuditMyManuVerifyService extends IService<TblYqnsAuditMyManuVerifyEntity> {


    /**
     * 根据我的底稿id查询 下方审计查证事实 子列表
     * @param myManuscriptId 我的底稿id
     * @return
     */
    JsonBean getListByMyManuscriptId(String token, String myManuscriptId)  throws Exception;


    /**
     * 根据我的底稿id查询 下方审计查证事实 子列表
     * @param myManuscriptId
     * @return
     * @throws Exception
     */
    List<TblYqnsAuditMyManuVerifyEntity> getListByMyManuscriptId(String myManuscriptId);


    /**
     * 保存或更新 下方审计查证事实 子列表
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsAuditMyManuVerifyEntity vo)  throws Exception;


    /**
     * 保存或更新 下方审计查证事实 子列表
     * @param voList
     * @return
     */
    JsonBean saveOrUpdateList(String token, List<TblYqnsAuditMyManuVerifyEntity> voList)  throws Exception;

    /**
     * 删除下方审计查证事实 子列表(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;


}
