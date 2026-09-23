package com.huabo.monitor.util;

import java.math.BigDecimal;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.config.ServiceException;
import com.huabo.monitor.vo.param.UserInfoParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {

	/**
	 * token解析用户数据
	 * @param user
	 * @return
	 */
	public static UserInfoParam getUserInfo(TblStaffUtil loginStaff) {
		try {
			BigDecimal creator = loginStaff.getStaffid();
			String creatorName = loginStaff.getRealname();
			BigDecimal workUnit = loginStaff.getLinkDetp().getOrgid();
			BigDecimal belongGroup = loginStaff.getCurrentOrg().getOrgid();
			return new UserInfoParam(creator, creatorName, workUnit, belongGroup);
		} catch (Exception e) {
			log.error("用户token解析失败：", e);
		}
		throw new ServiceException(401, 20006);
	}
}
