package com.huabo.audit.service;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.vo.TblNbsjAuditPlanVo;
import com.huabo.audit.util.R;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 描述: 计划编号Service
 * author: lyz
 * date: 2022-04-13
 */
public interface TblNbsjAuditplanService {
    /**
     * @description 审计计划管理列表页面
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param tblNbsjAuditPlanVo
     * @param planEndDate 
     * @param planStartDate 
     * @return
     */
    JsonBean planManagePageList(String token, Integer pageNumber, Integer pageSize,TblNbsjAuditPlanVo tblNbsjAuditPlanVo, String planStartDate, String planEndDate) throws Exception;
    
    /**
    * @description 计划管理详情
    * @author   lyz
    * @date 2022/4/15 9:56
    */
    JsonBean findNbsjAuditPlanDetail(String token, BigDecimal planid) throws Exception;
    
    /**
    * @description 删除计划管理
    * @author   lyz
    * @date 2022/4/19 10:17
    */
    JsonBean deletePlanManageByPlanId(BigDecimal planId, String token) throws Exception;
	
	JsonBean mergePlanManageInfo(TblNbsjAuditplan plan, String token, String planStartTime, String planEndTime, String attIds)throws Exception;
	
	R removeAttInfoByAttId(String token, BigDecimal attId) throws Exception;
	
	JsonBean getNbsjAuditPlanDateInfo(String token, BigDecimal planId) throws Exception;
	JsonBean getNbsjAuditPlanListForMergeNbsjProject(String token) throws Exception;
	JsonBean submitAuditplanArrpoval(String token, BigDecimal planId) throws Exception;
	JsonBean getAuditPlanApprovalInfo(String token, BigDecimal planId, String taskId, BigDecimal cyId,String v) throws Exception;
	JsonBean getAuditPlanAttInfo(String token, BigDecimal planId) throws Exception;
	JsonBean dealAuditPlanApporvalInfo(String token, BigDecimal cyId, String taskId, String transition, String optDesc, BigDecimal planId,String processDefinitionId,String processInstanceId) throws Exception;
	JsonBean getAuditPlanViewInfo(String token, String planName, String planYear, Integer pageNumber, Integer pageSize) throws Exception;
	JsonBean findNbsjAuditPlanViewDetail(String token, BigDecimal planid) throws Exception;

    public InputStream lookCurrentProcessImage(BigDecimal taskId);
    
    
    JsonBean addAutoPlan( String token)throws Exception;
    
    Map<String, Object> viewOppsiteActiviti(BigDecimal planId,String businessKey)throws Exception;
    
    JsonBean getAutoCodeBySjtz(String token)throws Exception;
    
    JsonBean getAutoCodeByYdgl(String token)throws Exception;
    
    JsonBean getAutoCodeByWddg(String token)throws Exception;
    
    JsonBean getAutoCodeBySjjys(String token)throws Exception; 
    
    JsonBean getAutoCodeByQxgl(String token)throws Exception;
    
    JsonBean getAutoCodeByFxfx(String token)throws Exception;
    
    JsonBean getAutoCodeByZgfa(String token)throws Exception;
    
    JsonBean getAutoCodeBySjmb(String token)throws Exception;
    
    JsonBean getAutoCodeBySjzy(String token)throws Exception;
    
    JsonBean getAutoCodeByGlzd(String token)throws Exception;
    
    JsonBean getAutoCodeByXmzl(String token)throws Exception;
    
    JsonBean getAutoCodeByNewSjmb(String token)throws Exception;
    
    JsonBean getAutoCodeByNewSjjyk(String token)throws Exception;

    JsonBean getAutoCodeByXmgl(String token,BigDecimal planId)throws Exception;
    
    JsonBean getAutoCodeByJhgl(String token)throws Exception;

    void resolveSheet(Sheet sheet, String token) throws Exception;

    List<TblNbsjPlanProject> resolveProjectSheet(Sheet sheet, String token , BigDecimal planid, String plancode) throws Exception;


}
