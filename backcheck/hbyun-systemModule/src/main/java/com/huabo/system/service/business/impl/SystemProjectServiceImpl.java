package com.huabo.system.service.business.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.BaseDao;
import com.huabo.system.entity.TblSystemHomeAuthorizationOracle;
import com.huabo.system.entity.TblSystemHomePageOracle;
import com.huabo.system.entity.TblSystemLoginPageOracle;
import com.huabo.system.entity.TblSystemProjectAuthOracle;
import com.huabo.system.entity.TblSystemProjectOracle;
import com.huabo.system.oracle.service.TblSystemHomeAuthorizationOracleService;
import com.huabo.system.oracle.service.TblSystemHomePageOracleService;
import com.huabo.system.oracle.service.TblSystemLoginPageOracleService;
import com.huabo.system.oracle.service.TblSystemProjectAuthOracleService;
import com.huabo.system.oracle.service.TblSystemProjectOracleService;
import com.huabo.system.service.business.FileUploadService;
import com.huabo.system.service.business.SystemProjectService;
import com.huabo.system.utils.JsonBean;
import com.huabo.system.utils.PageResult;
import com.huabo.system.utils.ResponseFormat;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationParam;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageStateParam;
import com.huabo.system.vo.param.TblSystemLoginPageQueryParam;
import com.huabo.system.vo.param.TblSystemLoginPageStateParam;
import com.huabo.system.vo.param.TblSystemProjectAuthParam;
import com.huabo.system.vo.param.TblSystemProjectAuthQueryParam;
import com.huabo.system.vo.param.TblSystemProjectQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SystemProjectServiceImpl implements SystemProjectService {

	@Resource
	private TblSystemProjectOracleService tblSystemProjectOracleService;
	@Resource
	private TblSystemLoginPageOracleService tblSystemLoginPageOracleService;
	@Resource
	private TblSystemHomePageOracleService tblSystemHomePageOracleService;
	@Resource
	private FileUploadService fileUploadService;
	@Resource
	private TblSystemProjectAuthOracleService tblSystemProjectAuthOracleService;
	@Resource
	private TblSystemHomeAuthorizationOracleService tblSystemHomeAuthorizationOracleService;

	/**
	 * 系统项目模块列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblSystemProjectList(TblSystemProjectQueryParam param) {
		param.setCreator(null);
		param.setWorkUnit(null);
		List<TblSystemProjectOracle> list = tblSystemProjectOracleService.getList(param);
		return ResponseFormat.retParam(200, 200, list);
	}

	/**
	 * 系统项目模块-新增或修改
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblSystemProject(TblSystemProjectOracle param) {
		//根据第三方名称获取第三方编码
		if (StringUtils.isNotBlank(param.getOtherName())) {
			param.setOtherNo(getOtherNo(param.getOtherName()));
		}
		TblSystemProjectOracle systemProject = tblSystemProjectOracleService.saveOrUpdate(param);
		systemProject.setOtherNo(param.getOtherNo());
		return ResponseFormat.retParam(200, 200, systemProject);
	}

	private String getOtherNo(String otherName) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			try {
				String sql = "SELECT F_Id FROM base_system WHERE F_FullName=" + "'" + otherName + "'";
				con = BaseDao.getInstance().getConnection();
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					fid = rs.getString("F_Id");
				}
			} finally {
				BaseDao.getInstance().close(con, rs, ps);
			}
		} catch (Exception e) {
			log.error("获取第三方项目编码异常");
		}
		return fid;
	}

	/**
	 * 系统项目模块-信息 {id}为主键id
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblSystemProject(BigDecimal id) {
		TblSystemProjectOracle systemProject = tblSystemProjectOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, systemProject);
	}

	/**
	 * 系统项目模块-信息删除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean deleteTblSystemProject(BigDecimal id) {
		//删除 系统项目模块
		tblSystemProjectOracleService.delete(id);
		//删除 系统项目模块授权信息
		tblSystemProjectAuthOracleService.deleteProjectAuth(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 登录页配置列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblSystemLoginPageList(TblSystemLoginPageQueryParam param) {
		PageInfo<TblSystemLoginPageOracle> list = tblSystemLoginPageOracleService.getList(param);
		PageResult<TblSystemLoginPageOracle> build = new PageResult<TblSystemLoginPageOracle>().build(list);
		return ResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 登录页配置-新增或修改
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblSystemLoginPage(TblSystemLoginPageOracle param) {
		TblSystemLoginPageOracle systemLoginPage = tblSystemLoginPageOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, systemLoginPage);
	}

	/**
	 * 登录页配置-信息
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblSystemLoginPage(BigDecimal id) {
		TblSystemLoginPageOracle systemLoginPage = tblSystemLoginPageOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, systemLoginPage);
	}

	/**
	 * 登录页配置-信息删除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblSystemLoginPage(BigDecimal id) {
		TblSystemLoginPageOracle systemLoginPage = tblSystemLoginPageOracleService.findById(id);
		if (systemLoginPage != null) {
			tblSystemLoginPageOracleService.delete(id);
			String leftUpperPicture = systemLoginPage.getLeftUpperPicture();
			if (StringUtils.isNotBlank(leftUpperPicture)) {
				fileUploadService.deletePicture(leftUpperPicture);
			}
			String homePicture = systemLoginPage.getHomePicture();
			if (StringUtils.isNotBlank(homePicture)) {
				fileUploadService.deletePicture(homePicture);
			}
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 登录页配置-状态变更
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean updateStateTblSystemLoginPage(TblSystemLoginPageStateParam param) {
		tblSystemLoginPageOracleService.updateStateTblSystemLoginPage(param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 首页配置列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblSystemHomePageList(TblSystemHomePageQueryParam param) {
		PageInfo<TblSystemHomePageOracle> list = tblSystemHomePageOracleService.getList(param);
		PageResult<TblSystemHomePageOracle> build = new PageResult<TblSystemHomePageOracle>().build(list);
		return ResponseFormat.retParam(200, 200, build);
	}

	@Override
	public JsonBean saveOrUpdateTblSystemHomePage(TblSystemHomePageOracle param) {
		TblSystemHomePageOracle tblSystemHomePage = tblSystemHomePageOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, tblSystemHomePage);
	}

	/**
	 * 首页配置-信息
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblSystemHomePage(BigDecimal id) {
		TblSystemHomePageOracle systemHomePage = tblSystemHomePageOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, systemHomePage);
	}

	/**
	 * 首页配置-信息删除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblSystemHomePage(BigDecimal id) {
		tblSystemHomePageOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 首页配置-状态变更
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean updateStateTblSystemHomePage(TblSystemHomePageStateParam param) {
		tblSystemHomePageOracleService.updateStateTblSystemHomePage(param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 系统项目授权列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblSystemProjectAuthList(TblSystemProjectAuthQueryParam param) {
		List<TblSystemProjectAuthOracle> list = tblSystemProjectAuthOracleService.getList(param);
		return ResponseFormat.retParam(200, 200, list);
	}

	/**
	 * 系统项目授权-新增
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean saveOrUpdateTblSystemProjectAuth(TblSystemProjectAuthParam param) {
		tblSystemProjectAuthOracleService.saveOrUpdate(param);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 登录页配置-登录页信息
	 * @param belongGroup
	 * @return
	 */
	@Override
	public JsonBean getTblSystemLoginPageInfo(BigDecimal belongGroup) {
		if (Objects.isNull(belongGroup)) {
			return ResponseFormat.retParam(200, 200, null);
		}
		TblSystemLoginPageOracle loginPageInfo = tblSystemLoginPageOracleService.getTblSystemLoginPageInfo(belongGroup);
		return ResponseFormat.retParam(200, 200, loginPageInfo);
	}

	/**
	 * 首页配置-授权列表
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean<List<TblSystemHomeAuthorizationOracle>> getTblSystemHomePageAuthList(TblSystemHomeAuthorizationQueryParam param) {
		List<TblSystemHomeAuthorizationOracle> list = tblSystemHomeAuthorizationOracleService.getList(param);
		if (CollectionUtil.isEmpty(list)) {
			return ResponseFormat.retParam(200, 200, Collections.emptyList());
		}
		return ResponseFormat.retParam(200, 200, list);
	}

	/**
	 * 首页配置-授权-新增
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean saveOrUpdateTblSystemHomePageAuth(TblSystemHomeAuthorizationParam param) {
		//先删除后新增
		tblSystemHomeAuthorizationOracleService.delete(param.getHomePageId());
		param.getList().forEach(item -> {
			//查询该公司是否有授权记录  如果有则删除 后新增
			TblSystemHomePageOracle tblSystemHomePageAuthCompany = tblSystemHomePageOracleService.getTblSystemHomePageAuthCompany(item);
			if (Objects.nonNull(tblSystemHomePageAuthCompany)) {
				tblSystemHomeAuthorizationOracleService.deleteAuthCompany(item);
			}
			TblSystemHomeAuthorizationOracle homeAuthorization = new TblSystemHomeAuthorizationOracle();
			homeAuthorization.setHomePageId(param.getHomePageId());
			homeAuthorization.setBelongGroup(item);
			tblSystemHomeAuthorizationOracleService.saveOrUpdate(homeAuthorization);
		});
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 首页配置-公司
	 * @param belongGroup
	 * @return
	 */
	@Override
	public JsonBean<TblSystemHomePageOracle> getTblSystemHomePageAuthCompany(BigDecimal belongGroup) {
		TblSystemHomePageOracle result = tblSystemHomePageOracleService.getTblSystemHomePageAuthCompany(belongGroup);
		return ResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 系统项目模块-查询编码流程平台项目编码是否存在
	 * @param uniqueIdentification
	 * @return
	 */
	@Override
	public JsonBean isUniqueIdentification(String uniqueIdentification) {
		Boolean result = tblSystemProjectOracleService.isUniqueIdentification(uniqueIdentification);
		return ResponseFormat.retParam(200, 200, result);
	}

}
