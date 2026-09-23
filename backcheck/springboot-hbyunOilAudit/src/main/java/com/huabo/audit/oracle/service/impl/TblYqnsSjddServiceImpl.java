package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsSjdd;
import com.huabo.audit.oracle.mapper.TblYqnsSjddMapper;
import com.huabo.audit.oracle.service.TblYqnsSjddService;


@Service
public class TblYqnsSjddServiceImpl implements TblYqnsSjddService {
	
	
	@Resource
	private TblYqnsSjddMapper tblYqnsSjddMapper;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, List<TblYqnsSjdd> list) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        if(list!=null && list.size()>0) {
        	for (TblYqnsSjdd sjdd : list) {
        		sjdd.setCreatestaffid(staff.getStaffid());
				if(sjdd.getRwid()!=null) {
					tblYqnsSjddMapper.updateById(sjdd);
				}else {
					sjdd.setRwid(RandomUtil.uuBigDecimalId());
					tblYqnsSjddMapper.insert(sjdd);
				}
        		
			}
        }
        return ResponseFormat.retParam(1,200,null);
	}



	@Override
	public JsonBean deleteone(String token, BigDecimal rwid) throws Exception {
		 
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsSjddMapper.deleteoneById(rwid);
        return ResponseFormat.retParam(1,200,null);
	}
	
	
}
