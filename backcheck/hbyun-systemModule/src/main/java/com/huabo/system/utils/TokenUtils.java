package com.huabo.system.utils;

import java.math.BigDecimal;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.vo.param.UserInfoParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {

	/**
	 * token解析用户数据
	 * @param loginStaff2
	 * @return
	 */
	public static UserInfoParam getUserInfo(TblStaffUtil loginStaff) {
		try {
			BigDecimal creator = loginStaff.getStaffid();
			String creatorName = loginStaff.getRealname();
			BigDecimal workUnit = loginStaff.getLinkDetp().getOrgid();
			BigDecimal belongGroup = loginStaff.getCurrentOrg().getOrgid();
			String roleIdStrs = loginStaff.getRoleIdStrs();
			return new UserInfoParam(creator, creatorName, workUnit, belongGroup,roleIdStrs);
		} catch (Exception e) {
			log.error("用户token解析失败：", e);
		}
		throw new ServiceException(401, 20006);
	}

}
