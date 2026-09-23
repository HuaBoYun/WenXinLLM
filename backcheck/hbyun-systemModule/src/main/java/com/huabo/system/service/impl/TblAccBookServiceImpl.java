package com.huabo.system.service.impl;


import java.math.BigDecimal;
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
import com.huabo.system.entity.TblAccBook;
import com.huabo.system.mapper.TblAccBookMapper;
import com.huabo.system.service.TblAccBookService;

@Service
public class TblAccBookServiceImpl implements TblAccBookService {

    @Resource
    private TblAccBookMapper tblAccBookMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public List<TblAccBook> findBookIdByUserAll(BigDecimal staffid, BigDecimal orgid) {
        //return this.tblAccBookMapper.listBySql(staffid,orgid);
        return this.tblAccBookMapper.listBySql(staffid, orgid);
    }

    @Override
    public TblAccBook findByBookIdOne(String connectionstrings) {
        return tblAccBookMapper.findByBookIdOne(connectionstrings);
    }

	@Override
	public JsonBean findstaffid(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",tblAccBookMapper.listBySqlstaffid(loginStaff.getStaffid()));
		return ResponseFormat.retParam(1,200,resultMap);
	}

}
