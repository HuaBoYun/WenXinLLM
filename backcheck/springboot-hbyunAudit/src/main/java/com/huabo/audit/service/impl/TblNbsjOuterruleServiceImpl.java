package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjOuterruleEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjOuterruleMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.service.TblNbsjOuterruleService;
import com.huabo.audit.util.FiexibleNameAssignment;
import com.huabo.audit.util.FreeMarkerUtil;
import com.huabo.audit.util.R;
import com.huabo.audit.vo.param.fieldOrgStaffId;
import com.huabo.audit.vo.param.fieldOrgStaffName;
@Service
public class TblNbsjOuterruleServiceImpl implements TblNbsjOuterruleService {

	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
	private TblNbsjOuterruleMapper tblNbsjOuterruleMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public void add(TblNbsjOuterruleEntity TblNbsjOuterrule) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addList(List<TblNbsjOuterruleEntity> TblNbsjOuterrule) {
		// TODO Auto-generated method stub

	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modify(TblNbsjOuterruleEntity TblNbsjOuterrule) {
		// TODO Auto-generated method stub

	}

	@Override
	public List search(String name, String org, String effectivelevel, String timeliness, String rulenumber,
			String status, String pubstart, String pubend, String effstart, String effend, String sort,
			String sort_type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjOuterruleEntity> findBydefect(String conid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findBysql(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjOuterruleEntity> findOuterRuleByFlowid(String flowid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjOuterruleEntity> findBycomid(String conid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjOuterruleEntity> findByRulecode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addList(List<TblNbsjOuterruleEntity> TblNbsjOuterrule, BigDecimal orgid) {
		// TODO Auto-generated method stub

	}

	@Override
	public JsonBean findOuterRuleList(String token, Integer pageNumber, Integer pageSize,
			TblNbsjOuterruleEntity TblNbsjOuterrule,String type) throws Exception{
		// TODO Auto-generated method stub
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
    	try {
    		TblNbsjOuterruleEntity NbsjOuterrule = new TblNbsjOuterruleEntity();
        	PageInfo<TblNbsjOuterruleEntity> pageInfo = new PageInfo<TblNbsjOuterruleEntity>();
        	TblOrganization org=tblOrganizationMapper.selectOrgById(loginStaff.getCurrentOrg().getOrgid());
        	TblNbsjOuterrule.setTblOrganization(org);
        	pageInfo.setCondition(TblNbsjOuterrule);
        	pageInfo.setPageSize(pageSize);
        	pageInfo.setCurrentPage(pageNumber);
        	List<TblNbsjOuterruleEntity>  list=this.tblNbsjOuterruleMapper.selectOuterruleListView(pageInfo,loginStaff);
        	FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(list)){
				list.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
        	pageInfo.setTlist(list);
        	pageInfo.setTotalRecord(this.tblNbsjOuterruleMapper.selectOuterruleCountView(pageInfo,loginStaff));
        	resultMap.put("pageInfo", pageInfo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	resultMap.put("type", type);
    	return  ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findById(String token, String outerid)throws Exception{
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjOuterruleEntity entity = this.tblNbsjOuterruleMapper.findById(outerid);
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
		  if(entity!=null){
      	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(entity,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,entity );
		  }
		List<TblAttachment> attList = this.tblAttachmentMapper.findtTblAttachmentByOuter_SJ(entity.getOutrulid());
		resultMap.put("data", entity);
		resultMap.put("attList", attList);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public  Map<String,Object> delete(String token,String id) throws Exception{
		// TODO Auto-generated method stub
		 Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			 resultMap.put("code", "0");
             resultMap.put("msg", "用户已失效！");
		}
		
		this.tblNbsjOuterruleMapper.deleteById(id);
		
		 resultMap.put("code", "1");
         resultMap.put("msg", "操作成功！");
         return resultMap;
	}

	@Override
	public JsonBean mergeOuterruleInfo(String token, TblNbsjOuterruleEntity outer, BigDecimal outerid, String attIds)
			throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		//outer.setEnteringperson(loginStaff.getStaffid().toString());
		//新增及修改前先把附件根据传过来的附件id 删除一遍
		if(attIds != null && !"".equals(attIds)) {
			String[] attId = attIds.split(",");
			for (String aid : attId) {
				this.tblNbsjOuterruleMapper.deleteFileInfoByAttId(new BigDecimal(aid));
			}
		}
		TblOrganization organization = tblOrganizationMapper.selectOrgById(loginStaff.getCurrentOrg().getOrgid());
		outer.setTblOrganization(organization);
		outer.setEnteringtime(new Date());
		try {
			
		if(outerid != null||outer.getOutrulid()!=null) {
			outer.setOutrulid(outerid);
			this.tblNbsjOuterruleMapper.updateById(outer);
		}else {
			//新增
			outer.setCreateorgid(loginStaff.getLinkOrg().getOrgid());
			outer.setCreatestaffid(loginStaff.getStaffid());
			outer.setOutrulid(RandomUtil.uuBigDecimalId());
			this.tblNbsjOuterruleMapper.insert(outer);
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(attIds != null && !"".equals(attIds)) {
			String[] attId = attIds.split(",");
			for (String aid : attId) {
				this.tblNbsjOuterruleMapper.insertAttInfoForPlan(outer.getOutrulid(),aid);
			}
		}
		resultMap.put("data", outer);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public TblNbsjOuterruleEntity findByid(String id) throws Exception{
		TblNbsjOuterruleEntity entity = this.tblNbsjOuterruleMapper.findById(id);
		return entity;
	}

	@Override
	public String SelectOuterruleAtt(String outer) throws Exception {
		// TODO Auto-generated method stub
		return tblNbsjOuterruleMapper.selectOuterAtt(outer) ;
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
        this.tblNbsjOuterruleMapper.deleteFileInfoByAttId(att.getAttid());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}
}
