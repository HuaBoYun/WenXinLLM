package com.huabo.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.ImplementPlanTeamEntity;
import com.huabo.audit.vo.result.QualityParam;

import java.math.BigDecimal;

/**
 * @author Rui
 * @ClassName ImplementPlanService
 * @Description
 * @DATE 2023/10/28
 */
public interface ImplementPlanService {
    JsonBean findAll(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime, BigDecimal staffId, Integer xmnd,String spzt,String xctyp) throws Exception;

    JsonBean findById(String id) throws Exception;

    JsonBean updateEntity(ImplementPlanEntity implementPlanEntity) throws Exception;

    JsonBean saveEntity(String token, ImplementPlanEntity implementPlanEntity) throws Exception;

    void deleteByIds(String ids) throws Exception;
    
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception;
    
    public JsonBean projectSS(String token, String projectid) throws Exception;
    
    public JsonBean findProjectDetail(String token, BigDecimal projectid) throws Exception;
    
    public JsonBean findRwfpAll(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime) throws Exception ;
    
    public JsonBean pjStart(String token, BigDecimal projectid) throws Exception ;
    
    public JsonBean jsfpRoleManageSave(String token, String ids, BigDecimal teamId, BigDecimal projectId) throws Exception;
    
    public JsonBean findByIdTeams(String token,BigDecimal id) throws Exception;
    
    public JsonBean findsqAll(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime) throws Exception;
    
    public JsonBean fpzyksry(String token, BigDecimal projectid,String zyksryids,String zyksryrwnames) throws Exception;

	JsonBean getReviewStatusList(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId,
			String projectName, String planStarttime, String planEndtime) throws Exception;
	
	/**
	 * 修改项目小组
	 * @param token
	 * @param id
	 * @param team
	 * @return
	 * @throws Exception
	 */
	JsonBean updateImplementPlanTeamWidthId(String token, ImplementPlanTeamEntity team) throws Exception;

    JsonBean getListcCompleted(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime, BigDecimal staffId, Integer xmnd) throws Exception;
    
    JsonBean yzmb(String token, String tempid) throws Exception;
    
    JsonBean findBytjsj(String token,QualityParam param) throws Exception;
    
    public JsonBean findbyOrgidLast(String token,String orgid) throws Exception;

	JsonBean getImplPlanListForSjbgdg(Integer pageNumber, Integer pageSize, String projectName) throws Exception;
    
}
