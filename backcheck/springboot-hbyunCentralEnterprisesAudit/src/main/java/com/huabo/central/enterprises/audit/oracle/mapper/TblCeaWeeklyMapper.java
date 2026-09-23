package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeekly;
import com.huabo.central.enterprises.audit.vo.param.TblCeaWeeklyQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaWeeklyMapper extends Mapper<TblCeaWeekly> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaWeekly> getList(@Param("param") TblCeaWeeklyQueryParam param);

}