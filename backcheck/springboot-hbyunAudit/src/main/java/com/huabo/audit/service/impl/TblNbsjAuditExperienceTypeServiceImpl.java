package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceTypeEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditExperienceTypeMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditStepMapper;
import com.huabo.audit.service.TblNbsjAuditExperienceTypeService;
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjAuditExperienceTypeServiceImpl implements TblNbsjAuditExperienceTypeService {
	
	@Resource
	private TblNbsjAuditExperienceTypeMapper typemapper;
	@Autowired
	private TblNbsjAuditStepMapper stepmapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean getRoot(String token, BigDecimal nodeId,String mpdeltype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		List<TblNbsjAuditExperienceTypeEntity> list = typemapper.getBynoid(nodeId, mpdeltype);
		List<Tree> tree = getNodes(list);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",tree);
		return ResponseFormat.retParam(1,200,resultMap);		
		
//		if(nodeId!=null) {
//			List<TblNbsjAuditExperienceTypeEntity> list = typemapper.getBynoid(nodeId, mpdeltype)
//			List<Tree> tree = getNodes(list);
//			Map<String,Object> resultMap = new HashMap<String,Object>(0);
//			resultMap.put("data",tree);
//			return ResponseFormat.retParam(1,200,resultMap);
//		}else {
//			List<TblNbsjAuditExperienceTypeEntity> list = typemapper.getByall();
//			if(list==null || list.size()==0) {
//				TblNbsjAuditExperienceTypeEntity auditExperienceType = new TblNbsjAuditExperienceTypeEntity();
//				auditExperienceType.setCreateTime(new Date());
//				auditExperienceType.setTypeName("审计经验分类目录");
//				auditExperienceType.setCreateStaffid(loginStaff.getStaffid());
//				typemapper.insertEntity(auditExperienceType);
//				list = typemapper.getByall();
//			}
//			List<Tree> tree = getNodes(list);
//			Map<String,Object> resultMap = new HashMap<String,Object>(0);
//			resultMap.put("data",tree);
//			return ResponseFormat.retParam(1,200,resultMap);
//		}
		
	}
	
	
	private List<Tree> getNodes(List<TblNbsjAuditExperienceTypeEntity> root) {
		List<Tree> list = new ArrayList<Tree>();
		for (TblNbsjAuditExperienceTypeEntity tblNbsjAuditExperienceType : root) {
			Tree tree = new Tree();
			BigDecimal typeId = tblNbsjAuditExperienceType.getTypeId();
			tree.setId(typeId);
			tree.setName(tblNbsjAuditExperienceType.getTypeName());
			int count=0;
			try {
				count = this.typemapper.getBynoidcount(typeId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(count>0){
				tree.setIsParent(true);
			}else{
				tree.setIsParent(false);
			}
			tree.setpId(tblNbsjAuditExperienceType.getParentId());
			tree.setTarget("mainFramex");
			tree.setType("0");
			//tree.setUrl("/nbsj/hyzsk_list?typeId="+typeId);
			list.add(tree);
		}
		return list;
	}


	@Override
	public JsonBean saveOrupdate(TblNbsjAuditExperienceTypeEntity type, String token)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(type.getTypeId()!=null) {
			type.setUpdateTime(new Date());
			typemapper.updateEntity(type);
		}else {
			type.setCreateTime(new Date());
			type.setCreateStaffid(loginStaff.getStaffid());
			typemapper.insertEntity(type);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",type);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean delete(String token, BigDecimal nodeId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		List<TblNbsjAuditStepEntity> list = stepmapper.findByExperByExperId(nodeId);
		if(list!=null && list.size()>0) {
			return ResponseFormat.retParam(0,"已建立模型，不能删除",null);
		}
		typemapper.deleteEntity(nodeId);
		return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean findbyid(String token, BigDecimal nodeId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjAuditExperienceTypeEntity entity = typemapper.findbyid(nodeId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",entity);
		return ResponseFormat.retParam(1,200,resultMap);
	}


}
 