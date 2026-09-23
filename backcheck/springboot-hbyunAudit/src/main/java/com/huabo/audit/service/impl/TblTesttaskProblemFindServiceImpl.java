package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.enums.ProcessEnum;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.mapper.TblTargetTypeMapper;
import com.huabo.audit.oracle.mapper.TblTesttaskProblemFindMapper;
import com.huabo.audit.oracle.vo.TblTesttaskProblemFindVo;
import com.huabo.audit.service.TblTemplateDuService;
import com.huabo.audit.service.TblTesttaskProblemFindService;
import com.huabo.audit.util.PageInfo;


@Service
public class TblTesttaskProblemFindServiceImpl implements TblTesttaskProblemFindService {

	@Autowired
	private TblTesttaskProblemFindMapper tblTesttaskProblemFindMapper;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean findALLProblemLedgerList(Integer pageNumber, Integer pageSize, String userid, String orgid,
			TblTesttaskProblemFind tblTesttaskProblemFind) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonBean findALLProblemLedgerList(Integer pageNumber, Integer pageSize, String token,
			TblTesttaskProblemFindVo vo) throws Exception{
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
    	PageInfo<TblTesttaskProblemFind> pageInfo = new PageInfo<TblTesttaskProblemFind>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	//vo.setCreatestaffid(loginStaff.getStaffid());
    	pageInfo.setTlist(this.tblTesttaskProblemFindMapper.selectListByPageInfo(pageInfo,vo));
    	pageInfo.setTotalRecord(this.tblTesttaskProblemFindMapper.selectCountByPageInfo(pageInfo,vo));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	  @Override
	  public	List<TblTesttaskProblemFind> getExportProblemLedgerList(String staffid, String orgid) throws Exception{
	        String sql=" SELECT TTP.*,TS.REALNAME,TORG.ORGNAME"
	        		+ " from TBL_TESTTASK_PROBLEMFIND TTP"
	        		+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID "
	        		+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG "
	                + " where 1=1 and TTP.status in (6)";

	        sql+=" ORDER BY TTP.FINDID desc";
	        return this.tblTesttaskProblemFindMapper.getListBySql(sql);
	    }
   
}
