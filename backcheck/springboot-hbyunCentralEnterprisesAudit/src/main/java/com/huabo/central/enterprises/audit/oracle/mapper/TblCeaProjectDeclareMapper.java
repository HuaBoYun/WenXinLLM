package com.huabo.central.enterprises.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclare;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareSortQueryParam;

import tk.mybatis.mapper.common.Mapper;

public interface TblCeaProjectDeclareMapper extends Mapper<TblCeaProjectDeclare> {

	/**
	 * 列表
	 * @param param
	 * @return
	 */
	List<TblCeaProjectDeclare> getList(@Param("param") TblCeaProjectDeclareQueryParam param);

	/**
	 * 详情
	 * @param id
	 * @return
	 */
	TblCeaProjectDeclare findById(@Param("id") Long id);

	/**
	 * 项目评优排序 列表查询
	 * @param param
	 * @return
	 */
	List<TblCeaProjectDeclare> findTblCeaProjectDeclareSortList(@Param("param") TblCeaProjectDeclareSortQueryParam param);

	/**
	 * 更新分组为空
	 * @param groupId
	 */
	@Update("update TBL_CEA_PROJECT_DECLARE set GROUPID = null where GROUPID = #{groupId}")
	void updateNullGroupList(@Param("groupId") Long groupId);

	/**
	 * 分组置空
	 * @param id
	 */
	@Update("update TBL_CEA_PROJECT_DECLARE set GROUPID = null,REVIEWTEAMJSON = '[]'where ID = #{id}")
	void updateNull(@Param("id") Long id);
	
	/**
	 * 查询项目的核减金额总数
	 */
	@Select("select sum(HJMONEY) from TBL_YQNS_RESULT where projectid = #{implementationPlanId}")
	Integer selectSumHjjeByProjectid(@Param("implementationPlanId") Long implementationPlanId);
	
}