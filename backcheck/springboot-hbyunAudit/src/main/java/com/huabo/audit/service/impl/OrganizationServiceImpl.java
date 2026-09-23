package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.OrganizationService;
import com.huabo.audit.util.PageInfo;

@Service
public class OrganizationServiceImpl implements OrganizationService {
	
	@Autowired
    private TblOrganizationMapper organizationMapper;
	
	@Autowired
    private ActivityPluginsService activityPluginsService;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public void add(TblOrganization org) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modify(TblOrganization org) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TblOrganization> findAllOrg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findAllHYOrg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findByPid(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findByHYPid(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrgTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHyOrgTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization getHY() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization getHYFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TblOrganization> findAllOrg(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findByOrgids(String orgids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findAllOrgBM(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrgTreeqx() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findByIdFatharid(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrgTree(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<BigDecimal> getSubOrgs(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrgTree(String pageurl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrgTreeLefts(BigDecimal orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrgTree(BigDecimal orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> isXj(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteOrg(TblOrganization org) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getOrgTreeLeft(BigDecimal orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> gsisXj(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization isCompanyAddWPZJ(TblOrganization org) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization getSJZYK(TblOrganization org, String iszy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countOrg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countOrgGS(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void orgSorts(List<TblOrganization> orgs, String orgid, String move) {
		// TODO Auto-generated method stub

	}

	@Override
	public TblOrganization findByname(String orgname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findByPlanAllPlanid(String planid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization findByFatharidAndCommpy(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findByIdAlls(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal addReturnId(TblOrganization org) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization belongToCompanyzc(String orgid, String bmid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findBynameGZ(String orgname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization findByCompanyzc(String orgid, String bmid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findIniStatus(BigDecimal orgid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblOrganization getOrgid(String orgid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> findOrgByType(String pid, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblOrganization> selectAllCompanyAndDept() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public JsonBean findAllListPage(String token, Integer pageNumber, Integer pageSize) throws Exception {
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
    	
    	PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.organizationMapper.selectListByPageInfo(pageInfo));
    	pageInfo.setTotalRecord(this.organizationMapper.selectCountByPageInfo(pageInfo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

}
