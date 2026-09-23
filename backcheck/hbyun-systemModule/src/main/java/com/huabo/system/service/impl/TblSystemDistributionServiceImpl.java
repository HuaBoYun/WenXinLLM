package com.huabo.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.RandowUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.constant.DistributionTypeFunc;
import com.huabo.system.entity.TblSystemDistribution;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemDistributionMapper;
import com.huabo.system.service.TblSystemDistributionService;
import com.huabo.system.vo.TblSystemDistributionTypeVo;
import com.huabo.system.vo.TblSystemDistributionVo;

import redis.clients.jedis.Jedis;

@Service
public class TblSystemDistributionServiceImpl implements TblSystemDistributionService {

	@Resource
    private TblSystemDistributionMapper tblSystemDistributionMapper;
    

    @Resource
	private TblStaffMapper tblStaffMapper;
    
    @Resource
    private UserProvider userProvider;
    
    
    
    @Override
	public JsonBean saveDistributions(String token, String jsondistribution, String formType) throws Exception {
		
		Jedis jedis = null;
		try {
			//验证用户是否登录
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			if(jsondistribution!=null && jsondistribution.length()>0) {
				
				List<TblSystemDistribution> list = JSONArray.parseArray(jsondistribution,TblSystemDistribution.class);
				for (TblSystemDistribution distribution : list) {
					//根据formType保存单据类型
					DistributionTypeFunc.setDistributionTypeInfo(formType,distribution);
					distribution.setDistributionId(RandowUtil.uuId());
					distribution.setCreateStaff(loginStaff.getStaffid());
					distribution.setCreateTime(new Date());
					
					this.tblSystemDistributionMapper.insertSelective(distribution);
					
					//从redis 中获取用户之前的下发提示数据
					List<TblSystemDistributionVo> distriList = new ArrayList<TblSystemDistributionVo>(0);
//					jedis = JedisUtil.getJedis();
//					
//					String disStr = jedis.get(distribution.getReciver() + JedisUtil.DISTRIKEY);
//					if(StringUtils.isNotBlank(disStr)) {
//						distriList = JSONObject.parseArray(disStr, TblSystemDistributionVo.class);
//					}
					TblSystemDistributionVo disVo = new TblSystemDistributionVo();
					disVo.setCreateStaff(distribution.getCreateStaff());
					disVo.setCreateTime(distribution.getCreateTime());
					disVo.setCreateStaffName(loginStaff.getRealname());
					disVo.setDistributionId(distribution.getDistributionId());
					disVo.setDistributionTitle(distribution.getDistributionTitle());
					disVo.setDistributionType(distribution.getDistributionType());
					disVo.setFormId(distribution.getFormId());
					disVo.setIsread(distribution.getIsread());
					disVo.setModuleType(distribution.getModuleType());
					disVo.setPageUrl(distribution.getPageUrl());
					disVo.setReciver(distribution.getReciver());
					distriList.add(disVo);
//					jedis.set(distribution.getReciver() + JedisUtil.DISTRIKEY, JSONObject.toJSONString(distriList));
				}
				
				
			}
		}finally {
			JedisUtil.returnResource(jedis);
		}
		return ResponseFormat.retParam(1, 200, null);
	}
    
    
    
    
    
	@Override
	public JsonBean saveDistribution(String token, TblSystemDistribution distribution, String formType) throws Exception {
		
		Jedis jedis = null;
		try {
			//验证用户是否登录
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			//根据formType保存单据类型
			DistributionTypeFunc.setDistributionTypeInfo(formType,distribution);
			distribution.setDistributionId(RandowUtil.uuId());
			distribution.setCreateStaff(loginStaff.getStaffid());
			distribution.setCreateTime(new Date());
			
			this.tblSystemDistributionMapper.insertSelective(distribution);
			
			//从redis 中获取用户之前的下发提示数据
			List<TblSystemDistributionVo> distriList = new ArrayList<TblSystemDistributionVo>(0);
			jedis = JedisUtil.getJedis();
			
			String disStr = jedis.get(distribution.getReciver() + JedisUtil.DISTRIKEY);
			if(StringUtils.isNotBlank(disStr)) {
				distriList = JSONObject.parseArray(disStr, TblSystemDistributionVo.class);
			}
			TblSystemDistributionVo disVo = new TblSystemDistributionVo();
			disVo.setCreateStaff(distribution.getCreateStaff());
			disVo.setCreateTime(distribution.getCreateTime());
			disVo.setCreateStaffName(loginStaff.getRealname());
			disVo.setDistributionId(distribution.getDistributionId());
			disVo.setDistributionTitle(distribution.getDistributionTitle());
			disVo.setDistributionType(distribution.getDistributionType());
			disVo.setFormId(distribution.getFormId());
			disVo.setIsread(distribution.getIsread());
			disVo.setModuleType(distribution.getModuleType());
			disVo.setPageUrl(distribution.getPageUrl());
			disVo.setReciver(distribution.getReciver());
			distriList.add(disVo);
			jedis.set(distribution.getReciver() + JedisUtil.DISTRIKEY, JSONObject.toJSONString(distriList));
			
		}finally {
			JedisUtil.returnResource(jedis);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getDistributionListPage(String token, TblSystemDistributionVo distribution, Integer pageSize,
			Integer pageNumber) throws Exception {
		//验证用户是否登录
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		//封装分页实体
		PageInfo<TblSystemDistributionVo> pageInfo = new PageInfo<TblSystemDistributionVo>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		distribution.setReciver(loginStaff.getStaffid());//查询当前登录人的 未处理消息
		pageInfo.setCondition(distribution);
		
		//查询获取分页列表和 总数量
		pageInfo.setTlist(this.tblSystemDistributionMapper.selectDistributionListPageInfo(pageInfo));
		pageInfo.setTotalRecord(this.tblSystemDistributionMapper.selectDistributionCountPageInfo(pageInfo));
		
		return ResponseFormat.retParam(1, 200, pageInfo);
	}

	@Override
	public JsonBean modifyDistributionInfo(String token, TblSystemDistribution distribution) throws Exception {
		Jedis jedis = null;
		try {
			
			//验证用户是否登录
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			//修改实体信息
			distribution.setReciveDate(new Date());
			distribution.setIsread(1);
			this.tblSystemDistributionMapper.updateByPrimaryKeySelective(distribution);
			
			//删除redis 中未处理的信息
			jedis = JedisUtil.getJedis(); 
			String disStr = jedis.get(loginStaff.getStaffid() + JedisUtil.DISTRIKEY);
			if(StringUtils.isNotBlank(disStr)) {
				List<TblSystemDistributionVo>  distriList = JSONObject.parseArray(disStr, TblSystemDistributionVo.class);
				distriList = distriList.stream().filter(item -> !distribution.getDistributionId().equals(item.getDistributionId())).collect(Collectors.toList());
				jedis.set(loginStaff.getStaffid() + JedisUtil.DISTRIKEY, JSONObject.toJSONString(distriList));
			}
		}finally {
			JedisUtil.returnResource(jedis);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getDistributionType(String token, TblSystemDistributionVo distribution) throws Exception {
		//验证用户是否登录
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		List<TblSystemDistributionTypeVo> typeList = new ArrayList<TblSystemDistributionTypeVo>(0);
		
		distribution.setReciver(loginStaff.getStaffid());
		
		
		//查询获取分页列表和 总数量
		List<TblSystemDistributionVo> disTypeList = this.tblSystemDistributionMapper.selectDistributionAllType(distribution);
		
		for (TblSystemDistributionVo dist : disTypeList) {
			typeList.add(DistributionTypeFunc.getDistributionTypeInfo(dist));
		}
		
		return ResponseFormat.retParam(1, 200, typeList); 
	}

	@Override
	public JsonBean deleteDistribution(String token, String ids) throws Exception {
		//验证用户是否登录
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		if(ids!=null && ids.length()>0) {
			tblSystemDistributionMapper.deletebyids(ids);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean batchModifyDistributionInfo(String token, String[] ids) throws Exception {
		Jedis jedis = null;
		try {
			//验证用户是否登录
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			
			String idStr = String.join(",", ids);
			
			idStr = "'"+idStr.replace(",", "','")+"'";
			
			//修改实体信息
			this.tblSystemDistributionMapper.updateBatchReadStatus(idStr);
			
			//删除redis 中未处理的信息
			jedis = JedisUtil.getJedis(); 
			String disStr = jedis.get(loginStaff.getStaffid() + JedisUtil.DISTRIKEY);
			if(StringUtils.isNotBlank(disStr)) {
				List<TblSystemDistributionVo>  distriList = JSONObject.parseArray(disStr, TblSystemDistributionVo.class);
				List<TblSystemDistributionVo> distriVoList = distriList;
				for (TblSystemDistributionVo vo : distriList) {
					if(idStr.indexOf("'"+vo.getDistributionId()+"'") != -1) {
						distriVoList.remove(vo);
					}
				}
				jedis.set(loginStaff.getStaffid() + JedisUtil.DISTRIKEY, JSONObject.toJSONString(distriVoList));
			}
		}finally {
			JedisUtil.returnResource(jedis);
		}
		return ResponseFormat.retParam(1, 200, null);
	}





	@Override
	public Integer getDistributionCount(String token) throws Exception {
		//验证用户是否登录
		TblStaffUtil loginStaff = userProvider.get(token);
				if (loginStaff == null) {
					return 0;
				}
				
				Map<String, Integer> map=new HashMap<String, Integer>();
				Integer noCount=tblSystemDistributionMapper.getDistributionCount(loginStaff.getStaffid());
	     return noCount;
	}

}
