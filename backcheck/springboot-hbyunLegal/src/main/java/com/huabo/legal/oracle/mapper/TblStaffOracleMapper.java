package com.huabo.legal.oracle.mapper;

import com.huabo.legal.oracle.entity.TblOrganization;
import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.vo.param.HeadquartersLegalQueryParam;
import com.huabo.legal.vo.result.StaffResult;
import com.huabo.legal.vo.result.UserInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblStaffOracleMapper extends Mapper<TblStaffOracle> {

	/**
	 * 根据集团id 查询名称
	 * @param orgid
	 * @return
	 */
	StaffResult findBelongGroupIdUserInfo(@Param("orgid") Long orgid);

	/**
	 * 根据工作单位id 查询信息
	 * @param orgid
	 * @return
	 */
	StaffResult findWorkUnitIdUserInfo(@Param("orgid") Long orgid);

	/**
	 * 根据用户id 查询信息
	 * @param staffId
	 * @return
	 */
	StaffResult findUserInfo(@Param("staffId") Long staffId);

	/**
	 * 根据创建人ID 查询信息
	 * @param staffId
	 * @return
	 */
	StaffResult findCreatorUserInfo(@Param("staffId") Long staffId);

	/**
	 * 批量获取用户信息
	 * @param staffId
	 * @return
	 */
	List<UserInfo> findCreatorUserInfos(@Param("staffId") String staffId);

	/**
	 * 批量获取用户工作单位信息
	 * @param orgid
	 * @return
	 */
	List<UserInfo> findWorkUnitIdUserInfos(@Param("orgid") String orgid);

	/**
	 * 批量获取用户集团信息
	 * @param orgid
	 * @return
	 */
	List<UserInfo> findBelongGroupIdUserInfos(@Param("orgid") String orgid);

	/**
	 * 批量获取用户集团信息 Orgmeno
	 * @param orgid
	 * @return
	 */
	List<UserInfo> findBelongGroupIdUserInfoOrgmeno(@Param("orgid") String orgid);

	/**
	 * 获取类型人员
	 * @param staffId
	 * @param rid
	 * @return
	 */
	StaffResult findUserInfoExam(@Param("staffId") Long staffId, @Param("rid") Integer rid);

	/**
	 * 根据集团与类型获取角色ID
	 * @param orgid
	 * @param type
	 * @return
	 */
	Integer findLikeUser(@Param("orgid") Long orgid, @Param("type") String type);

	/**
	 * 查询该用户是否是总部法务人员
	 * @param param
	 * @return
	 */
	Integer isHeadquartersLegal(@Param("param") HeadquartersLegalQueryParam param);


	TblStaffOracle getUserInfoForId(@Param("staffId") Long staffId);

	/**
	 * 所有的公司
	 * @return
	 */
	List<TblOrganization> findAllList();

	/**
	 * 当前公司下一级的子公司
	 * @param orgid
	 * @return
	 */
	List<Long> findTblOrganizationFatherorgidList(@Param("orgid") Long orgid);
}
