package com.huabo.system.mapper;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblWxUserInfo;

public class TblWxUserInfoSqlConfig {
	
	public String updateWxUserInfoReturnUserId(TblWxUserInfo userInfo) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_WXUSER_INFO(USERID SET STAFFID = STAFFID ");
		
		if(StringUtils.isNotBlank(userInfo.getAvatarUrl())) {
			sql.append(" , AVATARURL = '").append(userInfo.getAvatarUrl()).append("'");
		}
		if(StringUtils.isNotBlank(userInfo.getCity())) {
			sql.append(" , CITY = '").append(userInfo.getCity()).append("'");
		}
		if(StringUtils.isNotBlank(userInfo.getCountry())) {
			sql.append(" , COUNTRY = '").append(userInfo.getCountry()).append("'");
		}
		if(StringUtils.isNotBlank(userInfo.getGender())) {
			sql.append(" , GENDER = '").append(userInfo.getGender()).append("'");
		}
		if(StringUtils.isNotBlank(userInfo.getLanguage())) {
			sql.append(" , LANGUAGE = '").append(userInfo.getLanguage()).append("'");
		}
		if(StringUtils.isNotBlank(userInfo.getNickName())) {
			sql.append(" , NICKNAME = '").append(userInfo.getNickName()).append("'");
		}
		if(StringUtils.isNotBlank(userInfo.getProvince())) {
			sql.append(" , PROVINCE = '").append(userInfo.getProvince()).append("'");
		}
		
		sql.append(" WHERE UNIONID = '").append(userInfo.getUnionId()).append("'");
		return sql.toString();
	}
	
	public String insertWxUserInfoReturnUserId(TblWxUserInfo userInfo) {
		StringBuffer columnsb = new StringBuffer("INSERT INTO TBL_WXUSER_INFO(USERID");
		StringBuffer valueSb = new StringBuffer(") VALUES(").append(RandomUtil.uuBigDecimalId());
		
		if(userInfo.getAvatarUrl() != null && !"".equals(userInfo.getAvatarUrl())) {
			columnsb.append(",AVATARURL");
			valueSb.append(",'"+userInfo.getAvatarUrl()+"'");
		}
		if(userInfo.getCity() != null && !"".equals(userInfo.getCity())) {
			columnsb.append(",CITY");
			valueSb.append(",'"+userInfo.getCity()+"'");
		}
		if(userInfo.getCountry() != null && !"".equals(userInfo.getCountry())) {
			columnsb.append(",COUNTRY");
			valueSb.append(",'"+userInfo.getCountry()+"'");
		}
		if(userInfo.getGender() != null && !"".equals(userInfo.getGender())) {
			columnsb.append(",GENDER");
			valueSb.append(",'"+userInfo.getGender()+"'");
		}
		if(userInfo.getLanguage() != null && !"".equals(userInfo.getLanguage())) {
			columnsb.append(",LANGUAGE");
			valueSb.append(",'"+userInfo.getLanguage()+"'");
		}
		if(userInfo.getNickName() != null && !"".equals(userInfo.getNickName())) {
			columnsb.append(",NICKNAME");
			valueSb.append(",'"+userInfo.getNickName()+"'");
		}
		if(userInfo.getXcxOpenId() != null && !"".equals(userInfo.getXcxOpenId())) {
			columnsb.append(",XCXOPENID");
			valueSb.append(",'"+userInfo.getXcxOpenId()+"'");
		}
		if(userInfo.getProvince() != null && !"".equals(userInfo.getProvince())) {
			columnsb.append(",PROVINCE");
			valueSb.append(",'"+userInfo.getProvince()+"'");
		}
		if(userInfo.getUnionId() != null && !"".equals(userInfo.getUnionId())) {
			columnsb.append(",UNIONID");
			valueSb.append(",'"+userInfo.getUnionId()+"'");
		}
		if(userInfo.getAppName() != null && !"".equals(userInfo.getAppName())) {
			columnsb.append(",APPNAME");
			valueSb.append(",'"+userInfo.getAppName()+"'");
		}
		if(userInfo.getStaffId()!= null && !"".equals(userInfo.getStaffId())) {
			columnsb.append(",STAFFID");
			valueSb.append(",'"+userInfo.getStaffId()+"'");
		}
		valueSb.append(")");
		String sql = columnsb.toString()+valueSb.toString();
		return sql;
	}
}
