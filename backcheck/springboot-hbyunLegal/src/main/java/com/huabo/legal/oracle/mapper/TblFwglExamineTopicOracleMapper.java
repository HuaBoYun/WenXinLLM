package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblFwglExamineTopicOracle;
import com.huabo.legal.vo.param.TblFwglExamineTopicQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblFwglExamineTopicOracleMapper extends Mapper<TblFwglExamineTopicOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblFwglExamineTopicOracle> getList(@Param("param") TblFwglExamineTopicQueryParam param);
}