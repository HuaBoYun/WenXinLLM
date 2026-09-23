package com.huabo.central.enterprises.audit.oracle.mapper;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectQuality;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQualityQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQueryVoParam;
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectVoResult;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblCeaProjectQualityMapper extends Mapper<TblCeaProjectQuality> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaProjectQuality> getList(@Param("param") TblCeaProjectQualityQueryParam param);

	/**
	 * 项目管理-实施方案 列表查询
	 * @param param
	 * @return
	 */
	List<TblCeaProjectVoResult> getTblCeaProjectList(@Param("param") TblCeaProjectQueryVoParam param);

	/**
	 * 项目管理-实施方案 列表查询
	 * @return
	 */
	List<TblCeaProjectVoResult> getTblCeaProjectVoList(@Param("param") TblCeaProjectQueryVoParam param);

	/**
	 * 项目管理-实施方案-小组列表
	 * @param id
	 * @return
	 */
	List<TblCeaProjectVoResult> getTblCeaProjectTeamList(@Param("id")Long id);

	/**
	 * 根据人查询 项目 人员是项目经理、主审、助审、小组组长、副组长、组员
	 * @param assessId
	 * @return
	 */
	List<Long> getIds(@Param("assessId") Long assessId);

	/**
	 * 获取 项目管理-实施方案 名称
	 * @param projectId
	 * @return
	 */
	@Select("select project_name from TBL_YQNS_IMPLEMENT_PLAN where id =#{projectId}")
	String findTblCeaProjectName(@Param("projectId") Long projectId);
}