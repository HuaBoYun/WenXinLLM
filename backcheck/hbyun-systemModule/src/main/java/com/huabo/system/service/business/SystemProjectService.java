package com.huabo.system.service.business;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblSystemHomeAuthorizationOracle;
import com.huabo.system.entity.TblSystemHomePageOracle;
import com.huabo.system.entity.TblSystemLoginPageOracle;
import com.huabo.system.entity.TblSystemProjectOracle;
import com.huabo.system.utils.JsonBean;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationParam;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageStateParam;
import com.huabo.system.vo.param.TblSystemLoginPageQueryParam;
import com.huabo.system.vo.param.TblSystemLoginPageStateParam;
import com.huabo.system.vo.param.TblSystemProjectAuthParam;
import com.huabo.system.vo.param.TblSystemProjectAuthQueryParam;
import com.huabo.system.vo.param.TblSystemProjectQueryParam;

public interface SystemProjectService {

	/**
	 * 系统项目模块列表
	 * @param param
	 * @return
	 */
	JsonBean getTblSystemProjectList(TblSystemProjectQueryParam param);

	/**
	 * 系统项目模块-新增或修改
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblSystemProject(TblSystemProjectOracle param);

	/**
	 * 系统项目模块-信息 {id}为主键id
	 * @param id
	 * @return
	 */
	JsonBean getTblSystemProject(BigDecimal id);

	/**
	 * 系统项目模块-信息删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblSystemProject(BigDecimal id);

	/**
	 * 登录页配置列表
	 * @param param
	 * @return
	 */
	JsonBean getTblSystemLoginPageList(TblSystemLoginPageQueryParam param);

	/**
	 * 登录页配置-新增或修改
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblSystemLoginPage(TblSystemLoginPageOracle param);

	/**
	 * 登录页配置-信息
	 * @param id
	 * @return
	 */
	JsonBean getTblSystemLoginPage(BigDecimal id);

	/**
	 * 登录页配置-信息删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblSystemLoginPage(BigDecimal id);

	/**
	 * 登录页配置-状态变更
	 * @param param
	 * @return
	 */
	JsonBean updateStateTblSystemLoginPage(TblSystemLoginPageStateParam param);

	/**
	 * 首页配置列表
	 * @param param
	 * @return
	 */
	JsonBean getTblSystemHomePageList(TblSystemHomePageQueryParam param);

	/**
	 * 首页配置-新增或修改
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblSystemHomePage(TblSystemHomePageOracle param);

	/**
	 * 首页配置-信息
	 * @param id
	 * @return
	 */
	JsonBean getTblSystemHomePage(BigDecimal id);

	/**
	 * 首页配置-信息删除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblSystemHomePage(BigDecimal id);

	/**
	 * 首页配置-状态变更
	 * @param param
	 * @return
	 */
	JsonBean updateStateTblSystemHomePage(TblSystemHomePageStateParam param);

	/**
	 * 系统项目授权列表
	 * @param param
	 * @return
	 */
	JsonBean getTblSystemProjectAuthList(TblSystemProjectAuthQueryParam param);

	/**
	 * 系统项目授权-新增
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblSystemProjectAuth(TblSystemProjectAuthParam param);

	/**
	 * 登录页配置-登录页信息
	 * @param belongGroup
	 * @return
	 */
	JsonBean getTblSystemLoginPageInfo(BigDecimal belongGroup);

	/**
	 * 首页配置-授权列表
	 * @param param
	 * @return
	 */
	JsonBean<List<TblSystemHomeAuthorizationOracle>> getTblSystemHomePageAuthList(TblSystemHomeAuthorizationQueryParam param);

	/**
	 * 首页配置-授权-新增
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblSystemHomePageAuth(TblSystemHomeAuthorizationParam param);

	/**
	 * 首页配置-公司
	 * @param belongGroup
	 * @return
	 */
	JsonBean<TblSystemHomePageOracle> getTblSystemHomePageAuthCompany(BigDecimal belongGroup);

	/**
	 * 系统项目模块-查询编码流程平台项目编码是否存在
	 * @param otherNo
	 * @return
	 */
	JsonBean isUniqueIdentification(String otherNo);

}
