package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblUserOrgRelation;
import com.huabo.system.mapper.TblLoginTypeMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblUserOrgRelationMapper;
import com.huabo.system.service.TblUserOrgRelationService;

import lombok.extern.slf4j.Slf4j;

@Service("TblUserOrgRelationService")
@Slf4j
public class TblUserOrgRelationServiceImpl implements TblUserOrgRelationService {

    @Resource
    private TblUserOrgRelationMapper tblUserOrgRelationMapper;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;
    
	@Override
	public List<TblUserOrgRelation> dealInsertRealtionInfo(TblStaffUtil loginStaff, String[] deptIds) throws Exception {
		
		List<TblUserOrgRelation> relaList = new ArrayList<TblUserOrgRelation>(0);
		
		TblUserOrgRelation rela = null;
		BigDecimal dept = null;
		int i = 0;
		for (String deptId : deptIds) {
			dept = new BigDecimal(deptId);
			rela = new TblUserOrgRelation();
			rela.setCreateStaff(loginStaff.getStaffid());
			rela.setDeptId(dept);
			rela.setDetpName(this.tblOrganizationMapper.findByorgid(dept));
			rela.setNumno(i++);
			this.setOrgInfo(rela,dept);
			rela.setOrgstrs(rela.getOrgstrs().replace(",null", ""));
			rela.setOrgYmStrIds(rela.getOrgYmStrIds().replace(",null", ""));
			rela.setLongName(rela.getLongName().replace("/null", ""));
			relaList.add(rela);
		}
		return relaList;
	}

	@Override
	public List<TblUserOrgRelation> initDateDealRelation(TblStaff tblStaff, List<TblUserOrgRelation> relaList) throws Exception {
		List<TblUserOrgRelation> insertList = new ArrayList<TblUserOrgRelation>(0);
		TblUserOrgRelation insert = null;
		this.tblUserOrgRelationMapper.deleteRelationByStaffId(tblStaff.getStaffid());
		
		for (TblUserOrgRelation rela : relaList) {
			insert = new TblUserOrgRelation();
			insert.setCreateStaff(tblStaff.getStaffid());
			insert.setStaffid(tblStaff.getStaffid());
			insert.setDeptId(rela.getDeptId());
			insert.setNumno(rela.getNumno());
			this.setOrgInfo(insert,insert.getDeptId());
			insert.setOrgstrs(insert.getOrgstrs().replace(",null", ""));
			insert.setOrgYmStrIds(insert.getOrgYmStrIds().replace(",null", ""));
			insert.setCreateTime(new Date());
			this.tblUserOrgRelationMapper.insert(insert);
			insertList.add(insert);
		}
		return insertList;
	}
	
	private void setOrgInfo(TblUserOrgRelation rela, BigDecimal deptId) {
		TblOrganization org = this.tblOrganizationMapper.selectByOrgId(deptId);
		
		rela.setOrgstrs(org.getOrgid()+","+rela.getOrgstrs());
		rela.setLongName(org.getOrgname()+"/"+rela.getLongName());
		rela.setOrgYmStrIds(org.getPkYmOrgId()+","+rela.getOrgYmStrIds());
		if(org.getOrgtype() != 0 && rela.getOrgId() == null) {
			rela.setOrgId(org.getOrgid());
		}
		if(org.getFatherorgid().compareTo(BigDecimal.valueOf(-1)) > 0) {
			this.setOrgInfo(rela, org.getFatherorgid());
		}
	}

	@Override
	public void removeRelationByStaffId(BigDecimal staffId) throws Exception {
		this.tblUserOrgRelationMapper.deleteRelationByStaffId(staffId);
	}


	@Override
	public void InsertRealtionByStaffId(BigDecimal staffId, List<TblUserOrgRelation> relaList) throws Exception {
		for (TblUserOrgRelation rela : relaList) {
			rela.setStaffid(staffId);
			rela.setCreateTime(new Date());
			this.tblUserOrgRelationMapper.insert(rela);
		}
	}

	@Override
	public JsonBean getLoginUserOrgRelationList(String token,String orgname) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (null == user) {
			return ResponseFormat.retParam(0, 10002, null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("userInfo", user);
		
		//以部门为单元进行切换 并保留用户切换部门信息
		List<TblUserOrgRelation> relaList = this.tblUserOrgRelationMapper.selectUserOrgRelationInfoListByStaffId(user.getStaffid(),orgname);
		
		//以公司为单元 进行切换   不保留切换公司的信息
		//List<TblOrganization> relaList = this.tblOrganizationMapper.selectOrgListByDataRealtion(user.getRoleIdStrs());
		resultMap.put("relaList", relaList);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public List<TblUserOrgRelation> selectUserOrgRelationInfoListByStaffId(BigDecimal staffid) throws Exception {
		return this.tblUserOrgRelationMapper.selectUserOrgRelationInfoListByStaffId(staffid,null);
	}

	@Override
	public List<TblUserOrgRelation> dealAddRealtionInfo(TblStaffUtil loginStaff, String[] deptIds) throws Exception {
		List<TblUserOrgRelation> relaList = this.tblUserOrgRelationMapper.selectUserOrgRelationInfoListByStaffId(loginStaff.getStaffid(),null);
		TblUserOrgRelation rela = null;
		BigDecimal dept = null;
		Integer i = relaList.size();
		for (String deptId : deptIds) {
			dept = new BigDecimal(deptId);
			rela = new TblUserOrgRelation();
			rela.setCreateStaff(loginStaff.getStaffid());
			rela.setDeptId(dept);
			rela.setDetpName(this.tblOrganizationMapper.findByorgid(dept));
			rela.setNumno(i++);
			this.setOrgInfo(rela,dept);
			rela.setOrgstrs(rela.getOrgstrs().replace(",null", ""));
			rela.setOrgYmStrIds(rela.getOrgYmStrIds().replace(",null", ""));
			rela.setLongName(rela.getLongName().replace("/null", ""));
			relaList.add(rela);
		}
		return relaList;
	}

}
