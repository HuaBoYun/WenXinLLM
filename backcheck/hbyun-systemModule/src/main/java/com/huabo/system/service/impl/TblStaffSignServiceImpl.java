package com.huabo.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblStaffSign;
import com.huabo.system.mapper.TblStaffSignMapper;
import com.huabo.system.service.TblStaffSignService;

@Service
public class TblStaffSignServiceImpl implements TblStaffSignService {

	@Resource
	private TblStaffSignMapper tblStaffSignMapper;
	
	@Resource
    private UserProvider userProvider;
	
	
	@Override
	public JsonBean saveSignInfo(TblStaffSign sign, String token) throws Exception {
		TblStaffUtil loginStaff =userProvider.get();
	    if (loginStaff == null) {
	    	return ResponseFormat.retParam(0, 20006, null);
	    }
	    
	    if(sign.getSignstaff() == null) {
	    	return ResponseFormat.retParam(0, "缺少当前签名所属人信息！", null);
	    }
	    
	    QueryWrapper<TblStaffSign> wrapper = new QueryWrapper<TblStaffSign>();
	    wrapper.eq("SIGNSTAFF", sign.getSignstaff());
	    
		Integer count = this.tblStaffSignMapper.selectCount(wrapper).intValue();
	    if(count > 0) {
	    	return ResponseFormat.retParam(0, "当前签署人已有签名！", null);
	    }
	    
	    sign.setSignid(RandomUtil.uuStringId());
	    sign.setCreatedate(new Date());
	    sign.setCreatestaff(loginStaff.getStaffid());
	    sign.setCreatestaffname(loginStaff.getRealname());
	    this.tblStaffSignMapper.insert(sign);
		return  ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean saveDistribution(TblStaffSign sign, String token) throws Exception {
		TblStaffUtil loginStaff =userProvider.get();
	    if (loginStaff == null) {
	       return ResponseFormat.retParam(0, 20006, null);
	    }
	    if(StringUtils.isBlank(sign.getSignid())) {
	    	return ResponseFormat.retParam(0, 10004, null);
	    }
	    
	    sign.setUpdatedate(new Date());
	    sign.setUpdatestaff(loginStaff.getStaffid());
	    sign.setUpdatestaffname(loginStaff.getRealname());
	    this.tblStaffSignMapper.updateById(sign);
		return  ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean removeSignInfo(String signid, String token) throws Exception {
		TblStaffUtil loginStaff =userProvider.get();
	    if (loginStaff == null) {
	       return ResponseFormat.retParam(0, 20006, null);
	    }
	    if(StringUtils.isBlank(signid)) {
	    	return ResponseFormat.retParam(0, 10004, null);
	    }
	    
	    this.tblStaffSignMapper.deleteById(signid);
		return  ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getSignInfo(String signid, String token) throws Exception {
		TblStaffUtil loginStaff =userProvider.get();
	    if (loginStaff == null) {
	       return ResponseFormat.retParam(0, 20006, null);
	    }
	    if(StringUtils.isBlank(signid)) {
	    	return ResponseFormat.retParam(0, 10004, null);
	    }
	    
	    TblStaffSign sign = this.tblStaffSignMapper.selectById(signid);
		return  ResponseFormat.retParam(1, 200, sign);
	}

	@Override
	public JsonBean getSignList(TblStaffSign sign, Integer pageNumber, Integer pageSize, String token) throws Exception {
		TblStaffUtil loginStaff =userProvider.get();
	    if (loginStaff == null) {
	       return ResponseFormat.retParam(0, 20006, null);
	    }
	    Page<TblStaffSign> page = new Page<TblStaffSign>(pageNumber,pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblStaffSign> pageList =  this.tblStaffSignMapper.selectListPage(sign,page);
	    
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean getSignNatureList(String token) throws Exception {
		TblStaffUtil loginStaff =userProvider.get();
	    if (loginStaff == null) {
	       return ResponseFormat.retParam(0, 20006, null);
	    }
	    
	    QueryWrapper<TblStaffSign> wrapper = new QueryWrapper<TblStaffSign>();
	    wrapper.eq("SIGNSTAFF", loginStaff.getStaffid());
	    List<TblStaffSign> signList = this.tblStaffSignMapper.selectList(wrapper);
		
	    List<String> natureList = signList.stream().map(TblStaffSign::getSignature).collect(Collectors.toList());
	    
		return ResponseFormat.retParam(1, 200, natureList);
	}

}
