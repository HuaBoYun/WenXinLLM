package com.huabo.audit.service.impl;


import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
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

import com.huabo.audit.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TblNbsjStaffscoreServiceImpl implements TblNbsjStaffscoreService {

    @Autowired
    TblNbsjStaffscoreMapper tblNbsjStaffscoreMapper;

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
			tblNbsjStaffscoreMapper.updateEntity(re);
			if(details!=null && details.size()>0) {
				for (TblNbsjStaffscoreDetails tblNbsjStaffscoreDetails : details) {
					tblNbsjStaffscoreDetails.setStaffScore_id(re.getStaffScoreid());
					tblNbsjStaffscoreDetailsMapper.updateEntity(tblNbsjStaffscoreDetails);
				}
			}
		}else {
			re.setUnit(loginStaff.getCurrentOrg().getOrgid());
			tblNbsjStaffscoreMapper.insertEntity(re);
			if(details!=null && details.size()>0) {
				for (TblNbsjStaffscoreDetails tblNbsjStaffscoreDetails : details) {
					tblNbsjStaffscoreDetails.setStaffScore_id(re.getStaffScoreid());
					tblNbsjStaffscoreDetailsMapper.insertEntity(tblNbsjStaffscoreDetails);
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
		

		/*Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjStaffscore> pageInfo = new PageInfo<TblNbsjStaffscore>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	List<TblNbsjStaffscore> list = tblNbsjStaffscoreMapper.selectNbsjStaffscoreByPageInfo(pageInfo, loginStaff.getCurrentOrg().getOrgid(), re);
    	pageInfo.setTlist(list);
    	pageInfo.setTotalRecord(tblNbsjStaffscoreMapper.selectNbsjStaffscoreByPageCOunt(loginStaff.getCurrentOrg().getOrgid(), re));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);*/

		//链表分页xml写法
		com.github.pagehelper.PageInfo<TblNbsjStaffscore> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
                    try {
                        this.tblNbsjStaffscoreMapper.findList(loginStaff.getCurrentOrg().getOrgid(), re);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TblNbsjStaffscore> build = new PageResult<TblNbsjStaffscore>().build(pageInfo);
		return ResponseFormat.retParam(1, 200, build);
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
