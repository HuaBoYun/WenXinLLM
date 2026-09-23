package com.huabo.cybermonitor.util;

import java.math.BigDecimal;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.cybermonitor.exception.ServiceException;
import com.huabo.cybermonitor.vo.UserInfoParam;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class TokenUtil {

	private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);

	/**
	 * token解析用户数据
	 * @param token
	 * @return
	 */
	public static UserInfoParam getUserInfo(TblStaffUtil loginStaff) {
		try {
			BigDecimal creator = loginStaff.getStaffid();
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
