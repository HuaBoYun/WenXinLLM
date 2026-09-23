package com.huabo.audit.service.impl;

import java.math.BigDecimal;
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
import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjDoubtfulpointMapper;
import com.huabo.audit.oracle.vo.TblNbsjDoubtfulpointVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjDoubtfulpointService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;

@Service
public class TblNbsjDoubtfulpointServiceImpl extends ServiceImpl<TblNbsjDoubtfulpointMapper, TblNbsjDoubtfulpointEntity> implements TblNbsjDoubtfulpointService {

	@Autowired
    private TblNbsjDoubtfulpointMapper tblNbsjDoubtfulpointMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean dpPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjDoubtfulpointVo tblNbsjDoubtfulpointVo) throws Exception {
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
    	
//    	//==查询当前实施的项目！
//		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
//		if(tnp == null) {
//			return ResponseFormat.retParam(30003,resultMap);
//		}
//		Integer projectId = tnp.getProjectId();
//		if(null == projectId) {
//			return ResponseFormat.retParam(30003,resultMap);
//		}
    	BigDecimal orgid = loginStaff.getCurrentOrg().getOrgid();
    	tblNbsjDoubtfulpointVo.setOrgid(orgid);
    	TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp!=null) {
			tblNbsjDoubtfulpointVo.setProjectid(tnp.getProjectId());
		}
		
    	
    	PageInfo<TblNbsjDoubtfulpointEntity> pageInfo = new PageInfo<TblNbsjDoubtfulpointEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjDoubtfulpointMapper.selectListByPageInfo(pageInfo,tblNbsjDoubtfulpointVo));
    	pageInfo.setTotalRecord(this.tblNbsjDoubtfulpointMapper.selectCountByPageInfo(pageInfo,tblNbsjDoubtfulpointVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dpAdd(TblNbsjDoubtfulpointEntity dp, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjDoubtfulpointMapper.selectPlanCodeByOrgid(dp);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
//		dp.setAuditStaffId(loginStaff.getStaffid().intValue());
		dp.setEdittime(new Date());
		dp.setDpstatus("0");
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp!=null) {
			dp.setProjectid(tnp.getProjectId());
		}
		
		
		Integer orgid = loginStaff.getCurrentOrg().getOrgid().intValue();
		dp.setOrgid(orgid);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(dp.getDpointid() != null) {
			//修改；
			this.tblNbsjDoubtfulpointMapper.updateEntity(dp);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationDoubtfulpoint(dp.getDpointid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationDoubtfulpoint(id, dp.getDpointid());
				}
			}
		}else {
			//新增；
			this.tblNbsjDoubtfulpointMapper.insertEntity(dp);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationDoubtfulpoint(id, dp.getDpointid());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("Doubtfulpoint",dp);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean dpDelete(Integer dpointid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjDoubtfulpointEntity plan = this.tblNbsjDoubtfulpointMapper.selectById(dpointid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjDoubtfulpointMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjDoubtfulpointMapper.deleteById(dpointid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findDPDetail(String token, Integer dpointid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjDoubtfulpointEntity plan = this.tblNbsjDoubtfulpointMapper.selectById(dpointid);
		resultMap.put("Doubtfulpoint", plan);
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
        this.tblNbsjDoubtfulpointMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}

}
