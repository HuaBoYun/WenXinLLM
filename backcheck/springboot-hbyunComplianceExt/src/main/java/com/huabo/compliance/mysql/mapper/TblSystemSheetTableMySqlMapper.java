package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.entity.TblSystemSheetTable;
import com.huabo.compliance.mysql.entity.TblSystemSheetTableMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemSheetTableMySqlMapper extends BaseMapper<TblSystemSheetTableMySql> {

	@Insert("INSERT INTO TBL_SYSTEM_YMWORK(TABLEID, ORGID, YMWORKFROM, YMWORKNAME) VALUES (#{tableId}, #{orgid}, #{workId}, #{workName})")
	void InsertSystemYmWork(Integer tableId, String workId, BigDecimal orgid, String workName) throws Exception;

	@Update("UPDATE TBL_SYSTEM_YMWORK SET YMWORKNAME = #{workName} WHERE TABLEID = #{tableId} AND ORGID = #{orgid} AND YMWORKFROM = #{workId}")
	void UpdateSystemYmWork(Integer tableId, String workId, BigDecimal orgid, String workName);

	@SelectProvider(method="selectWorkFlowList",type=TblSystemSheetTableMySqlMapperSqlConfig.class)
	@Results({
		@Result(column="YMWORKFROM",property="ymWorkFrom"),
		@Result(column="YMWORKNAME",property="ymWorkName"),
	})
	List<TblSystemSheetTableMySql> selectWorkFlowList(Integer tableId, BigDecimal orgid, String workName) throws Exception;

	
	@SelectProvider(method="selectSystemFlowList",type=TblSystemSheetTableMySqlMapperSqlConfig.class)
	@Results({
		@Result(column="TABLEID",property="tableId"),
		@Result(column="YMWORKNAME",property="ymWorkName"),
		@Result(column="TABLETYPE",property="tableType"),
		@Result(column="WORKCOUNT",property="workCount"),
	})
	List<TblSystemSheetTableMySql> selectSystemFlowList(TblSystemSheetTable sheet) throws Exception;

	@Insert("INSERT INTO TBL_SYSTEM_FORMFLOW(FORMID,FLOWID,YMFORMID) VALUES (#{fromId}, #{flowId},#{ymflowid})")
	void insertFormFlowTable(Integer fromId, String flowId, String ymflowid) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{fromId} AND FLOWID = ${flowId} AND YMFORMID = ${ymflowid}")
	Integer selectCountFormFlow(Integer fromId, String flowId, String ymflowid) throws Exception;

	@Update("UPDATE TBL_SYSTEM_FORMFLOW SET FLOWID = #{flowId} , YMFORMID = ${ymflowid} WHERE FORMID = #{fromId}")
	void updateFormFlowTable(Integer fromId, String flowId, String ymflowid) throws Exception;

	@Select("SELECT * FROM TBL_SYSTEM_SHEETTABLE WHERE TABLEID = (SELECT TABLEID FROM TBL_SYSTEM_YMWORK WHERE YMWORKFROM = #{flowId})")
	TblSystemSheetTableMySql selectSheetTableInfoByFlowId(String flowId) throws Exception;

	@Update("${sql}")
	void executeSql(@Param("sql")String sql) throws Exception;

	@Select("SELECT FORMID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{id}")
	Integer selectFormIdByYmFormId(String id) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{id}")
	void deleteYmFormInfo(String id) throws Exception;

	@Select("SELECT MODULETYPE FROM TBL_SYSTEM_RIGHT WHERE ID IN (SELECT RIGHTID FROM TBL_SYSTEM_ORG_RIGHT WHERE ORGID = #{orgid}) GROUP BY MODULETYPE")
	List<String> selectTableTypeNameList(BigDecimal orgid) throws Exception;

}
