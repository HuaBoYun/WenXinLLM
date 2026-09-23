package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_WTZG(问题整改表)】的数据库操作Service
 */
public interface TblYqnsSjzgWtzgService extends IService<TblYqnsSjzgWtzg> {
    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @param issues 
     * @return
     * @throws Exception
     */
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo, TblYqnsIssueListEntity issues) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsSjzgWtzg vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param wtzgid
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, BigDecimal wtzgid) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsSjzgWtzg vo) throws Exception;

    /**
     * 附件删除
     * @param token
     * @param wtzgid 
     * @return
     * @throws Exception
     */
    JsonBean deleteAttach(String token, String attid, BigDecimal wtzgid) throws Exception;


	JsonBean deleteYsAttach(String token, String attid, BigDecimal ysnrid) throws Exception;


	JsonBean getGzhfList(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception;


	JsonBean complete(String token, BigDecimal wtzgid) throws Exception;


	JsonBean assignment(String token, TblYqnsSjzgWtzg vo, BigDecimal rectPerson, String rectPersonName) throws Exception;


	JsonBean withdraw(String token, BigDecimal wtzgid) throws Exception;


	JsonBean saveSjyqbcAtt(String token, BigDecimal wtzgid, BigDecimal attId) throws Exception;


	JsonBean removeSjyqbcAtt(String token, BigDecimal wtzgid, BigDecimal attId) throws Exception;


	JsonBean getZgtjhzData(String token, BigDecimal wtzgid) throws Exception;


	JsonBean getAuditFlowType(String token, BigDecimal wtzgid) throws Exception;


	JsonBean saveWtzgSjtzs(String token, BigDecimal wtzgid, BigDecimal formId, Integer optype) throws Exception;


	JsonBean removeWtzgSjtzs(String token, BigDecimal wtzgid, BigDecimal formId, Integer optype) throws Exception;


	JsonBean getWtzgReportInfoList(String token, BigDecimal wtzgid) throws Exception;


	JsonBean hgzgList(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception;


	JsonBean hgzgFillInListList(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception;


	JsonBean getTotalMoney(String token, BigDecimal projectId) throws Exception;


	JsonBean rectificationLedger(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception;


	JsonBean rectificationLedgerFillin(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgWtzg vo,
			TblYqnsIssueListEntity issues) throws Exception;


	JsonBean getHistoryVersion(String token, BigDecimal issuesId, BigDecimal wtzgid) throws Exception;



	/**
	 * 问题整改-审计成果统计
	 *
	 * @return
	 */
	JsonBean selectWtzgAuditResultsStatistics(String token, Integer queryYear) throws Exception;




}
