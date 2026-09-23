package com.huabo.system.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblImplog;
import com.huabo.system.entity.TblWxUserInfo;
import com.huabo.system.mapper.TblImplogMapper;
import com.huabo.system.mapper.TblWxUserInfoMapper;
import com.huabo.system.service.TblImplogService;
import com.huabo.system.service.TblWxUserInfoService;
import com.huabo.system.vo.result.AiHistory;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblWxUserInfoServiceImpl implements TblWxUserInfoService {

	@Resource
	private TblWxUserInfoMapper tblWxUserInfoMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public TblWxUserInfo findWxUserInfoByOpenId(String unionId) throws Exception {
		return tblWxUserInfoMapper.getuser(unionId);
	}

	@Override
	public void updateAvatarUrl(TblWxUserInfo userInfo) throws Exception {
		tblWxUserInfoMapper.updateAvatarUrl(userInfo);
	}

	@Override
	public JsonBean generateDoc(String token, String prompt, String history, boolean think) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		 	HashMap<String, Object> paramMap = new HashMap<String, Object>(0);
	        paramMap.put("prompt", prompt);
	        paramMap.put("temperature", 1.0);
	        paramMap.put("max_tokens", 2048);
	        paramMap.put("think", think);
	        
	        if(StringUtils.isNotBlank(history)) {
	        	List<AiHistory> list = JSONArray.parseArray(history,AiHistory.class);
	        	paramMap.put("history", list);
	        }
	        Map<String, String> headerMap = new HashMap<String,String>(0);
	        headerMap.put("Content-Type", "application/json");
	        String result = HttpClient.httpPostClient(HttpClient.genDoc, paramMap,headerMap,HttpClient.PARAMBODY);
	        System.out.println(result);
	        return ResponseFormat.retParam(200, 200, result);
	}

}
