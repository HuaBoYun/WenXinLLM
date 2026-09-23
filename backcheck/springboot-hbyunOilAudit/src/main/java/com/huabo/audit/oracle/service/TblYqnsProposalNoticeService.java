package com.huabo.audit.oracle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeEntity;
import com.huabo.audit.oracle.entity.TblYqnsProposalNoticeEntity;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProposalNoticeService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企模块-计划编制-审计立项建议通知
 */
public interface TblYqnsProposalNoticeService extends IService<TblYqnsProposalNoticeEntity> {


    /**
     * 查询审计立项建议通知列表
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     */
    JsonBean getNoticeList(String token, Integer pageNumber, Integer pageSize, TblYqnsProposalNoticeEntity vo) throws Exception;


    /**
     * 根据审计立项建议通知id查询审计立项建议通知
     * @param id
     * @return
     */
    JsonBean getNoticeById(String token, Long id)  throws Exception;

    /**
     * 保存或更新审计立项建议通知
     * @param vo
     * @return
     */
    JsonBean saveOrUpdate(String token,TblYqnsProposalNoticeEntity vo)  throws Exception;

    /**
     * 删除审计立项建议通知(直接删除)
     * @param id
     * @return
     */
    JsonBean delete(String token,Long id) throws Exception;


    /**
     * 删除审计立项建议通知 附件
     * @param token
     * @param attId
     * @return
     * @throws Exception
     */
    JsonBean deleteFileAttach(String token,String attId) throws Exception;

}
