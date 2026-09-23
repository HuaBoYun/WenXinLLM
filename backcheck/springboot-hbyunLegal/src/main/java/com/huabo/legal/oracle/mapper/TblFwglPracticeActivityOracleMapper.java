package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblFwglPracticeActivityOracle;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblFwglPracticeActivityOracleMapper extends Mapper<TblFwglPracticeActivityOracle> {

	List<TblFwglPracticeActivityOracle> findArticleList(@Param("staffId") Long staffId, @Param("type") Integer type);
}
