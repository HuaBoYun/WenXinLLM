package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblSystemSheetTable;

import feign.Param;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemSheetTableMapper extends BaseMapper<TblSystemSheetTable> {

	@Insert("INSERT INTO TBL_SYSTEM_YMWORK(TABLEID, ORGID, YMWORKFROM, YMWORKNAME,VERSION) VALUES (#{tableId}, #{orgid}, #{workId}, #{workName},#{count})")
	void InsertSystemYmWork(BigDecimal tableId, String workId, BigDecimal orgid, String workName, Integer count) throws Exception;

	@Update("UPDATE TBL_SYSTEM_YMWORK SET YMWORKNAME = #{workName} WHERE TABLEID = #{tableId} AND ORGID = #{orgid} AND YMWORKFROM = #{workId}")
	void UpdateSystemYmWork(BigDecimal tableId, String workId, BigDecimal orgid, String workName);

	@SelectProvider(method="selectWorkFlowList",type=TblSystemSheetTableMapperSqlConfig.class)
	@Results({
		@Result(column="YMWORKFROM",property="ymWorkFrom"),
		@Result(column="YMWORKNAME",property="ymWorkName"),
		@Result(column="QYSTATS",property="qyStats"),
		@Result(column="VERSION",property="version"),
		@Result(column="RN",property="rowNo"),
		@Result(column="FLOWTEMPLATEID",property="flowtemplateId"),
	})
	IPage<TblSystemSheetTable> selectWorkFlowList(BigDecimal tableId, BigDecimal orgid, String workName, IPage<TblSystemSheetTable> page) throws Exception;


	@SelectProvider(method="selectSystemFlowList",type=TblSystemSheetTableMapperSqlConfig.class)
	@Results({
		@Result(column="TABLEID",property="tableId"),
		@Result(column="YMWORKNAME",property="ymWorkName"),
		@Result(column="TABLETYPE",property="tableType"),
		@Result(column="WORKCOUNT",property="workCount"),
	})
	List<TblSystemSheetTable> selectSystemFlowList(TblSystemSheetTable sheet, BigDecimal orgid) throws Exception;

	@SelectProvider(method="selectEntityById",type=TblSystemSheetTableMapperSqlConfig.class)
	@Results({
		@Result(column="TABLEID",property="tableId"),
		@Result(column="YMWORKNAME",property="ymWorkName"),
		@Result(column="TABLETYPE",property="tableType"),
	})
	TblSystemSheetTable selectEntityById(BigDecimal tableId) throws Exception;
	
	
	
	@InsertProvider(method="insertFormFlowTable",type=TblSystemSheetTableMapperSqlConfig.class)
	void insertFormFlowTable(BigDecimal fromId, String flowId, String ymflowid, BigDecimal staffId, String deptId, BigDecimal tableId, BigDecimal orgId) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{fromId} AND FLOWID = ${flowId}")
	Integer selectCountFormFlow(BigDecimal fromId, String flowId) throws Exception;

	@Update("UPDATE TBL_SYSTEM_FORMFLOW SET YMFORMID = ${ymflowid}  WHERE FORMID = #{fromId} AND FLOWID = #{flowId} ")
	void updateFormFlowTable(Integer fromId, String flowId, String ymflowid) throws Exception;

	@Select("SELECT * FROM TBL_SYSTEM_SHEETTABLE WHERE TABLEID IN (SELECT TABLEID FROM TBL_SYSTEM_YMWORK WHERE YMWORKFROM = #{flowId}) OR TABLEID IN (SELECT TABLEID FROM TBL_CONTRACTTYPE_ACTIVITY WHERE YMWORKFROM = #{flowId} )")
	TblSystemSheetTable selectSheetTableInfoByFlowId(String flowId, BigDecimal orgId) throws Exception;

	/**
	 * 5.0 版本：流程实例的 flowId 在撤回后重新提交时会变更，而 TBL_SYSTEM_YMWORK / TBL_CONTRACTTYPE_ACTIVITY
	 * 里登记的 YMWORKFROM 可能仍是旧 flowId（5.0 版本以 FLOWTEMPLATEID 为稳定主键登记）。
	 * 故当用 flowId 查不到 sheet 时，用稳定不变的 FLOWTEMPLATEID（流程模板主键）二次查询补全。
	 */
	@Select("SELECT * FROM TBL_SYSTEM_SHEETTABLE WHERE TABLEID IN (SELECT TABLEID FROM TBL_SYSTEM_YMWORK WHERE FLOWTEMPLATEID = #{templateId}) OR TABLEID IN (SELECT TABLEID FROM TBL_CONTRACTTYPE_ACTIVITY WHERE FLOWTEMPLATEID = #{templateId} )")
	TblSystemSheetTable selectSheetTableInfoByTemplateId(String templateId, BigDecimal orgId) throws Exception;

	/**
	 * 5.0 版本：审批接口拿到的是流程平台“流程实例”flowId（每次撤回重提交都会变），本地映射表查不到。
	 * 而 TBL_SYSTEM_FORMFLOW 里按合同 FORMID 存的 FLOWID 始终是“定义 flowId”（能匹配 YMWORKFROM）。
	 * 故用合同 formId 查回该合同最近一次提交登记的“定义 flowId”，作为查 sheet 的稳定兜底键，不修改任何数据。
	 */
	@Select("SELECT FLOWID FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{formId} AND SUBTIME = (SELECT MAX(SUBTIME) FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{formId})")
	String selectDefFlowIdByFormId(@Param("formId") BigDecimal formId, @Param("orgId") BigDecimal orgId) throws Exception;

	@Select("SELECT * FROM TBL_SYSTEM_SHEETTABLE WHERE TABLEID IN (SELECT TABLEID FROM TBL_CONTRACTTYPE_ACTIVITY WHERE YMWORKFROM = #{flowId} AND TYPEID = #{typeId})")
	TblSystemSheetTable selectSheetTableInfoByContractTypeFlowId(@Param("typeId")BigDecimal typeId,@Param("flowId") String flowId, BigDecimal orgid);

	@Update("${sql}")
	void executeSql(@Param("sql")String sql) throws Exception;

	@Select("SELECT FORMID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{id}")
	BigDecimal selectFormIdByYmFormId(@Param("id")String id) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{id}")
	void deleteYmFormInfo(String id) throws Exception;

	@Select("SELECT MODULETYPE FROM TBL_SYSTEM_RIGHT WHERE ID IN (SELECT RIGHTID FROM TBL_SYSTEM_ORG_RIGHT WHERE ORGID = #{orgid}) GROUP BY MODULETYPE")
	List<String> selectTableTypeNameList(BigDecimal orgid) throws Exception;

	
	@Update("UPDATE TBL_SYSTEM_YMWORK SET QYSTATS = 1 WHERE TABLEID = #{tableId} AND YMWORKFROM = #{ymWorkForm} AND ORGID = #{orgid}")
	void startYmWorkFormInfo(BigDecimal tableId, String ymWorkForm, BigDecimal orgid, Integer qystatus);

	@Update("UPDATE TBL_SYSTEM_YMWORK SET QYSTATS = 0 WHERE TABLEID = #{tableId} AND ORGID = #{orgid}")
	void removeYmWorkFormInfo(Integer tableId, BigDecimal orgid);

	@Select("SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE QYSTATS = 1 AND TABLEID = #{tableId} AND ORGID = #{orgId}")
	String selectFlowIdQyByTableId(String tableId, BigDecimal orgId);
	
	@Select("SELECT USERID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{id}")
	BigDecimal selectFormIdByYmUserId(String id);
	
	@Select("SELECT REALNAME FROM TBL_STAFF WHERE STAFFID = #{userId}")
	String selectFormIdByYmUserName(BigDecimal userId);

	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{tableId} AND YMWORKFROM = #{workId} AND ORGID = #{orgid}")
	Integer selectTableYmFlowCount(BigDecimal tableId, String workId, BigDecimal orgid) throws Exception;

	@Select("${sql}")
	String executeSqlReturnString(@Param("sql")String sql) throws Exception;

	@Select("SELECT YMFORMID FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{formId} AND FLOWID = #{flowId}")
	String selectYmFormIdByFlowId(String formId, String flowId) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_FORMFLOW WHERE FLOWID = #{ymWorkId}")
	Integer selectWorkFlowFormCount(String ymWorkId) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{tableId} AND YMWORKFROM = #{ymWorkId} AND ORGID = #{orgid}")
	void deleteTableYmFlowInfo(BigDecimal tableId, String ymWorkId, BigDecimal orgid) throws Exception;

	/**
	 * 5.0版本：按 FLOWTEMPLATEID 删除流程记录（5.0版本 YMWORKFROM 为空，需用 FLOWTEMPLATEID 匹配）
	 */
	@Delete("DELETE FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{tableId} AND FLOWTEMPLATEID = #{flowtemplateId} AND ORGID = #{orgid}")
	void deleteTableYmFlowInfoByTemplateId(@Param("tableId") BigDecimal tableId, @Param("flowtemplateId") String flowtemplateId, @Param("orgid") BigDecimal orgid) throws Exception;

	@Select("SELECT FLOWID,YMFORMID FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{formId} AND ( FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE ORGID = #{orgid} AND TABLEID = #{tableId}) "
			+ " OR FLOWID IN (SELECT YMWORKFROM FROM TBL_CONTRACTTYPE_ACTIVITY WHERE ORGID = #{orgid} AND TABLEID = #{tableId}) ) "
			+ " AND SUBTIME = (SELECT MAX(SUBTIME) FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{formId}) ")
	@Results({
		@Result(column="FLOWID",property="flowId"),
		@Result(column="YMFORMID",property="ymWorkFrom"),
	})
	TblSystemSheetTable selectSheetTableInfoByFormId(String tableId, String formId, BigDecimal orgid) throws Exception;

	@Select("${orgSql}")
	String executeFindSqlReturnUnique(@Param("orgSql")String orgSql) throws Exception;

	@Select("SELECT * FROM TBL_SYSTEM_SHEETTABLE WHERE TABLEID = #{tableId}")
	TblSystemSheetTable selectByPK(BigDecimal tableId) throws Exception;

	@Update("UPDATE TBL_SYSTEM_YMWORK SET QYSTATS = 0 WHERE TABLEID = #{tableId} AND ORGID = #{orgid}")
	void removeYmWorkFormInfo(BigDecimal tableId, BigDecimal orgid);

	@Select("${findSql}")
	Integer executeFindSqlReturnInteger(@Param("findSql")String findSql) throws Exception;

	@Select("SELECT YMWORKNAME FROM TBL_SYSTEM_YMWORK WHERE TABLEID = #{tableId} AND ORGID = #{orgid} AND YMWORKFROM = #{ymWorkForm}")
	String selectYmWorkFlowName(@Param("tableId")BigDecimal tableId,@Param("ymWorkForm") String ymWorkForm,@Param("orgid") BigDecimal orgid);

	@SelectProvider(method="selectEntityByProcessId",type=TblSystemSheetTableMapperSqlConfig.class)
	TblSystemSheetTable selectEntityByProcessId(String processId) throws Exception;

	@Insert("INSERT INTO TBL_SYSTEM_YMWORK(TABLEID, ORGID, FLOWTEMPLATEID, YMWORKNAME,VERSION) VALUES (#{tableId}, #{orgid}, #{templateId}, #{workName},#{count})")
	void InsertSystemYmWorkTemplate(BigDecimal tableId, String templateId, BigDecimal orgid, String workName, Integer count);
	
	@Update("UPDATE TBL_SYSTEM_YMWORK SET YMWORKNAME = #{workName} WHERE TABLEID = #{tableId} AND ORGID = #{orgid} AND FLOWTEMPLATEID = #{templateId}")
	void UpdateSystemYmWorkTemplate(BigDecimal tableId, String templateId, BigDecimal orgid, String workName);

	@Update("UPDATE TBL_SYSTEM_YMWORK SET YMWORKFROM = #{flowId} WHERE TABLEID = #{tableId} AND ORGID = #{orgid} AND FLOWTEMPLATEID = #{id} ")
	void updateSystemYmFlowInfo(@Param("tableId")String tableId,@Param("id") String id,@Param("flowId") String flowId,@Param("orgid") BigDecimal orgid) throws Exception;

	@Insert("INSERT INTO TBL_SYSTEM_YMWORK(TABLEID, ORGID, YMWORKFROM, YMWORKNAME, VERSION, FLOWTYPE) VALUES (#{tableId}, #{orgid}, #{workId}, #{workName}, #{count}, #{flowType})")
	void InsertSystemYmWorkWithType(@Param("tableId") BigDecimal tableId, @Param("workId") String workId, @Param("orgid") BigDecimal orgid, @Param("workName") String workName, @Param("count") Integer count, @Param("flowType") Integer flowType) throws Exception;

	@Update("UPDATE TBL_SYSTEM_YMWORK SET YMWORKNAME = #{workName}, FLOWTYPE = #{flowType} WHERE TABLEID = #{tableId} AND ORGID = #{orgid} AND YMWORKFROM = #{workId}")
	void UpdateSystemYmWorkWithType(@Param("tableId") BigDecimal tableId, @Param("workId") String workId, @Param("orgid") BigDecimal orgid, @Param("workName") String workName, @Param("flowType") Integer flowType);

	@Insert("INSERT INTO TBL_SYSTEM_YMWORK(TABLEID, ORGID, FLOWTEMPLATEID, YMWORKNAME, VERSION, FLOWTYPE) VALUES (#{tableId}, #{orgid}, #{templateId}, #{workName}, #{count}, #{flowType})")
	void InsertSystemYmWorkTemplateWithType(@Param("tableId") BigDecimal tableId, @Param("templateId") String templateId, @Param("orgid") BigDecimal orgid, @Param("workName") String workName, @Param("count") Integer count, @Param("flowType") Integer flowType);

	@Update("UPDATE TBL_SYSTEM_YMWORK SET YMWORKNAME = #{workName}, FLOWTYPE = #{flowType} WHERE TABLEID = #{tableId} AND ORGID = #{orgid} AND FLOWTEMPLATEID = #{templateId}")
	void UpdateSystemYmWorkTemplateWithType(@Param("tableId") BigDecimal tableId, @Param("templateId") String templateId, @Param("orgid") BigDecimal orgid, @Param("workName") String workName, @Param("flowType") Integer flowType);

}
