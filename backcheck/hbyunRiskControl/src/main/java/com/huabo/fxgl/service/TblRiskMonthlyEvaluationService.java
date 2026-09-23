package com.huabo.fxgl.service;


import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.TblRiskMonthlyEvaluationEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TblRiskMonthlyEvaluationService {

    /**
     * 月度评估-新增月度评估
     * 根据risk id 查询相关月度评估
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    JsonBean queryMonthlyEvaluationList(String token, Integer pageNumber, Integer pageSize, BigDecimal riskid) throws Exception;

    /**
     * 单位月度风险评估表新增
     * @param attIds
     * @param tblRiskMonthlyEvaluationEntity
     * @param token
     * @return
     * @throws Exception
     */
    JsonBean insertOrUpdateReporting(TblRiskMonthlyEvaluationEntity tblRiskMonthlyEvaluationEntity, String token, String attIds) throws Exception;
    /**
     * 单位月度风险评估表详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    JsonBean monthlyEvaluationDetails(String id, String token) throws Exception;
    /**
     * 单位月度风险评估表详情的附件
     * @param token
     * @param id
     * @return
     */
    JsonBean getMonthlyEvaluationAttInfo(String token, String id) throws Exception;
    /**
     * 单位月度风险评估表详情删除
     * @param id
     * @return
     * @throws Exception
     */
    void monthlyEvaluationADelete(String id);

    /**
     * 分公司上报到总公司
     * @param token
     * @return
     * @throws Exception
     */
    JsonBean monthlyEvaluationSubmit(String token,String id);

    /**
     * 风险评估汇总列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    JsonBean queryRiskAssessmentSummaryList(String token, Integer pageNumber, Integer pageSize,TblRiskMonthlyEvaluationEntity entity);
    
    List<TblRiskMonthlyEvaluationEntity> exportMonthlyEvaluationSummary(String id,TblRiskMonthlyEvaluationEntity entity,String token)throws Exception;
    List<TblRiskMonthlyEvaluationEntity> exportMonthlyEvaluation(String token,String id,String createUnitidName,String riskid)throws Exception;

    /**
     * 月度评估节点--外层显示已经评估的风险点
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    JsonBean queryAssessedRiskList(String token,Risk risk,String id,Integer pageNumber,Integer pageSize,Integer authorityType)throws Exception;

    /**
     * 月度评估节点--外层 导出已经评估的风险点
     * @return
     * @throws Exception
     */
    List<Risk> exportRiskEvaluated(String token,Risk risk,String id,Integer authorityType) throws Exception;
    

    void isReportVersionT();
    
    JsonBean remindMonthlyEvaluation(String token)throws Exception;
    
    JsonBean test(String toke,int number);
    
    
}
