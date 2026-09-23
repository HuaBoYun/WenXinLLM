package com.huabo.system.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblStaff;

public interface TblManageScreenRightService {
    void grantScreenRight(String userid, String priid);

    String getTree(TblStaff tblStaff) throws Exception;

	void grantScreenRightToRole(String roleId, String rightIds) throws Exception;

	JsonBean getScreenRoleRightList(BigDecimal rightId) throws Exception;

	JsonBean getAllRightList(String token, BigDecimal rightId, BigDecimal roleId) throws Exception;
}
