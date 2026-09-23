package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpInventory;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpInventoryQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaIpInventoryMapper extends Mapper<TblCeaIpInventory> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaIpInventory> getList(@Param("param") TblCeaIpInventoryQueryParam param);
}