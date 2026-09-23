package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayExp;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaCancelHolidayExpMapper extends Mapper<TblCeaCancelHolidayExp> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaCancelHolidayExp> getList(@Param("param") TblCeaCancelHolidayQueryParam param);
}