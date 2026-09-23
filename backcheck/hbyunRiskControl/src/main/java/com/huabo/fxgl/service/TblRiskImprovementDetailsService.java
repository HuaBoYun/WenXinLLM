package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;

import java.math.BigDecimal;
import java.util.List;

public interface TblRiskImprovementDetailsService {

    /**
     * 风险事件上报后在 风险监督改进详情中增加汇总数据;
     * @param riskImprovementDetailsEntiry
     */
    void insertTblRiskImprovementDetails(TblRiskImprovementDetailsEntiry riskImprovementDetailsEntiry);
    /**
     * 风险监督改进列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    JsonBean riskImprovementDetailsList(String token, Integer pageNumber, Integer pageSize, BigDecimal riskImplementID,String orgName);

    //JsonBean riskImprovementDetailsList(String token,  BigDecimal riskImplementID);
    
    TblRiskImprovementDetailsEntiry riskImprovementDetails(String token,  BigDecimal id);

    List<TblRiskImprovementDetailsEntiry> exportRiskReport(String ids,BigDecimal id,String orgName);
    

    /**
     * 每个月月初，判断下发的单位上月个是否以上报，如上报...
     * @return
     * @throws Exception
     */
    void isReport();
    
  //  JsonBean updateScore(String token,String id,Integer score);

    JsonBean updateScoreNew(String token,BigDecimal id,String score);

}
