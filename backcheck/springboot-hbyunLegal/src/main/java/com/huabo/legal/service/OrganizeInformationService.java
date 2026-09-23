package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.vo.param.TblFwglFirmLawyerQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalAdviserQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalOrganizationQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalPersonnelQueryParam;
import com.huabo.legal.vo.result.*;

public interface OrganizeInformationService {

	/**
	 * 查询公司律师列表 查询
	 * @param pageableParam
	 * @return
	 */
	JsonBean getTblFwglFirmLawyerList(TblFwglFirmLawyerQueryParam pageableParam);

	/**
	 * 公司律师 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglFirmLawyer(TblFwglFirmLawyer param);


	/**
	 * 公司律师 删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	JsonBean deleteTblFwglFirmLawyer(Long id);


	/**
	 * 公司律师详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglFirmLawyer(Long id);

	/**
	 * 公司律师-简历 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglFirmLawyerExt(TblFwglFirmLawyerExt param);

	/**
	 * 公司律师-简历 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglFirmLawyerExt(Long id);


	/**
	 * 法务机构及负责人列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglLegalOrganizationList(TblFwglLegalOrganizationQueryParam param);

	/**
	 * 法务机构及负责人 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLegalOrganization(TblFwglLegalOrganization param);

	/**
	 * 法务机构及负责人详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLegalOrganization(Long id);

	/**
	 * 法务机构及负责人 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLegalOrganization(Long id);

	/**
	 * 法务机构及负责人-年度法律审核情况 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLegalOrganizationExt(TblFwglLegalOrganizationExt param);

	/**
	 * 法务机构及负责人-年度法律审核情况 删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLegalOrganizationExt(Long id);

	/**
	 * 法务人员列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglLegalPersonnelList(TblFwglLegalPersonnelQueryParam param);

	/**
	 * 法务人员 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLegalPersonnel(TblFwglLegalPersonnel param);

	/**
	 * 法务人员详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLegalPersonnel(Long id);

	/**
	 * 法务人员-工作经历 删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLegalPersonnel(Long id);

	/**
	 * 法务人员-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLegalPersonnelExt(TblFwglLegalPersonnelExt param);

	/**
	 * 法务人员-工作经历 删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLegalPersonnelExt(Long id);

	/**
	 * 总法律顾问列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglLegalAdviserList(TblFwglLegalAdviserQueryParam param);

	/**
	 * 总法律顾问 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLegalAdviser(TblFwglLegalAdviser param);

	/**
	 * 总法律顾问详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglLegalAdviser(Long id);

	/**
	 * 总法律顾问 删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLegalAdviser(Long id);

	/**
	 * 总法律顾问-工作经历 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglLegalAdviserExt(TblFwglLegalAdviserExt param);

	/**
	 * 总法律顾问-工作经历 删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglLegalAdviserExt(Long id);
}
