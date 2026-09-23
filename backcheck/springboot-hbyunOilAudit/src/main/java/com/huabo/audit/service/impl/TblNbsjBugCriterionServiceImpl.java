package com.huabo.audit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;
import com.huabo.audit.util.PageResult;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import com.huabo.audit.oracle.entity.TblNbsjBugEntity;
import com.huabo.audit.oracle.mapper.TblNbsjBugCriterionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjBugMapper;
import com.huabo.audit.service.TblNbsjBugCriterionService;
import com.huabo.audit.util.PageInfo;
@Service
public class TblNbsjBugCriterionServiceImpl implements TblNbsjBugCriterionService {

	@Resource
	private TblNbsjBugCriterionMapper tblNbsjBugCriterionMapper;
	
	@Resource
	private TblNbsjBugMapper tblNbsjBugMapper;
	
	@Resource
    private UserProvider userProvider;
	
	
//	@Override
//	public JsonBean getNbsjBugCriterionListForMerge(String token) throws Exception {
//		TblStaffUtil loginStaff = userProvider.get();
//		if(loginStaff == null) {
//			return ResponseFormat.retParam(0,20006,null);
//		}
//		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		List<TblNbsjBugCriterion> typeList = this.tblNbsjBugCriterionMapper.selectNbsjBugCriterionListForMerge(loginStaff.getCurrentOrg().getOrgid());
//		resultMap.put("typeList", typeList);
//		return ResponseFormat.retParam(1,200,resultMap);
//	}

	@Override
	public JsonBean saveNbsjBugCriterion(TblNbsjBugCriterion con, String token) throws Exception {
		try {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if (con != null && con.getBugcriid() == null) {
			con.setVersion(1);
			if(loginStaff.getCurrentOrg()!=null&&loginStaff.getCurrentOrg().getOrgid()!=null){
				con.setOrgid(Integer.valueOf(loginStaff.getCurrentOrg().getOrgid().toString()));
			}
			tblNbsjBugCriterionMapper.insertEntity(con);
			}else {
				TblNbsjBugCriterion nbsjBug=tblNbsjBugCriterionMapper.selectTblNbsjCriterion(con.getBugcriid().toString());
				con.setVersion(nbsjBug.getVersion()+1);
				if(loginStaff.getCurrentOrg()!=null&&loginStaff.getCurrentOrg().getOrgid()!=null){
					con.setOrgid(Integer.valueOf(loginStaff.getCurrentOrg().getOrgid().toString()));
				}
				tblNbsjBugCriterionMapper.updateEntity(con);
 		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tblNbsjBugCriterion",con);
		return ResponseFormat.retParam(1,200,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public  JsonBean  SelectNbsjBugCriterion(String bugid, String token) throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjBugCriterion nbsjBug=tblNbsjBugCriterionMapper.selectTblNbsjCriterion(bugid);
         resultMap.put("date", nbsjBug);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delNbsjBugCriterion(String bugcriid, String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
   			return ResponseFormat.retParam(1,20006,null);
		}
		
		TblNbsjBugCriterion nbsjBug=tblNbsjBugCriterionMapper.selectTblNbsjCriterion(bugcriid);
		List<TblNbsjBugEntity> findByCriterionId = tblNbsjBugMapper.findByCriterionId(nbsjBug.getBugcriid());
		if(null!=findByCriterionId&&findByCriterionId.size()>0) {
    			return ResponseFormat.retParam(1,"缺陷标准使用中,不能删除！",null);
		}
		
		 tblNbsjBugCriterionMapper.delTblBugCriterion(bugcriid);
 		return ResponseFormat.retParam(1,70003,null);
	}

	@Override
	public JsonBean selectNbsjBugCriterionByPageInfo(String token, Integer pageNumber, Integer pageSize) throws Exception {
    	
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
    	Map<String,Object> resultMap = new HashMap<>(0);
    	PageInfo<TblNbsjBugCriterion> pageInfo = new PageInfo<TblNbsjBugCriterion>();
		com.github.pagehelper.PageInfo<TblNbsjBugCriterion> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjBugCriterionMapper.selectNbsjBugCriterionListByPageInfo(null,Integer.parseInt(loginStaff.getCurrentOrg().getOrgid().toString()));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord((int) pageInfo2.getTotal());
		pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	  

}
