package com.huabo.finance.service;

import com.huabo.finance.entity.TblRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 角色表管理
rid:主键ID,自动增长；
rname:角色名称；
rdesc:角色描述；
rstatus:角色状态，是否启用 服务类
 * </p>
 *
 * @author L
 * @since 2025-04-11
 */
public interface TblRoleService extends IService<TblRole> {

	JsonBean getRoleListByBook(TblStaffUtil staff, String pkAccbookinfo, String roleName, Integer pageNumber, Integer pageSize) throws Exception;

}
