package com.huabo.audit.service.impl;



import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAccBook;
import com.huabo.audit.oracle.mapper.TblAccBookMapper;
import com.huabo.audit.service.TblAccBookService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblAccBookServiceImpl implements TblAccBookService {

    @Resource
    private TblAccBookMapper tblAccBookMapper;

    @Resource
    private UserProvider userProvider;


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
