package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.vo.TblTesttaskProblemFindVo;

public interface TblTesttaskProblemFindMapper{

	@SelectProvider(method = "selectPageInfoByIssues" , type = TblTesttaskProblemFindMapperSqlConfig.class)
	List<TblTesttaskProblemFind> selectPageInfoByIssues(TblTesttaskProblemFind problem);

	@SelectProvider(method="selectListByPageInfo",type=TblTesttaskProblemFindMapperSqlConfig.class)
	List<TblTesttaskProblemFind> selectListByPageInfo(com.huabo.audit.util.PageInfo<TblTesttaskProblemFind> pageInfo,TblTesttaskProblemFindVo tblTesttaskProblemFindVo) throws Exception;
	
	 @SelectProvider(method="selectCountByPageInfo",type=TblTesttaskProblemFindMapperSqlConfig.class)
	 Integer selectCountByPageInfo(com.huabo.audit.util.PageInfo<TblTesttaskProblemFind> pageInfo,TblTesttaskProblemFindVo tblTesttaskProblemFindVo) throws Exception;
	 
    @Select("${sql}")
	List<TblTesttaskProblemFind> getListBySql(@Param("sql") String sql);
	
}
