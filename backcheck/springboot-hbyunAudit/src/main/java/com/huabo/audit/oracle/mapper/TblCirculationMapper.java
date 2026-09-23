package com.huabo.audit.oracle.mapper;


import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblCirculation;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-28
 */
public interface TblCirculationMapper extends tk.mybatis.mapper.common.Mapper<TblCirculation> {


	@InsertProvider(method="saveTblCirculationnew",type=TblCirculationMapperSqlConfig.class)
	@Options(useGeneratedKeys=true, keyProperty="cyid", keyColumn="CYID")
	TblCirculation saveTblCirculationnew(String type, String number, String name, String url, String loginUser,String buskey,String definitionId,String taskid) throws Exception;

	@Select("select * from TBL_CIRCULATION where TASKID = #{taskid} and rownum =1")
	TblCirculation selectCiculaInfoById(@Param("taskid")String taskid) throws Exception;

	@UpdateProvider(method="updateCirculationInfoById",type=TblCirculationMapperSqlConfig.class)
	void updateCirculationInfoById(TblCirculation circulation);

	@Select("select * from TBL_CIRCULATION where TASKID = #{taskId}")
    List<TblCirculation> findByTaskId(String taskId);

	@Select("select * from TBL_CIRCULATION where CYID = #{cyid}")
	TblCirculation findById(String cyid);

	@InsertProvider(method = "saveTblCirculation",type =TblCirculationMapperSqlConfig.class )
	@Options(useGeneratedKeys=true, keyProperty="cyid", keyColumn="CYID")
    void saveTblCirculation(TblCirculation c);

	@Select("select * from TBL_CIRCULATION where TASKID = #{contractId}")
    TblCirculation getOneBytaskid(String contractId);

	@Delete("DELETE FROM TBL_CIRCULATION WHERE CYID = #{cyid}")
	void deleteEntityById(@Param("cyid")BigDecimal cyid) throws Exception;
}
