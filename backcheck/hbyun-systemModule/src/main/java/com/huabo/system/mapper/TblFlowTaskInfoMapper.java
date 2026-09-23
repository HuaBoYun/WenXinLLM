package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFlowTaskInfo;
import com.huabo.system.mappersql.TblFlowTaskInfoMapperSqlConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblFlowTaskInfoMapper extends BaseMapper<TblFlowTaskInfo> {

	@InsertProvider(type=TblFlowTaskInfoMapperSqlConfig.class,method="saveEntity")
	@Options(useGeneratedKeys=true, keyProperty="taksId", keyColumn="TAKSID")
	void saveEntity(TblFlowTaskInfo taskInfo) throws Exception;

	@Select("SELECT * FROM TBL_FLOW_TASKINFO WHERE TAKSID = (SELECT MAX(TAKSID) FROM TBL_FLOW_TASKINFO WHERE FROMID = #{formId} AND FLOWID = #{flowId} AND FLOWTASKID = #{id} AND MODULETYPE = #{moduleType} AND NEXTSTAFFID = #{staffId} )")
	TblFlowTaskInfo selectPreTaskInfo(BigDecimal formId, String flowId, String id, String moduleType, BigDecimal staffId) throws Exception;
	
	@Select("SELECT CURRENTSTAFFID FROM TBL_FLOW_TASKINFO WHERE TAKSID = (SELECT MIN(TAKSID) FROM TBL_FLOW_TASKINFO WHERE FROMID = #{formId} AND FLOWID = #{flowId} AND PROCESSID = #{processId} AND FLOWTASKID = #{id} AND MODULETYPE = #{tableType} ) ")
	Integer selectCurrentStaffId(Integer formId, String processId, String flowId, String id, String tableType) throws Exception;

	@Select("SELECT * FROM TBL_FLOW_TASKINFO WHERE FROMID = #{formId} AND FLOWTASKID = #{id} AND MODULETYPE = #{tableType} ORDER BY CREATETIME DESC ")
	List<TblFlowTaskInfo> selectPreTaskInfoByTaskId(BigDecimal formId, String id, String tableType) throws Exception;

	@Delete("Delete FROM TBL_FLOW_TASKINFO WHERE FROMID = #{formId} AND FLOWTASKID = #{id} AND MODULETYPE = #{tableType} ")
	void deleteTaskInfoByTaskId(BigDecimal formId, String id, String tableType) throws Exception;

	@Update("UPDATE TBL_FLOW_TASKINFO SET TASKSTATUS = 1 WHERE TAKSID = #{taksId}")
	void updateTaskStatus(TblFlowTaskInfo preTaskInfo) throws Exception;

	@Update("UPDATE TBL_FLOW_TASKINFO SET TASKSTATUS = 1,OPERATION = #{operate} WHERE THISSTEPID = #{thisStepId} AND PROCESSID = #{processId} AND TASKGROUPID = #{taskGroupId} AND TAKSID != #{taksId} AND TASKSTATUS = 0")
	void updateTaskStatusGroupId(Integer taskGroupId, BigDecimal taksId, String thisStepId, String operate,
			String processId) throws Exception;

	@Update("UPDATE TBL_FLOW_TASKINFO SET TASKSTATUS = 3,OPERATION = '撤回' WHERE PROCESSID = #{processId} AND FLOWID = #{flowId} AND MODULETYPE = #{tableType} AND TASKSTATUS = 0")
	void updateTaskStatusAlreadyRecall(String processId, String flowId, String tableType) throws Exception;
	
	@Select("SELECT COUNT(0) FROM TBL_FLOW_TASKINFO WHERE TASKSTATUS = #{taskStatus} AND TASKGROUPID = #{taskGroupId} AND THISSTEPID = #{stepId} AND FLOWID = #{flowId} AND PROCESSID = #{flowTaskId}")
	Integer selectSameLevelInfoCount(String stepId, Integer taskStatus, Integer taskGroupId, String flowId, String flowTaskId) throws Exception;

	@Select("SELECT * FROM TBL_FLOW_TASKINFO WHERE THISSTEPID = #{thisStepId} AND TASKGROUPID = #{taskGroupId} AND TAKSID != #{taksId} AND TASKSTATUS = 0 AND PROCESSID = #{processId}")
	List<TblFlowTaskInfo> selectNoDealTaskInfo(String thisStepId, Integer taskGroupId, BigDecimal taksId, String processId);

	@SelectProvider(type=TblFlowTaskInfoMapperSqlConfig.class,method="selectApprovalMemo")
	@Results({
		@Result(column="REALNAME",property="currentName"),
		@Result(column="COMMONT",property="commont"),
	})
	List<TblFlowTaskInfo> selectApprovalMemo(String processId, String condition) throws Exception;

	@Select("SELECT MAX(TASKGROUPID) FROM TBL_FLOW_TASKINFO WHERE PROCESSID = #{processId} AND FLOWID = #{flowId} AND MODULETYPE = #{tableType}")
	Integer selectMaxGrouIdByFlowIdProcessId(String processId, String flowId, String tableType);

	@Select("SELECT * FROM TBL_FLOW_TASKINFO WHERE PROCESSID = #{processId} AND FLOWID = #{flowId} AND MODULETYPE = #{tableType} AND TASKSTATUS = 0")
	List<TblFlowTaskInfo> selectPreTaskInfoListByProcessIdFlowId(String processId, String flowId, String tableType);

	@Select("SELECT * FROM TBL_FLOW_TASKINFO WHERE FLOWTASKID = #{id} AND TASKSTATUS = 0 ")
	List<TblFlowTaskInfo> selectUntreatedInfoList(@Param("id")String id) throws Exception;

	@Select("SELECT * FROM TBL_FLOW_TASKINFO WHERE FLOWTASKID = #{id}")
	List<TblFlowTaskInfo> selectAllTaskInfoListByTaskId(@Param("id")String id);

	@Update("UPDATE TBL_FLOW_TASKINFO SET TASKSTATUS = 1 WHERE FLOWTASKID = #{id} AND THISSTEPID != #{step}")
	void updateTaskStatusTodeal(@Param("id")String id,@Param("step") String step) throws Exception;

	@Delete("DELETE FROM TBL_FLOW_TASKINFO WHERE FLOWTASKID = #{ymFromId}")
	void deleteInfosByProcessId(@Param("ymFromId")String ymFromId);

	@Select("SELECT * FROM TBL_FLOW_TASKINFO WHERE OPERATORID = #{operatorId}")
	TblFlowTaskInfo selectPreTaskInfoByOperatorId(@Param("operatorId")String operatorId);

}
