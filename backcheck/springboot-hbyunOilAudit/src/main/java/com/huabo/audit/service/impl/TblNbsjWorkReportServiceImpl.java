package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjWorkReportEntity;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjWorkReportMapper;
import com.huabo.audit.oracle.vo.TblNbsjWorkReportVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjWorkReportService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
@Service
public class TblNbsjWorkReportServiceImpl implements TblNbsjWorkReportService {

	@Autowired
	private TblNbsjWorkReportMapper tblNbsjWorkReportMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private UserProvider userProvider;
    
	
	@Override
	public JsonBean workReportPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjWorkReportVo tblNbsjWorkReportVo) throws Exception {
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
    	
    	PageInfo<TblNbsjWorkReportEntity> pageInfo = new PageInfo<TblNbsjWorkReportEntity>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	BigDecimal selectProjectIdByStaffId = tblNbsjStaffSelectMapper.selectProjectIdByStaffId(loginStaff.getStaffid());
    	if(selectProjectIdByStaffId==null) {
        	return ResponseFormat.retParam(0,50001,resultMap);//当前人无实施项目
    	}
    	tblNbsjWorkReportVo.setProjectId(selectProjectIdByStaffId);
    	pageInfo.setTlist(this.tblNbsjWorkReportMapper.selectListByPageInfo(pageInfo,tblNbsjWorkReportVo));
    	pageInfo.setTotalRecord(this.tblNbsjWorkReportMapper.selectCountByPageInfo(pageInfo,tblNbsjWorkReportVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean workReportAdd(TblNbsjWorkReportEntity wr, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
//		Integer count = this.tblNbsjWorkReportMapper.selectPlanCodeByOrgid(wr);
//		if(count > 0) {
//			return ResponseFormat.retParam(0,202,null);
//		}
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
//		TblNbsjProject project = this.tblNbsjProjectMapper.selectPJById(projectId);
		
    	
//		wr.setCreatestaffid(loginStaff.getStaffid()+"");
//		wr.setReporttime(new Date());
		wr.setReportstatus("0");
		wr.set_projectid(projectId);	
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(wr.getReportid() != null) {
			//修改；
			this.tblNbsjWorkReportMapper.updateEntity(wr);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationWorkReport(wr.getReportid().intValue());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationWorkReport(id, wr.getReportid().intValue());
				}
			}
		}else {
			//新增；
			this.tblNbsjWorkReportMapper.insertEntity(wr);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationWorkReport(id, wr.getReportid().intValue());
				}
			}
		}
		resultMap.put("WorkReport",wr);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean workReportDelete(Integer reportid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjWorkReportEntity plan = this.tblNbsjWorkReportMapper.selectById(reportid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjWorkReportMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjWorkReportMapper.deleteById(reportid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findNbsjWorkReportDetail(String token, Integer reportid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjWorkReportEntity plan = this.tblNbsjWorkReportMapper.selectById(reportid);
		resultMap.put("wr", plan);
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
        this.tblNbsjWorkReportMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
	
}
