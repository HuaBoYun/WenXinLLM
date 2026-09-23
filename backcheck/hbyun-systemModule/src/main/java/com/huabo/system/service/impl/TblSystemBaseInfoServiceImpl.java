package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemRight;
import com.huabo.system.entity.TblSystemTableNoConfig;
import com.huabo.system.entity.TblSystemUrgentEvents;
import com.huabo.system.mapper.TblFlowInformInfoMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemDistributionMapper;
import com.huabo.system.mapper.TblSystemRightMapper;
import com.huabo.system.mapper.TblSystemTableNoConfigMapper;
import com.huabo.system.mapper.TblSystemUrgentEventsMapper;
import com.huabo.system.service.TblSystemBaseInfoService;

@Service
@Transactional
public class TblSystemBaseInfoServiceImpl implements TblSystemBaseInfoService {
	
	@Resource
	private TblSystemTableNoConfigMapper tblSystemTableNoConfigMapper;
	
	@Resource
	private TblSystemUrgentEventsMapper tblSystemUrgentEventsMapper;
	
	@Resource
	private TblSystemRightMapper tblSystemRightMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Resource
	private TblStaffMapper tblStaffMapper;
	
	@Resource
	private TblFlowInformInfoMapper tblFlowInformInfoMapper;
	
	@Resource
	private TblSystemDistributionMapper tblSystemDistributionMapper;

	@Override
	public JsonBean getAutoNumber(String token,String configId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        TblSystemTableNoConfig config = this.tblSystemTableNoConfigMapper.selectById(configId);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String noRule = year+"-"+month+"-"+config.getFirstNo()+"-"+config.getSecondNo()+"-"+config.getThirdNo()+"-";
        String queryRule = year+"-%-"+config.getFirstNo()+"-"+config.getSecondNo()+"-"+config.getThirdNo()+"-";
        String sql ="SELECT "+DataBaseSqlConfig.getMaxNoDeal(config.getNumberColumn(), "-", noRule)+" FROM "+config.getTableName()+" WHERE "+config.getNumberColumn() +" LIKE '"+queryRule+"%'";
        BigDecimal no = this.tblSystemTableNoConfigMapper.executeSelectSql(sql);
        
        String resultNo = "";
        if(no == null) {
        	resultNo = noRule+"01";
        }else {
        	no = no.add(BigDecimal.valueOf(1));
        	if(no.toString().length() < 2) {
        		resultNo = noRule+"0"+no;
        	}else {
        		resultNo = noRule+no;
        	}
        }
        return ResponseFormat.retParam(1, 200, resultNo);
	}

	@Override
	public JsonBean saveUrgentEvent(String token, TblSystemUrgentEvents events) throws Exception {
		if(StringUtils.isBlank(events.getConfigId()) || StringUtils.isBlank(events.getReformId())) {
			return ResponseFormat.retParam(0, 10004, null);
		}
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        TblSystemTableNoConfig config = this.tblSystemTableNoConfigMapper.selectById(events.getConfigId());
        if(config == null) {
        	return ResponseFormat.retParam(0, "缺少系统配置，无法催办", null);
        }
        TblSystemRight right = this.tblSystemRightMapper.selectById(config.getRightId());
        if(StringUtils.isBlank(config.getUrgeFindSql())) {
        	return ResponseFormat.retParam(0, "缺少获取催办人配置，无法催办", null);
        }
        
        String sql = String.format(config.getUrgeFindSql(), events.getReformId());
        
        BigDecimal urgentStaffId = this.tblSystemTableNoConfigMapper.executeSelectSql(sql);
        
        if(urgentStaffId == null) {
        	return ResponseFormat.retParam(0, "缺少催办人无法催办", null);
        }
        
        TblStaff staff = this.tblStaffMapper.selectStaff(urgentStaffId.toString());
        events.setModuleName(right.getName());
        events.setModuleType(right.getModuletype());
        events.setEventId(RandomUtil.uuStringId());
        events.setCreateStaff(loginStaff.getStaffid());
        events.setCreateStaffName(loginStaff.getRealname());
        events.setCreateTime(new Date());
        events.setIsConfirm(0);
        events.setRecipient(staff.getStaffid());
        events.setRecipientName(staff.getRealname());
        
        this.tblSystemUrgentEventsMapper.insert(events);
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean modifyUrgentEvent(String token, TblSystemUrgentEvents events) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        events.setIsConfirm(1);
        events.setConfirmTime(new Date());
        this.tblSystemUrgentEventsMapper.updateById(events);
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getUrgentEvent(String token, String eventId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblSystemUrgentEvents event = this.tblSystemUrgentEventsMapper.selectById(eventId);
        return ResponseFormat.retParam(1, 200, event);
	}

	@Override
	public JsonBean recipientList(String token, TblSystemUrgentEvents events, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblSystemUrgentEvents> wrapper = new QueryWrapper<TblSystemUrgentEvents>();
        
        wrapper.eq("RECIPIENT", loginStaff.getStaffid());
        if(StringUtils.isNotBlank(events.getModuleType())) {
        	wrapper.eq("MODULETYPE", events.getModuleType());
        }
        
        if(events.getIsConfirm() != null) {
        	wrapper.eq("ISCONFIRM", events.getIsConfirm());
        }
        
        wrapper.orderByDesc("ISCONFIRM").orderByAsc("CREATETIME");
        Page<TblSystemUrgentEvents> page = new Page<TblSystemUrgentEvents>(pageNumber,pageSize);
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblSystemUrgentEvents> pageList = tblSystemUrgentEventsMapper.selectPage(page, wrapper);
        
        return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean initiatiorUrgentList(String token, TblSystemUrgentEvents events, Integer pageNumber,
			Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblSystemUrgentEvents> wrapper = new QueryWrapper<TblSystemUrgentEvents>();
        
        wrapper.eq("CREATESTAFF", loginStaff.getStaffid());
        if(StringUtils.isNotBlank(events.getModuleType())) {
        	wrapper.eq("MODULETYPE", events.getModuleType());
        }
        
        if(events.getIsConfirm() != null) {
        	wrapper.eq("ISCONFIRM", events.getIsConfirm());
        }
        
        wrapper.orderByDesc("CREATETIME");
        Page<TblSystemUrgentEvents> page = new Page<TblSystemUrgentEvents>(pageNumber,pageSize);
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblSystemUrgentEvents> pageList = tblSystemUrgentEventsMapper.selectPage(page, wrapper);
        
        return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean loginRecipientList(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblSystemUrgentEvents> wrapper = new QueryWrapper<TblSystemUrgentEvents>();
        
        wrapper.eq("RECIPIENT", loginStaff.getStaffid());
        
        wrapper.eq("ISCONFIRM", 0);
        
        wrapper.orderByAsc("CREATETIME");
        List<TblSystemUrgentEvents> list = tblSystemUrgentEventsMapper.selectList(wrapper);
        return ResponseFormat.retParam(1, 200, list);
	}

	@Override
	public JsonBean getFtpInfo(String url, Integer port, String userName, String userp) throws Exception {
		String result = FtpUtil.getFtpInfo(url ,port, userName,userp);
		return ResponseFormat.retParam(1, 200, result);
	}

	@Override
	public JsonBean getRefreshInfo() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		Object num = userProvider.getActiveUsersNum();
		resultMap.put("activeUsersNum", num);//系统在线人数
		resultMap.put("systemVersion", SystemStaticValue.SYSTEMVERSION);//系统版本号
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean getPendingProcessingAllNum() throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Integer total = 0;
        
        Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("currentPage",1);
		filedMap.put("pageSize",1);
		//查询待审批的工作流程
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				total = this.setFiveZeroOneCount(filedMap,headerMap,loginStaff,total);
				break;
			default:
				total = this.setThreeFourTwoCount(filedMap,headerMap,loginStaff,total);
				break;
		}
		
		//查询抄送 未读的数量
		Integer noreadCount = this.tblFlowInformInfoMapper.selectNoReadCountyLoginUser(loginStaff.getStaffid());
		total += noreadCount;
        
		//查询下发给当前人未确认的数量
		Integer noConfirmCount = this.tblSystemDistributionMapper.selectNoConfirmCount(loginStaff.getStaffid());
		total += noConfirmCount;
		return ResponseFormat.retParam(1, 200, total);
	}

	private Integer setFiveZeroOneCount(HashMap<String, Object> filedMap, Map<String, String> headerMap,
			TblStaffUtil loginStaff, Integer total) throws Exception {
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.operatorListUrl+"1",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code == 200) {
			JSONObject dataJson = reJson.getJSONObject("data");
			Map<String,Object> dataMap = new HashMap<String,Object>(0);
			dataMap.put("list", dataJson.getJSONArray("list"));
			JSONObject pageJson = dataJson.getJSONObject("pagination");
			total += pageJson.getInteger("total");
		}
		
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.operatorListUrl+"2",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		reJson = JSONObject.parseObject(result);
		if(code == 200) {
			JSONObject dataJson = reJson.getJSONObject("data");
			Map<String,Object> dataMap = new HashMap<String,Object>(0);
			dataMap.put("list", dataJson.getJSONArray("list"));
			JSONObject pageJson = dataJson.getJSONObject("pagination");
			total += pageJson.getInteger("total");
		}
		
		filedMap.put("status",3);
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.taskList,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		reJson = JSONObject.parseObject(result);
		if(code == 200) {
			JSONObject dataJson = reJson.getJSONObject("data");
			Map<String,Object> dataMap = new HashMap<String,Object>(0);
			dataMap.put("list", dataJson.getJSONArray("list"));
			JSONObject pageJson = dataJson.getJSONObject("pagination");
			total += pageJson.getInteger("total");
		}
		return total;
	}

	private Integer setThreeFourTwoCount(HashMap<String, Object> filedMap,Map<String, String> headerMap,TblStaffUtil loginStaff,Integer total) throws Exception {
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workList+"1",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code == 200) {
			JSONObject dataJson = reJson.getJSONObject("data");
			Map<String,Object> dataMap = new HashMap<String,Object>(0);
			dataMap.put("list", dataJson.getJSONArray("list"));
			JSONObject pageJson = dataJson.getJSONObject("pagination");
			dataMap.put("currentPage", pageJson.getInteger("currentPage"));
			dataMap.put("pageSize", pageJson.getInteger("pageSize"));
			total += pageJson.getInteger("total");
		}
		
		//查询被拒绝的工作流程
		filedMap.put("status",3);
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowLaunch,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		reJson = JSONObject.parseObject(result);
		code = reJson.getInteger("code");
		if(code == 200) {
			JSONObject dataJson = reJson.getJSONObject("data");
			Map<String,Object> dataMap = new HashMap<String,Object>(0);
			dataMap.put("list", dataJson.getJSONArray("list"));
			JSONObject pageJson = dataJson.getJSONObject("pagination");
			dataMap.put("currentPage", pageJson.getInteger("currentPage"));
			dataMap.put("pageSize", pageJson.getInteger("pageSize"));
			total += pageJson.getInteger("total");
		}
		
		return total;
	}


}