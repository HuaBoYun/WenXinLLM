package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaPeopleLeaveOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaPeopleLeaveQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaPeopleLeaveOracleMapper extends Mapper<TblCeaPeopleLeaveOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaPeopleLeaveOracle> getList(@Param("param") TblCeaPeopleLeaveQueryParam param);

	/**
	 * 人员请假台账
	 * @param param
	 * @return
	 */
	List<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveAllList(@Param("param") TblCeaPeopleLeaveQueryParam param);
}