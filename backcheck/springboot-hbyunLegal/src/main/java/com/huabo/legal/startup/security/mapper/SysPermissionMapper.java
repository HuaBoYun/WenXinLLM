package com.huabo.legal.startup.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.legal.startup.security.domain.SysPermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限表DAO接口
 *
 * @author zhuhuix
 * @date 2021-10-26
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
}
