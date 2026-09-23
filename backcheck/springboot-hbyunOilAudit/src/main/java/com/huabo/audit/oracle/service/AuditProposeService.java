package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsProposeAdopt;
import com.huabo.audit.oracle.entity.TblYqnsProposeEntity;

/**
 * @ Author: Striker dev@example.com
 * @ Date: 2023-09-07 15:45
 * @ TODO:
 **/
public interface AuditProposeService {


    /**
     * 审计建议-保存新增的数据
     *
     * @param param
     * @param token 
     */
    JsonBean saveOrUpdate(TblYqnsProposeEntity param, String token) throws Exception;

    /**
     * 审计建议-删除数据
     *
     * @param id
     * @return
     */

    JsonBean delete(BigDecimal id);

    /**
     * 审计建议-获取所有的建议信息
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    JsonBean getProposeList(Integer pageNumber, Integer pageSize, String title);

    /**
     * 审计建议-根据ID获取建议数据
     * @param proposeId
     * @param wtzgid 
     * @return
     */
    JsonBean getProposeById(BigDecimal proposeId, BigDecimal wtzgid);

	JsonBean saveAuditProposeAdopt(TblYqnsProposeAdopt adopt, String token) throws Exception;

	JsonBean getAuditProposeAdopt(String adoptId, String token) throws Exception;
}
