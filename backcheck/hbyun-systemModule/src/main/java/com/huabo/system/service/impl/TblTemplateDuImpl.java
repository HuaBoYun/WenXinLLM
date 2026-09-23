package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblTemplateDu;
import com.huabo.system.mapper.TblTemplateDuMapper;
import com.huabo.system.service.TblTemplateDuService;

@Service
public class TblTemplateDuImpl implements TblTemplateDuService {

    @Resource
    private TblTemplateDuMapper tblTemplateDuMapper;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveEntity(String token, String tempType, String tempTitle, String tempContent) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblTemplateDu temp = new TblTemplateDu();
		temp.setPre("pre1.png");
		temp.setType(tempType);
		temp.setTitle(tempTitle);
		temp.setHtml(tempContent);
		temp.setTempId(RandomUtil.uuBigDecimalId());
		this.tblTemplateDuMapper.insert(temp);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean removeEntity(String token, BigDecimal tempId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		this.tblTemplateDuMapper.deleteById(tempId);
		
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean getList(String token, String tempType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		QueryWrapper<TblTemplateDu> wrapper = new QueryWrapper<TblTemplateDu>();
		wrapper.eq("TYPE", tempType);
		
		List<TblTemplateDu> tempList= this.tblTemplateDuMapper.selectList(wrapper);
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("tempList", tempList);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean get(String token, BigDecimal tempId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblTemplateDu temp= this.tblTemplateDuMapper.selectById(tempId);
		return ResponseFormat.retParam(1,200,temp);
	}
}
