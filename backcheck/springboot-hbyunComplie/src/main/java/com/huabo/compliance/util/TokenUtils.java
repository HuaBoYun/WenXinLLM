package com.huabo.compliance.util;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.vo.param.UserInfoParam;
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
