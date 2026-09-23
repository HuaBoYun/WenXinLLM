package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaDigitalCertificateOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaDigitalCertificateOracleService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaDigitalCertificateService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.TblCeaDigitalCertificateQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CeaDigitalCertificateServiceImpl implements CeaDigitalCertificateService {

	@Resource
	private TblCeaDigitalCertificateOracleService tblCeaDigitalCertificateOracleService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 数字证书管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaDigitalCertificateOracle> getTblCeaDigitalCertificateList(TblCeaDigitalCertificateQueryParam param) {
		PageInfo<TblCeaDigitalCertificateOracle> pageInfo = tblCeaDigitalCertificateOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getList())) {
			List<Long> creators = pageInfo.getList().stream().filter(x -> Objects.nonNull(x.getCreator())).distinct()
					.map(TblCeaDigitalCertificateOracle::getCreator).distinct().collect(Collectors.toList());
			Map<Long, String> creatorUserInfoMap = tblStaffOracleService.getCreatorUserInfoMap(StringUtils.join(creators, ","));
			pageInfo.getList().forEach(item -> {
				item.setCreatorName(creatorUserInfoMap.getOrDefault(item.getCreator(), ""));
			});
			PageResult<TblCeaDigitalCertificateOracle> build = new PageResult<TblCeaDigitalCertificateOracle>().build(pageInfo);
			return MyResponseFormat.retParam(200, 200, build);
		}
		return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
	}

	/**
	 * 数字证书管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaDigitalCertificateOracle> saveOrUpdateTblCeaDigitalCertificate(TblCeaDigitalCertificateOracle param) {
		if("新办证书".equals(param.getServiceType())||"证书注销".equals(param.getServiceType())) {
			param.setState(0);
		}else {
			param.setState(6);
		}
		TblCeaDigitalCertificateOracle model = tblCeaDigitalCertificateOracleService.saveOrUpdate(param);
		return MyResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 数字证书管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<Void> deleteTblCeaDigitalCertificate(Long id) {
		tblCeaDigitalCertificateOracleService.delete(id);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 数字证书管理 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblCeaDigitalCertificateOracle> getTblCeaDigitalCertificate(Long id) {
		TblCeaDigitalCertificateOracle model = tblCeaDigitalCertificateOracleService.findById(id);
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		return MyResponseFormat.retParam(200, 200, model);
	}
}
