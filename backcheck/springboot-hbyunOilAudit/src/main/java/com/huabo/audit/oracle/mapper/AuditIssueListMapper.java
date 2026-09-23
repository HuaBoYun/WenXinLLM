package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsIssueListEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuditIssueListMapper extends BaseMapper<TblYqnsIssueListEntity> {

    @Select("SELECT ISSUE_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

    @Update("UPDATE TBL_YQNS_ISSUE_LIST SET RECTPERNAME = NULL , RECTPERSON = NULL WHERE ID = #{issuesId}")
	void setPersonIsNull(BigDecimal issuesId) throws Exception;

    @Select("SELECT DISTINCT RECTPERNAME FROM TBL_YQNS_ISSUE_LIST WHERE PROJECTID = #{projectId} AND RECTPERNAME IS NOT NULL ")
	List<String> selectAllRectPerNameByProjectId(BigDecimal projectId) throws Exception;

}
