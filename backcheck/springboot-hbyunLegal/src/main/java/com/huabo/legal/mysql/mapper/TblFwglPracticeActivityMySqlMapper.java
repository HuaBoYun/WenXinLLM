package com.huabo.legal.mysql.mapper;

import com.huabo.legal.mysql.entity.TblFwglPracticeActivityMySql;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblFwglPracticeActivityMySqlMapper extends Mapper<TblFwglPracticeActivityMySql> {

	/**
	 * 根据人员ID,类型 查询执业活动列表
	 * @param staffId
	 * @param type
	 */
	List<TblFwglPracticeActivityMySql> findArticleList(@Param("staffId") Integer staffId, @Param("type") Integer type);
}