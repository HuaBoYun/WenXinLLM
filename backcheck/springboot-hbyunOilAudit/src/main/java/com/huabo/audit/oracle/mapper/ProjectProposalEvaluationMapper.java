package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.ProjectProposalEvaluationEntity;
import com.huabo.audit.oracle.entity.TblStaff;

import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author CJ
 * @ClassName ProjectProposalEvaluationMapper
 * @Description
 * @DATE 2024/5/29
 */
public interface ProjectProposalEvaluationMapper extends  Mapper<ProjectProposalEvaluationEntity> {

	List<ProjectProposalEvaluationEntity> findList(@Param("queryParam")ProjectProposalEvaluationEntity queryParam);

	ProjectProposalEvaluationEntity selectById(@Param("id")BigDecimal id);

	List<ProjectProposalEvaluationEntity> getLxZxsjList(@Param("queryParam")ProjectProposalEvaluationEntity queryParam);
	
	List<ProjectProposalEvaluationEntity> findByIds(@Param("ids")List<String> ids);

	@SelectProvider(type = ProjectProposalEvaluationMapperSqlConfig.class,method = "selectLxZxsjHzChooseList")
	List<ProjectProposalEvaluationEntity> selectLxZxsjHzChooseList(ProjectProposalEvaluationEntity projectProposalEvaluationEntity);

	@Update("UPDATE TBL_YQNS_PROJECT_EVALUATION SET ISSHOWLIST = 1 WHERE ID IN (${idStrs}) ")
	void updateShowListByIds(@Param("idStrs")String idStrs) throws Exception;

	@SelectProvider(type = ProjectProposalEvaluationMapperSqlConfig.class,method = "selectLxZxsjHzDetailList")
	List<ProjectProposalEvaluationEntity> selectLxZxsjHzDetailList(ProjectProposalEvaluationEntity projectProposalEvaluationEntity);

	@SelectProvider(type = ProjectProposalEvaluationMapperSqlConfig.class,method = "findListByAnalysis")
	List<ProjectProposalEvaluationEntity> findListByAnalysis(Integer xmnd, String projectType, String projectName);

}
