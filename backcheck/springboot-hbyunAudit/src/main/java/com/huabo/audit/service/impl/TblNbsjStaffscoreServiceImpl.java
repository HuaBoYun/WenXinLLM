package com.huabo.audit.service.impl;


import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjRefopm;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;
import com.huabo.audit.oracle.entity.TblNbsjStaffscore;
import com.huabo.audit.oracle.entity.TblNbsjStaffscoreDetails;
import com.huabo.audit.oracle.mapper.TblNbsjStaffscoreDetailsMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffscoreMapper;
import com.huabo.audit.service.TblNbsjStaffscoreService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TblNbsjStaffscoreServiceImpl implements TblNbsjStaffscoreService {

    @Autowired
    private TblNbsjStaffscoreMapper tblNbsjStaffscoreMapper;

    @Autowired
    TblNbsjStaffscoreDetailsMapper tblNbsjStaffscoreDetailsMapper;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrUpdate(TblNbsjStaffscore re, String token, BigDecimal staffScoreid, List<TblNbsjStaffscoreDetails> details) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(staffScoreid!=null) {
			re.setStaffScoreid(staffScoreid);
			tblNbsjStaffscoreMapper.updateById(re);
			if(details!=null && details.size()>0) {
				for (TblNbsjStaffscoreDetails tblNbsjStaffscoreDetails : details) {
					tblNbsjStaffscoreDetails.setStaffScore_id(re.getStaffScoreid());
					tblNbsjStaffscoreDetailsMapper.updateEntity(tblNbsjStaffscoreDetails);
				}
			}
		}else {
			re.setCreatestaffid(loginStaff.getStaffid());
			re.setUnit(loginStaff.getCurrentOrg().getOrgid());
			re.setStaffScoreid(RandomUtil.uuBigDecimalId());
			tblNbsjStaffscoreMapper.insert(re);
			if(details!=null && details.size()>0) {
				for (TblNbsjStaffscoreDetails tblNbsjStaffscoreDetails : details) {
					tblNbsjStaffscoreDetails.setStaffScore_details_id(RandomUtil.uuBigDecimalId());
					tblNbsjStaffscoreDetails.setStaffScore_id(re.getStaffScoreid());
					tblNbsjStaffscoreDetailsMapper.insert(tblNbsjStaffscoreDetails);
				}
			}
		}
		
		resultMap.put("staffscore",re);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delete(BigDecimal staffScoreid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(staffScoreid!=null) {
			tblNbsjStaffscoreMapper.deleteStaffscoreById(staffScoreid);
			tblNbsjStaffscoreDetailsMapper.deleteInfoAttByScoreid(staffScoreid);
			return ResponseFormat.retParam(1,200,null);
		}
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean findAll(TblNbsjStaffscore re, Integer pageNumber, Integer pageSize, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		

		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjStaffscore> pageInfo = new PageInfo<TblNbsjStaffscore>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	List<TblNbsjStaffscore> list = tblNbsjStaffscoreMapper.selectNbsjStaffscoreByPageInfo(pageInfo, loginStaff.getCurrentOrg().getOrgid(), re,loginStaff);
    	pageInfo.setTlist(list);
    	pageInfo.setTotalRecord(tblNbsjStaffscoreMapper.selectNbsjStaffscoreByPageCOunt(loginStaff.getCurrentOrg().getOrgid(), re,loginStaff));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
		
	}

	@Override
	public JsonBean getAttListByStaffScoreid(String token, BigDecimal staffScoreid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(staffScoreid!=null) {
			TblNbsjStaffscore score = tblNbsjStaffscoreMapper.selectNbsjStaffscoreListByID(staffScoreid);
			if(score!=null) { 
				List<TblNbsjStaffscoreDetails> details = tblNbsjStaffscoreDetailsMapper.findTblNbsjStaffscoreDetails(score.getStaffScoreid());
    			score.setTblNbsjStaffscoreDetails(details);
			}
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("score", score);
	    	return ResponseFormat.retParam(1,200,resultMap);
		}
		
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean getscoreListnopage(TblNbsjStaffscore re, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	PageInfo<TblNbsjStaffscore> pageInfo = new PageInfo<TblNbsjStaffscore>();
    	List<TblNbsjStaffscore> list = tblNbsjStaffscoreMapper.selectNbsjStaffscoreList(loginStaff.getCurrentOrg().getOrgid(), re);
    	resultMap.put("list", list);
    	return ResponseFormat.retParam(1,200,resultMap);
	}


}
