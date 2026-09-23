package com.huabo.system.mapper;


import com.huabo.system.entity.TblStaffOracle;
import com.huabo.system.vo.result.StaffResult;
import com.huabo.system.vo.result.UserInfo;
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
	StaffResult findUserInfoExam(@Param("staffId") Long staffId, @Param("rid") Long rid);

	/**
	 * 根据集团与类型获取角色ID
	 * @param orgid
	 * @param type
	 * @return
	 */
	Long findLikeUser(@Param("orgid") Long orgid, @Param("type") String type);

	TblStaffOracle getUserInfoForId(@Param("staffId") Long staffId);

	List<TblStaffOracle> getUserInfoForIdMap(@Param("staffIds") List<Long> staffIds);

	/**
	 * 根据用户名称与公司ID 查询用户信息
	 * @param staffName
	 * @param orgid
	 * @return
	 */
	TblStaffOracle findUserInfoForName(@Param("staffName") String staffName, @Param("orgid") Long orgid);

	/**
	 * 根据部门名称与部门ID 查询用户信息
	 * @param workUnitName
	 * @param orgid
	 * @return
	 */
	StaffResult findUserInfoForWorkUnitName(@Param("workUnitName") String workUnitName, @Param("orgid") Long orgid);
}
