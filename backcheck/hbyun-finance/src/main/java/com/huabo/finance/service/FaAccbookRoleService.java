package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.FaAccbookRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 账簿角色授权 服务类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
public interface FaAccbookRoleService extends IService<FaAccbookRole> {

	JsonBean grant(TblStaffUtil staff, String pkAccbookinfo, String[] roleIds) throws Exception;

	JsonBean cancel(TblStaffUtil staff, String pkAccbookinfo, String[] roleIds) throws Exception;

}
