package com.huabo.finance.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.caiji.OrgOrgs;
import com.huabo.finance.vo.OrgOrgsVo;

/**
 * <p>
 * 财务组织信息 服务类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
public interface OrgOrgsService extends IService<OrgOrgs> {

	JsonBean getList(TblStaffUtil staff, OrgOrgsVo vo) throws Exception;

	JsonBean setCompanyInfo(TblStaffUtil staff, String pkOrg, BigDecimal orgId) throws Exception;

	JsonBean getTreeList(TblStaffUtil staff, OrgOrgsVo vo) throws Exception;


}
