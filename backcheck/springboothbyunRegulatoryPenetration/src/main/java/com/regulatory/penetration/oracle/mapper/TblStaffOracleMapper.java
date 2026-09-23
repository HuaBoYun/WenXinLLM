package com.regulatory.penetration.oracle.mapper;

import com.regulatory.penetration.oracle.entity.TblOrganization;
import com.regulatory.penetration.oracle.entity.TblStaffOracle;
import com.regulatory.penetration.vo.param.UserAllQueryParam;
import com.regulatory.penetration.vo.result.StaffResult;
import com.regulatory.penetration.vo.result.UserAllResult;
import com.regulatory.penetration.vo.result.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
	 * 批量获取用户工作单位/部门 信息
	 * @param orgid
	 * @return
	 */
	List<UserInfo> findIdUserInfos(@Param("orgid") String orgid);

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
	StaffResult findUserInfoExam(@Param("staffId") Integer staffId, @Param("rid") Integer rid);

	/**
	 * 根据集团与类型获取角色ID
	 * @param orgid
	 * @param type
	 * @return
	 */
	Integer findLikeUser(@Param("orgid") Integer orgid, @Param("type") String type);

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

	/**
	 * 根据部门名称与部门ID 查询用户信息
	 * @param workUnitName
	 * @param orgid
	 * @return
	 */
	StaffResult findUserInfoForBelongGroupName(@Param("belongGroupName") String workUnitName, @Param("orgid") Long orgid);

	/**
	 * 所有的公司
	 * @return
	 */
	List<TblOrganization> findAllList();

	/**
	 * 人员台账列表
	 * @param param
	 */
	List<UserAllResult> findUserAllList(@Param("param") UserAllQueryParam param);

	/**
	 * 根据用户ID、角色ID 查询该用户是否是这个角色
	 * @param staffId
	 * @param roleId
	 * @return
	 */
	@Select("select count(*) from TBL_STAFF where STAFFID = #{staffId}")
	Integer findFlagUserRole(@Param("staffId") Long staffId, @Param("roleId") Long roleId);

	/**
	 * 部门id或集团id 查询数据
	 * @param orgid
	 * @return
	 */
	StaffResult findIdUserInfo(@Param("orgid") Long orgid);
}
