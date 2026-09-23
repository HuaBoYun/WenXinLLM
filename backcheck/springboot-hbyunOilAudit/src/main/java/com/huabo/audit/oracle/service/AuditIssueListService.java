package com.huabo.audit.oracle.service;


import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;

/**
 * @ Author: Striker dev@example.com
 * @ Date: 2023-09-11 16:15
 * @ TODO:
 **/
public interface AuditIssueListService {

    /**
     * 添加和修改问题清单信息
     *
     * @param param
     * @return
     */
    JsonBean saveOrUpdate(TblYqnsIssueListEntity param);

    /**
     * 根据清单表ID查询数据
     *
     * @param issueId
     * @return
     */
    JsonBean getIssueById(BigDecimal issueId);

    /**
     * 查询所有的问题信息
     *
     * @param param
     * @return
     */
    JsonBean getAllIssueInfo(Integer pageNumber, Integer pageSize, TblYqnsIssueListEntity param);

    /**
     * 删除关联表中的附件ID
     *
     * @param issueId
     * @param fileIds
     * @return
     */
    JsonBean deleteAttachFile(BigDecimal issueId, String fileIds);

    /**
     * 删除问题数据
     *
     * @param id
     * @return
     */
    JsonBean delete(BigDecimal id);

	JsonBean getPreAddInfo(String token) throws Exception;

	JsonBean excelUtils(HttpServletResponse response, String token, TblYqnsIssueListEntity param, List<String> idList) throws Exception;

	JsonBean getAuditOrgList(String token, BigDecimal sjbgdgid) throws Exception;

	JsonBean assignment(String token, BigDecimal issueId, BigDecimal rectPerson, String rectPersonName) throws Exception;
}
