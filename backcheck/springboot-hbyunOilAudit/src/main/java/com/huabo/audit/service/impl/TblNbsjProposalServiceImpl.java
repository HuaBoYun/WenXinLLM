package com.huabo.audit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProposalMapper;
import com.huabo.audit.oracle.vo.TblNbsjProposalVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjProposalService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
@Service
public class TblNbsjProposalServiceImpl implements TblNbsjProposalService {

	@Autowired
	private TblNbsjProposalMapper tblNbsjProposalMapper;
    
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    
    @Resource
    private UserProvider userProvider;
	
	@Override
	public void delete(TblNbsjProposalEntity leave) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(TblNbsjProposalEntity leave) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(TblNbsjProposalEntity leave) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TblNbsjProposalEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjProposalEntity> isNoteCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjProposalEntity get(String noteid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjProposalEntity> isNoteCode(String code, String projectId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	//==
	@Override
	public JsonBean suggestPageList(String token, Integer pageNumber, Integer pageSize,
			TblNbsjProposalVo tblNbsjProposalVo,Integer projectId) throws Exception {
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
    	
    	if(null == projectId) {
    		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
    		if(tnp == null) {
    			return ResponseFormat.retParam(0,30003,resultMap);
    		}
    		projectId = tnp.getProjectId();
    	}
    	
    	
    	PageInfo<TblNbsjProposalEntity> pageInfo = new PageInfo<TblNbsjProposalEntity>();
//    	tblNbsjWorkReport.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjWorkReport);


		Integer finalProjectId = projectId;
		com.github.pagehelper.PageInfo<TblNbsjProposalEntity> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjProposalMapper.selectListByPageInfo(pageInfo,tblNbsjProposalVo, finalProjectId);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean suggestAdd(TblNbsjProposalEntity proposal, String token,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer count = this.tblNbsjProposalMapper.selectPlanCodeByOrgid(proposal);
		if(count > 0) {
			return ResponseFormat.retParam(0,202,null);
		}
    	
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		Integer projectId = tnp.getProjectId();
		proposal.setProjectId(projectId);
		
		
		proposal.setStaffid(loginStaff.getStaffid().intValue());
		proposal.setCreateTime(new Date());
		proposal.setStatus(0);
		//根据planId主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(proposal.getProid() != null) {
			//修改；
			this.tblNbsjProposalMapper.updateEntity(proposal);
			
			//==附件，先删除 再重新添加
			this.tblAttachmentMapper.deleteAttmentRelationAuditSuggest(proposal.getProid());
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblAttachmentMapper.insertAttmentRelationAuditSuggest(id, proposal.getProid());
				}
			}
		}else {
			//新增；
			this.tblNbsjProposalMapper.insertEntity(proposal);
			//==附件
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					this.tblAttachmentMapper.insertAttmentRelationAuditSuggest(id, proposal.getProid());
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("proposal",proposal);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean suggestCancel(Integer proid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjProposalEntity plan = this.tblNbsjProposalMapper.selectById(proid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
		this.tblNbsjProposalMapper.calcelById(proid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findSuggestDetail(String token, Integer proid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjProposalEntity plan = this.tblNbsjProposalMapper.selectById(proid);
		resultMap.put("proposal", plan);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean suggestDelete(Integer proid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjProposalEntity plan = this.tblNbsjProposalMapper.selectById(proid);
		
		if(plan == null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		
		this.tblNbsjProposalMapper.deleteById(proid);
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
        this.tblNbsjProposalMapper.deleteFileInfoByAttId(att.getAttid().intValue());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
	

}
