package com.huabo.fxgl.service;

import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;
import com.huabo.fxgl.entity.TblRiskImprovementEntiry;

public interface TblRiskImprovementService {
    /**
     * 下发后在总公司列表（风险监督改进）显示下发详情
     * @param improvementEntiry
     */
    void insertImprovement(TblRiskImprovementEntiry improvementEntiry);
    /**
     * 风险监督改进列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    JsonBean riskImprovementList(String token, Integer pageNumber, Integer pageSize,String name,String orgname);
    
    List<TblRiskImprovementDetailsEntiry>  reportList(String token,String name,String orgname);

}
