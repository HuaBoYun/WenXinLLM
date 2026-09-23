package com.huabo.system.service;



import com.alibaba.fastjson.JSONArray;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblImplog;
import com.huabo.system.entity.TblWxUserInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public interface TblWxUserInfoService{

	TblWxUserInfo findWxUserInfoByOpenId(String unionId) throws Exception;

	void updateAvatarUrl(TblWxUserInfo userInfo) throws Exception;

	JsonBean generateDoc(String token, String prompt, String history, boolean think) throws Exception;

}
