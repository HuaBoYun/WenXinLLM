package com.huabo.system.oracle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.mapper.ScheduledTaskMapper;
import com.huabo.system.oracle.service.ScheduledTaskService;
import com.huabo.system.vo.result.TimeoutEntityResult;

import redis.clients.jedis.Jedis;

@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

	@Resource
	private ScheduledTaskMapper scheduledTaskMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean setCurrentContractDataRemind() throws Exception {
		Jedis jedis = null;
		try {
			//1.获取需要提醒的合同落实数据
			jedis = JedisUtil.getJedis();
			//查询距离履行阶段截止日期3 、7 、 15 天的履行借口都信息；
			List<Map<String, Object>> resultList = this.scheduledTaskMapper.selectContractPlanNodeTimeout();
			List<TimeoutEntityResult> enList = new ArrayList<TimeoutEntityResult>(0);
			TimeoutEntityResult result = null;
			Integer preStaffId = null;
			Integer cutStaffId = null;
			//循环放入经办人需提醒的履行阶段信息，并放入redis中；
			for (Map<String, Object> entity : resultList) {
				result = new TimeoutEntityResult();
				cutStaffId = this.getMapValueInteger(entity, "DISPATCHSTAFF");
				if(!cutStaffId.equals(preStaffId)) {
					if(preStaffId != null) {
						//将上一个人需要提醒的数据放入至redis
						jedis.set(preStaffId + TimeoutEntityResult.CONTRACTSTAGEIMPLTIMEOUT, JSONObject.toJSONString(enList));
					}
					preStaffId = cutStaffId;
					enList = new ArrayList<TimeoutEntityResult>(0);
				}
				result.setStaffId(cutStaffId);
				result.setFormId(this.getMapValueInteger(entity, "CONTRACTID"));
				result.setTitle(this.getMapValueString(entity, "CONTRACTNAME")+"履行阶段落实超时提醒");
				result.setContent("合同编号："+this.getMapValueString(entity, "CONTRACTNO")+"，合同名称："+this.getMapValueString(entity, "CONTRACTNAME")+"中的履行阶段已经超时"+this.getMapValueInteger(entity, "DIFFDAY")+"天未完成落实，请及时处理！");
				result.setModuletype("TblCyhwUnit");
				enList.add(result);
			}
			if(preStaffId != null) {
				//将最后一人需要提醒的数据放入至redis
				jedis.set(preStaffId + TimeoutEntityResult.CONTRACTSTAGEIMPLTIMEOUT, JSONObject.toJSONString(enList));
			}
			
			resultList = this.scheduledTaskMapper.selectContractPlanNodePaymentTimeout();
		}finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
		return null;
	}
	
	@Override
	public JsonBean getRemindInfoListAll(String token) throws Exception {
		Jedis jedis = null;
		List<TimeoutEntityResult> enList = new ArrayList<TimeoutEntityResult>(0);
		try {
			jedis = JedisUtil.getJedis();
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			//从redis中获取需要提醒的预警信息
			String tipStrs = jedis.get(loginStaff.getStaffid() + TimeoutEntityResult.CONTRACTSTAGEIMPLTIMEOUT);
			if(StringUtils.isNotBlank(tipStrs)) {
				enList = JSONObject.parseArray(tipStrs, TimeoutEntityResult.class);
				jedis.del(loginStaff.getStaffid() + TimeoutEntityResult.CONTRACTSTAGEIMPLTIMEOUT);
			}
			
		}finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
		return ResponseFormat.retParam(1,200, enList);
	}

	
	private Object getMapValueObj(Map<String, Object> dataMap, String key) {
		return dataMap.containsKey(key)?dataMap.get(key):null;
	}
	
	private Integer getMapValueInteger(Map<String, Object> dataMap, String key) {
		return dataMap.containsKey(key)?Integer.parseInt(dataMap.get(key).toString()):null;
	}
	
	private String getMapValueString(Map<String, Object> dataMap, String key) {
		return dataMap.containsKey(key)?dataMap.get(key).toString():null;
	}


	
}
