package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblUserOrgRelation;

public interface TblUserOrgRelationService {

	/**
	 * 用户新增修改 处理用户与多个组织之间的关系数据
	 * @param loginStaff
	 * @param deptIds
	 * @return
	 * @throws Exception
	 */
	List<TblUserOrgRelation> dealInsertRealtionInfo(TblStaffUtil loginStaff, String[] deptIds) throws Exception;

	/**
	 * 通过用户主键删除 多组织中间关系表数据
	 * @param staffId
	 * @throws Exception
	 */
	void removeRelationByStaffId(BigDecimal staffId) throws Exception;

	/**
	 * 保存新增用户和多个组织之间的关系
	 * @param staffId
	 * @param relaList
	 * @throws Exception
	 */
	void InsertRealtionByStaffId(BigDecimal staffId, List<TblUserOrgRelation> relaList) throws Exception;

	/**
	 * 当前用户获取自己的组织机构关系信息
	 * @param token
	 * @param orgname 
	 * @return
	 * @throws Exception
	 */
	JsonBean getLoginUserOrgRelationList(String token, String orgname) throws Exception;

	/**
	 * 一体化平台同步数据时调用接口
	 * @param tblStaff
	 * @param relaList
	 * @return 
	 * @throws Exception
	 */
	List<TblUserOrgRelation> initDateDealRelation(TblStaff tblStaff, List<TblUserOrgRelation> relaList) throws Exception;

	/**
	 * 根据用户Id 获取 用户和多组织之间的关系
	 * @param staffid
	 * @return
	 * @throws Exception
	 */
	List<TblUserOrgRelation> selectUserOrgRelationInfoListByStaffId(BigDecimal staffid) throws Exception;

	List<TblUserOrgRelation> dealAddRealtionInfo(TblStaffUtil loginStaff, String[] deptIds) throws Exception;

}
