package com.huabo.audit.util;

import java.math.BigDecimal;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.vo.result.UserInfoParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtil {

	/**
	 * token解析用户数据
	 * @param staff
	 * @return
	 */
	public static UserInfoParam getUserInfo(TblStaffUtil loginStaff) {
		try {
			BigDecimal creator = loginStaff.getStaffid();
			String creatorName = loginStaff.getRealname();
			BigDecimal workUnit = loginStaff.getCurrentOrg().getOrgid();
			BigDecimal belongGroup = loginStaff.getLinkOrg().getOrgid();
			return new UserInfoParam(creator, creatorName, workUnit, belongGroup);
		} catch (Exception e) {
			log.error("用户token解析失败：", e);
		}
		throw new ServiceException(401, 20006);
	}

}
