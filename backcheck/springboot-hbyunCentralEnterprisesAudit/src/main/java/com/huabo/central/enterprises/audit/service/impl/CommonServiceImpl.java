package com.huabo.central.enterprises.audit.service.impl;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CommonService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.vo.param.UpdateUserOnDutyStatusParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 用户信息
	 * @param id
	 * @return
	 */
	@Override
	public MyJsonBean<TblStaffOracle> getUserInfo(Long id) {
		TblStaffOracle result = tblStaffOracleService.getUserInfoForId(id);
		return MyResponseFormat.retParam(200, 200, result);
	}

	/**
	 * 更新用户在岗状态
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<Void> updateUserOnDutyStatus(UpdateUserOnDutyStatusParam param) {
		if (Objects.isNull(param.getUserId())) {
			throw new ServiceException("用户ID不能为空");
		}
		Long count = tblStaffOracleService.getUserJobProjectCount(param.getUserId());
		if (count > 0) {
			param.setOnDutyStatus(2);
		}
		tblStaffOracleService.updateUserOnDutyStatus(param);
		return MyResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 人员台账列表
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<UserAllResult> getUserAllList(UserAllQueryParam param) {
		PageInfo<UserAllResult> pageInfo = tblStaffOracleService.getUserAllList(param);
		PageResult<UserAllResult> build = new PageResult<UserAllResult>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}
}
