package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectXFEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundtb;
import com.huabo.audit.oracle.vo.SjdwjdVo;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProposalNoticeXfService
 * @PACKAGE_NAME: com.huabo.audit.oracle.service
 * @date 2023/10/10 11:20.
 * @version: V1.0
 * @description: 央企模块-计划编制-央企模块-计划编制-财务审计项目 下发-分发表
 */
public interface TblYqnsFundAuditProjectXFEntityService extends IService<TblYqnsFundAuditProjectXFEntity> {


    /**
     * 根据财务审计项目 -分发表id查询财务审计项目 -分发表
     *
     * @param fundId
     * @return
     * @throws Exception
     */
    JsonBean getListByFundId(String token, String fundId) throws Exception;


    /**
     * 保存或更新财务审计项目 -分发表
     *
     * @param fundId 1
     * @param userIds 1,2,
     * @return
     */
    JsonBean saveOrUpdate(String token,  String fundId , String userIds) throws Exception;


    /**
     * 删除所有分发人员
     *
     * @param fundId 1
     * @return
     */
    JsonBean delete(String token,  String fundId ) throws Exception;

    
    

}
