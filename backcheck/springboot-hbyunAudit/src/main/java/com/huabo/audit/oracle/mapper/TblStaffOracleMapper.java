package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblStaffOracle;
import com.huabo.audit.oracle.vo.StaffResult;
import com.huabo.audit.oracle.vo.UserInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

public interface TblStaffOracleMapper extends Mapper<TblStaffOracle> {

	/**
	 * 根据集团id 查询名称
	 * @param orgid
	 * @return
	 */
	StaffResult findBelongGroupIdUserInfo(@Param("orgid") Integer orgid);

	/**
	 * 根据工作单位id 查询信息
	 * @param orgid
	 * @return
	 */
	StaffResult findWorkUnitIdUserInfo(@Param("orgid") Integer orgid);

	/**
	 * 根据用户id 查询信息
	 * @param staffId
	 * @return
	 */
	StaffResult findUserInfo(@Param("staffId") Integer staffId);

	/**
	 * 根据创建人ID 查询信息
	 * @param staffId
	 * @return
	 */
	StaffResult findCreatorUserInfo(@Param("staffId") BigDecimal staffId);

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

	TblStaffOracle getUserInfoForId(@Param("staffId") Integer staffId);
}
