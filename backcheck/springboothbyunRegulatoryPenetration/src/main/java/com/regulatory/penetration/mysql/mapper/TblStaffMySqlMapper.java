package com.regulatory.penetration.mysql.mapper;

import com.regulatory.penetration.mysql.entity.TblStaffMySql;
import com.regulatory.penetration.vo.result.StaffResult;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

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
}