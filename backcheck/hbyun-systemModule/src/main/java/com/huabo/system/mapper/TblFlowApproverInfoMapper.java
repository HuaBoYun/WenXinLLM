package com.huabo.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFlowApproverInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblFlowApproverInfoMapper extends BaseMapper<TblFlowApproverInfo> {

	@Select("SELECT T1.* FROM (SELECT APPROVERID,APPSTATUS,FLOWID,PROCESSID,THISSTEPID,STAFFID,APPORDER,ROWNUM FROM TBL_FLOW_APPROVER_INFO WHERE FLOWID = #{flowId} AND PROCESSID = #{id} AND THISSTEPID = #{thisStepId} AND APPSTATUS = 0 ORDER BY APPORDER ASC,CREATETIME ASC ) T1 WHERE ROWNUM <= 2")
	List<TblFlowApproverInfo> selectNextApprovalStaffList(String flowId, String id, String thisStepId) throws Exception;

	@Insert("INSERT INTO TBL_FLOW_APPROVER_INFO(APPROVERID,APPSTATUS,FLOWID,PROCESSID,THISSTEPID,STAFFID,APPORDER,CREATETIME)"
			+ " VALUES (#{approverId},#{appStatus},#{flowId},#{processId},#{thisStepId},#{staffId},#{apporDer},SYSDATE)")
	void insertEntity(TblFlowApproverInfo insertApp) throws Exception;

	@Update("UPDATE TBL_FLOW_APPROVER_INFO SET APPSTATUS = 1 , DEALTIME = SYSDATE WHERE APPROVERID = #{approverId}")
	void updateEntity(TblFlowApproverInfo currentApp) throws Exception;
	
	@Update("UPDATE TBL_FLOW_APPROVER_INFO SET APPSTATUS = 2 WHERE FLOWID = #{flowId} AND PROCESSID = #{id} AND THISSTEPID = #{oldStepId} AND APPSTATUS = 0")
	void stopEntity(String oldStepId, String id, String flowId);

	@Delete("DELETE FROM TBL_FLOW_APPROVER_INFO WHERE PROCESSID = #{ymFromId}")
	void deleteInfoByProcessId(@Param("ymFromId")String ymFromId) throws Exception;
}
