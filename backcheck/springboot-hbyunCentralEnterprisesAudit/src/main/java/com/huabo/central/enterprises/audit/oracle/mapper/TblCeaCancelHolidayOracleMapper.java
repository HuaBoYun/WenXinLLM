package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaCancelHolidayOracleMapper extends Mapper<TblCeaCancelHolidayOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaCancelHolidayOracle> getList(@Param("param") TblCeaCancelHolidayQueryParam param);
}