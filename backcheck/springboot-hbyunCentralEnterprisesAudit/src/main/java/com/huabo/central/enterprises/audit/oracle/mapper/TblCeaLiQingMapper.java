package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaLiQing;
import com.huabo.central.enterprises.audit.vo.param.TblCeaLiQingQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaLiQingMapper extends Mapper<TblCeaLiQing> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaLiQing> getList(@Param("param") TblCeaLiQingQueryParam param);
}