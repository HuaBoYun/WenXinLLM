package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjLeavemeetingEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjLeavemeetingMapper;
import com.huabo.audit.oracle.vo.TblNbsjLeavemeetingVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjLeavemeetingService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
@Service
public class TblNbsjLeavemeetingServiceImpl extends ServiceImpl<TblNbsjLeavemeetingMapper, TblNbsjLeavemeetingEntity> implements TblNbsjLeavemeetingService {

	@Autowired
	TblNbsjLeavemeetingMapper tblNbsjLeavemeetingMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean outMeetRecordListPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjLeavemeetingVo tblNbsjLeavemeetingVo) throws Exception {
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
    	
    	if(null == tblNbsjLeavemeetingVo.getProgectid()) {
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		Integer projectId = tnp.getProjectId();
    		if(null == projectId) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		tblNbsjLeavemeetingVo.setProgectid(projectId);
    	}
    	
    	
    	PageInfo<TblNbsjLeavemeetingEntity> pageInfo = new PageInfo<TblNbsjLeavemeetingEntity>();
//    	tblNbsjLeavemeeting.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjLeavemeeting);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjLeavemeetingMapper.selectNbsjLeavemeetingListByPageInfo(pageInfo,tblNbsjLeavemeetingVo));
    	pageInfo.setTotalRecord(this.tblNbsjLeavemeetingMapper.selectNbsjLeavemeetingCountByPageInfo(pageInfo,tblNbsjLeavemeetingVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean outMeetRecordAdd(TblNbsjLeavemeetingEntity lev, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjLeavemeetingMapper.selectPlanCodeByOrgid(lev);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
    	
		lev.setCreatrtime(new Date());
		lev.setCreatestaffid(loginStaff.getStaffid()+"");
		lev.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		Integer projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,null);
		}
		lev.setProject(tnp);
		
		if(lev.getLeaveid() != null) {
			//修改；
			this.tblNbsjLeavemeetingMapper.updateEntity(lev);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationLeavemeeting(lev.getLeaveid().intValue());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationLeavemeeting(id, lev.getLeaveid().intValue());
				}
			}
		}else {
			//新增；
			this.tblNbsjLeavemeetingMapper.insertEntity(lev);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationLeavemeeting(id, lev.getLeaveid().intValue());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Leavemeeting",lev);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean outmetDelete(Integer leaveid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjLeavemeetingEntity plan = this.tblNbsjLeavemeetingMapper.selectNbsjLeavemeetingEntityById(leaveid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjLeavemeetingMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjLeavemeetingMapper.deleteLeavemeetingEntityById(leaveid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findNbsjLeavemeetingDetail(String token, Integer leaveid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjLeavemeetingEntity plan = this.tblNbsjLeavemeetingMapper.selectNbsjLeavemeetingEntityById(leaveid);
		resultMap.put("leavemeeting", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean outMetCalcel(Integer leaveid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjLeavemeetingEntity plan = this.tblNbsjLeavemeetingMapper.selectNbsjLeavemeetingEntityById(leaveid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
		this.tblNbsjLeavemeetingMapper.calcelLeavemeetingEntityById(leaveid);
		return ResponseFormat.retParam(1,200,null);
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
        this.tblNbsjLeavemeetingMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}

}
