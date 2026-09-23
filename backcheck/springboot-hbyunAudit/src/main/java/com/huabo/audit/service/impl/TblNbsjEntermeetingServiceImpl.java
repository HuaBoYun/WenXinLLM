package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjEntermeetingEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjEntermeetingMapper;
import com.huabo.audit.oracle.vo.ExecPhraseVo;
import com.huabo.audit.oracle.vo.TblNbsjEntermeetingVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjEntermeetingService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;

@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjEntermeetingServiceImpl implements TblNbsjEntermeetingService {
    @Autowired
    TblNbsjEntermeetingMapper tblNbsjEntermeetingMapper;
    
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public PageInfo<TblNbsjEntermeetingEntity> approachList(ExecPhraseVo vo) {
		/*
		 * PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		 * List<TblNbsjEntermeetingEntity> list =
		 * tblNbsjEntermeetingMapper.approachList(vo);
		 */
        //return new PageInfo<>(list);
    	return null;
    }

//	@Override
//	public List<TblNbsjEntermeetingEntity> isNoteCode(String code, String string) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
    @Override
	public TblNbsjEntermeetingEntity findEntityByEnterId(String enterid) {
		return this.tblNbsjEntermeetingMapper.getById(enterid);
	}
    
	@Override
	public JsonBean inMeetRecordListPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjEntermeetingVo tblNbsjEntermeetingVo) throws Exception {
    	
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
    	
    	
    	if(null == tblNbsjEntermeetingVo.getProgectid()) {
    		//==查询当前实施的项目！
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
			BigDecimal projectId = tnp.getProjectId();
    		if(null == projectId) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		tblNbsjEntermeetingVo.setProgectid(projectId);
    	}
    	
    	PageInfo<TblNbsjEntermeetingEntity> pageInfo = new PageInfo<TblNbsjEntermeetingEntity>();
//    	TblNbsjEntermeetingEntity.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjEntermeeting);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjEntermeetingMapper.selectNbsjEntermeetingListByPageInfo(pageInfo,tblNbsjEntermeetingVo));
    	pageInfo.setTotalRecord(this.tblNbsjEntermeetingMapper.selectNbsjEntermeetingCountByPageInfo(pageInfo,tblNbsjEntermeetingVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean inMeetRecordAdd(TblNbsjEntermeetingEntity met, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjEntermeetingMapper.selectPlanCodeByOrgid(met);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
		
		//==
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,10001,null);
		}
		BigDecimal projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30001,null);
		}
		met.setProject(tnp);
		
		met.setCreatestaffid(loginStaff.getStaffid()+"");
		met.setCreatrtime(new Date());
		met.setStatus("0");
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(met.getEnterid() != null) {
			//修改；
			this.tblNbsjEntermeetingMapper.updateEntity(met);
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationEntermeeting(met.getEnterid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationEntermeeting(id, met.getEnterid());
				}
			}
		}else {
			//新增；
			this.tblNbsjEntermeetingMapper.insertEntity(met);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					this.tblAttachmentMapper.insertAttmentRelationEntermeeting(id, met.getEnterid());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("entermeeting",met);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean metDelete(BigDecimal enterid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjEntermeetingEntity plan = this.tblNbsjEntermeetingMapper.selectNbsjEntermeetingEntityById(enterid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
//		if (plan.getOpinionstatus().equals(TblNbsjAuditplan.SPNO)) {
//			this.tblNbsjEntermeetingMapper.deleteAuditPlanEntityById(planId);
//			return ResponseFormat.retParam(1,200,null);
//        } else {
//            return ResponseFormat.retParam(0,50001,null);
//        }
		this.tblNbsjEntermeetingMapper.deleteEntermeetingEntityById(enterid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findNbsjEntermeetingDetail(String token, BigDecimal enterid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjEntermeetingEntity plan = this.tblNbsjEntermeetingMapper.selectNbsjEntermeetingEntityById(enterid);
		resultMap.put("entermeeting", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean metCalcel(BigDecimal enterid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjEntermeetingEntity plan = this.tblNbsjEntermeetingMapper.selectNbsjEntermeetingEntityById(enterid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
		this.tblNbsjEntermeetingMapper.calcelEntermeetingEntityById(enterid);
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
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(new BigDecimal(attId));
        this.tblNbsjEntermeetingMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
}
