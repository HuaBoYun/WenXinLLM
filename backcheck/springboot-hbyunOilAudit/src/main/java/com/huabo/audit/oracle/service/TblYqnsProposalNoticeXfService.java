package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsFgldhzFf;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeXfEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProposalNoticeXfService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企模块-计划编制-审计立项建议通知-分发表
 */
public interface TblYqnsProposalNoticeXfService extends IService<TblYqnsProposalNoticeXfEntity> {


    /**
     * 根据审计立项建议通知-分发表id查询审计立项建议通知-分发表
     *
     * @param noticeId
     * @return
     * @throws Exception
     */
    JsonBean getListByNoticeId(String token, String noticeId) throws Exception;


    /**
     * 保存或更新审计立项建议通知-分发表
     *
     * @param noticeId 1
     * @param userIds 1,2,
     * @return
     */
    JsonBean saveOrUpdate(String token,  String noticeId , String userIds) throws Exception;


    /**
     * 删除所有分发人员
     *
     * @param noticeId 1
     * @return
     */
    JsonBean delete(String token,  String noticeId ) throws Exception;


}
