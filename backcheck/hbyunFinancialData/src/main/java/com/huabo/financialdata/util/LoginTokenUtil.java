package com.huabo.financialdata.util;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.financialdata.config.exception.BizException;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;

import java.util.Objects;

/**
 * 登录token工具栏
 *
 * @author lee
 * @version 1.0.0
 **/
public class LoginTokenUtil {

    /**
     * 登录token 解析
     *
     * @param staff2 登录token
     * @return 返回精简后的信息
     */
    public static LoginUserInfoDO tokenAnalysis(TblStaffUtil staff) {

        LoginUserInfoDO userInfoDO = new LoginUserInfoDO();
        userInfoDO.setStaffId(staff.getStaffid());
        userInfoDO.setLinkOrgId(staff.getLinkOrg().getOrgid());
        userInfoDO.setCurrentOrgId(staff.getCurrentOrg().getOrgid());
        userInfoDO.setRealName(staff.getRealname());
        return userInfoDO;
    }

}
