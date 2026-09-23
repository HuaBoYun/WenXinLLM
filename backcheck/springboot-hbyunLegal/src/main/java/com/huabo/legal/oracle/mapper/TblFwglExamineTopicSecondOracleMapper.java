package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblFwglExamineTopicSecondOracle;
import com.huabo.legal.vo.param.TblFwglExamineTopicSecondQueryParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblFwglExamineTopicSecondOracleMapper extends Mapper<TblFwglExamineTopicSecondOracle> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblFwglExamineTopicSecondOracle> getList(@Param("param") TblFwglExamineTopicSecondQueryParam param);
}