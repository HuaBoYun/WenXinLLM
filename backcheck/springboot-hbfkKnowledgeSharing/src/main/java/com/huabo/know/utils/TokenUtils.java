package com.huabo.know.utils;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.know.exception.ServiceException;
import com.huabo.know.vo.param.UserInfoParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {

	/**
	 * token解析用户数据
	 * @param token
	 * @return
	 */
	public static UserInfoParam getUserInfo(TblStaffUtil loginStaff) {
		try {
			int creator = loginStaff.getStaffid().intValue();
			String creatorName = loginStaff.getRealname();
			int workUnit = loginStaff.getLinkDetp().getOrgid().intValue();
			int belongGroup = loginStaff.getCurrentOrg().getOrgid().intValue();
			return new UserInfoParam(creator, creatorName, workUnit, belongGroup);
		} catch (Exception e) {
			log.error("用户token解析失败：", e);
		}
		throw new ServiceException(401, 20006);
	}
}
