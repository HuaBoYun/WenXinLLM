package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.oracle.entity.TblAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblNbkzRiskEntity;
import com.huabo.audit.oracle.entity.TblNbsjRisktolerability;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbkzRiskMapper;
import com.huabo.audit.oracle.vo.TblNbkzRiskVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbkzRiskService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
@Service
public class TblNbkzRiskServiceImpl implements TblNbkzRiskService{
	
	@Autowired
	private TblNbkzRiskMapper tblNbkzRiskMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean riskPageList(String token, Integer pageNumber, Integer pageSize, TblNbkzRiskVo tblNbkzRiskVo,Integer orgid)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	
//    	if(null == orgid) {
//    		orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
//    	}
    	
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbkzRiskEntity> pageInfo = new PageInfo<TblNbkzRiskEntity>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbkzRiskMapper.selectListByPageInfo(pageInfo,tblNbkzRiskVo,orgid));
    	pageInfo.setTotalRecord(this.tblNbkzRiskMapper.selectCountByPageInfo(pageInfo,tblNbkzRiskVo,orgid));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean riskAdd(TblNbkzRiskEntity risk, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbkzRiskMapper.selectPlanCodeByOrgid(risk);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
    	
//		risk.setCreatestaffid(loginStaff.getStaffid()+"");
		risk.setCreateDate(new Date());
//		risk.set("0");
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(risk.getRiskid() != null) {
			//修改；
			this.tblNbkzRiskMapper.updateEntity(risk);
			//==附件，先删除 再重新添加
			this.tblNbkzRiskMapper.deleteAttmentRelationRISK(risk.getRiskid().intValue());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblNbkzRiskMapper.insertAttmentRelationRISK(id, risk.getRiskid().intValue());
				}
			}
		}else {
			//新增；
			this.tblNbkzRiskMapper.insertEntity(risk);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					this.tblNbkzRiskMapper.insertAttmentRelationRISK(id, risk.getRiskid().intValue());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("risk",risk);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean riskDelete(Integer riskid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbkzRiskEntity plan = this.tblNbkzRiskMapper.selectById(riskid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbkzRiskMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbkzRiskMapper.deleteById(riskid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findRiskDetail(String token, Integer riskid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbkzRiskEntity plan = this.tblNbkzRiskMapper.selectById(riskid);
		
		//
		if(null != plan) {
			String sysorgid = plan.getSysOrgid();
			if(null != sysorgid) {
				String sysOrgName = "";
				List<TblOrganization> listOrg = this.tblNbkzRiskMapper.selectListOrgInId(sysorgid);
				if(null != listOrg) {
					for (int i = 0; i < listOrg.size(); i++) {
						TblOrganization org = listOrg.get(i);
						String orgname = org.getOrgname();
						sysOrgName += orgname;
						if((listOrg.size()-1) > i) {
							sysOrgName += ",";
						}
					}
				}
				plan.setSysOrgName(sysOrgName);
			}
		}
		
		
		resultMap.put("risk", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	/**
	 * 风险发现-附件列表
	 * @param token
	 * @param riskId
	 * @return
	 */
	@Override
	public JsonBean risk_file_list(String token, Integer riskId)throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		List<TblAttachment> attList = this.tblNbkzRiskMapper.risk_file_list(riskId);
		resultMap.put("data", attList);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean fxrrdAdd(String token, TblNbsjRisktolerability tnr) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
//		risk.setCreatestaffid(loginStaff.getStaffid()+"");
//		risk.setCreateDate(new Date());
//		risk.set("0");
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(tnr.getToleid() != null) {
			//修改；
			this.tblNbkzRiskMapper.updateToleEntity(tnr);
		}else {
			//新增；
			this.tblNbkzRiskMapper.insertToleEntity(tnr);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Tole",tnr);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean fxrrdDelete(Integer toleid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
//		TblNbkzRiskEntity plan = this.tblNbkzRiskMapper.selectById(riskid);
//		
//		if(plan == null) {
//			return ResponseFormat.retParam(0,50001,null);
//		}
		
		this.tblNbkzRiskMapper.deletefxrrdById(toleid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean fxrrdLinkList(String token, Integer pageNumber, Integer pageSize, Integer riskid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjRisktolerability> pageInfo = new PageInfo<TblNbsjRisktolerability>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbkzRiskMapper.selectFxrrdListByLink(pageInfo,riskid));
    	pageInfo.setTotalRecord(0);
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public R removeAttInfoByAttId(String token, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return R.fail("用户已失效！");
		}
        return this.deleteRealtionAttInfo(attId);
	}

	private R deleteRealtionAttInfo(String attId) throws Exception {
		boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(attId);
        this.tblNbkzRiskMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}

}
