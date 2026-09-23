package com.huabo.finance.mapper;

import com.huabo.finance.entity.TblRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色表管理
rid:主键ID,自动增长；
rname:角色名称；
rdesc:角色描述；
rstatus:角色状态，是否启用 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-04-11
 */
public interface TblRoleMapper extends BaseMapper<TblRole> {

}
