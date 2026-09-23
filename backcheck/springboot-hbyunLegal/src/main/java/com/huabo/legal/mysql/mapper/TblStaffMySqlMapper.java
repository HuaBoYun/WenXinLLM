package com.huabo.legal.mysql.mapper;

import com.huabo.legal.mysql.entity.TblStaffMySql;
import com.huabo.legal.vo.result.UserInfo;
import com.huabo.legal.vo.result.StaffResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TblStaffMySqlMapper extends Mapper<TblStaffMySql> {

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
	StaffResult findCreatorUserInfo(@Param("staffId") Integer staffId);

	/**
	 * 批量获取用户信息
	 * @param staffId
	 * @return
	 */
	List<UserInfo> findCreatorUserInfos(@Param("staffId") String staffId);
	
	/**
	 * 根据工作单位获取当前部门合规管理员
	 * @param orgid
	 * @param type
	 * @return
	 */
	StaffResult getCompliance(String workUnit);
}