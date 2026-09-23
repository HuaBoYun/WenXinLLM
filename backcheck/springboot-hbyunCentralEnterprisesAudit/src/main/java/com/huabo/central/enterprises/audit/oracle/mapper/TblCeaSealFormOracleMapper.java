package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSealFormOracle;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaSealFormOracleMapper extends Mapper<TblCeaSealFormOracle> {

	/**
	 * sort最大值
	 * @return
	 */
	@Select("SELECT MAX(SORT) FROM TBL_CEA_SEAL_FORM")
	Integer maxSort();

	/**
	 * 上移最近一条数据
	 * @param sort
	 * @return
	 */
	@Select("SELECT id,sort from TBL_CEA_SEAL_FORM where sort<#{sort}")
	List<TblCeaSealFormOracle> getMoveUp(@Param("sort") Integer sort);

	/**
	 * 下移最近一条数据
	 * @param sort
	 * @return
	 */
	@Select("SELECT id,sort from TBL_CEA_SEAL_FORM where sort>#{sort}")
	List<TblCeaSealFormOracle> moveDown(@Param("sort") Integer sort);
}