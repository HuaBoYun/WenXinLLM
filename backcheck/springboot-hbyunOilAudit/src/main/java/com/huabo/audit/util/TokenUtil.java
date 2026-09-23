package com.huabo.audit.util;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.vo.result.UserInfoParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtil {

	/**
	 * token解析用户数据
	 * @param token
	 * @return
	 */
	public static UserInfoParam getUserInfo(TblStaffUtil loginStaff) {
		try {
			int creator = loginStaff.getStaffid().intValue();
			String creatorName = loginStaff.getRealname();
			int workUnit = loginStaff.getCurrentOrg().getOrgid().intValue();
			int belongGroup = loginStaff.getLinkOrg().getOrgid().intValue();
			return new UserInfoParam(creator, creatorName, workUnit, belongGroup);
		} catch (Exception e) {
			log.error("用户token解析失败：", e);
		}
		throw new ServiceException(401, 20006);
	}

}
