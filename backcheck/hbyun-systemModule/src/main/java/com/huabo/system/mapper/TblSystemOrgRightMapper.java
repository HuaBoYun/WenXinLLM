package com.huabo.system.mapper;

import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemOrgRight;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-05-22
 */
public interface TblSystemOrgRightMapper extends BaseMapper<TblSystemOrgRight> {

	@UpdateProvider(method="updateSystemSettingRight",type=TblSystemRightMapperSqlConfig.class)
	void updateSystemSettingRight(TblSystemOrgRight orgRight) throws Exception;

}
