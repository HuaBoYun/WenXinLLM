package com.huabo.fxgl.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Riskevent;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IRiskeventService extends IService<Riskevent> {
    public List<Riskevent> findRiskeventByRiskId(String riskid);

    JsonBean riskQueryLeft(String orgid, String hbOrgEntity, String treeName) throws Exception;

    JsonBean disposalManage(String riskcatid, String orgid, Find find, Integer pageNumber, Integer pageSize, String hbOrgEntity, String ty, String choiceSearch) throws Exception;

    JsonBean riskEventDetail(String eventid);
    
	Map<String, Object>  riskevent_add(Riskevent risk, String attid) throws Exception;
	
	JsonBean get_risksj_no(String token) throws Exception;
	
	
	
	//中核的调整代码
    JsonBean disposalManageZh(String riskcatid, String orgid, Find find, Integer pageNumber, Integer pageSize, String hbOrgEntity, String ty, String choiceSearch) throws Exception;
    JsonBean disposalManageZhMain(String riskcatid, String orgid, Find find, Integer pageNumber, Integer pageSize, String hbOrgEntity, String ty, String choiceSearch ,String losseventcategory) throws Exception;

    
    List<Riskevent> disposalManageZhExport(String ids,String riskcatid, String orgid, Find find, String hbOrgEntity, String ty, String choiceSearch) throws Exception;

    
    //获取版本号
    JsonBean getMaxVersion(String token,BigDecimal id) throws Exception;
	
    JsonBean getViewHistoricalVersions( String token,BigDecimal id) throws Exception;

    JsonBean getViewHistoricalVersionsMain( String token,BigDecimal id) throws Exception;
    
    JsonBean reportToLeader( String token,BigDecimal id) throws Exception;

    JsonBean getRemindList(String token) throws Exception;

    Map<String,Object>  getRiskeventCountByCompany(String token,String company) throws Exception;
    
    Map<String,Object>  getRiskLosseventcategory(String token,String company) throws Exception;

    Map<String,Object>  getCompanyRiskEventList(String token,String year) throws Exception;
    
}
