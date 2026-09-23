package com.huabo.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.entity.TblStaffOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.SystemAgentInfoMapper;
import com.huabo.system.mapper.SystemAgentIssuedMapper;
import com.huabo.system.mapper.TblStaffOracleMapper;
import com.huabo.system.service.SystemAgentInfoService;
import com.huabo.system.vo.param.SystemAgentInfoQueryParam;
import com.huabo.system.vo.param.SystemAgentModuleParam;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class SystemAgentInfoServiceImpl implements SystemAgentInfoService {

	@Resource
	private SystemAgentInfoMapper systemAgentInfoMapper;
	@Resource
	private SystemAgentIssuedMapper systemAgentIssuedMapper;
	@Resource
	private TblStaffOracleMapper tblStaffOracleMapper;

	@Override
	public PageInfo<SystemAgentInfo> getList(SystemAgentInfoQueryParam param) {
		Weekend<SystemAgentInfo> weekend = Weekend.of(SystemAgentInfo.class);
		WeekendCriteria<SystemAgentInfo, Object> weekendCriteria = weekend.weekendCriteria();
		if (Objects.equals(param.getFlagIssued(), 1)) {
			weekendCriteria.andEqualTo(SystemAgentInfo::getFlagIssued, 1);
		}
		weekend.orderBy("id").desc();
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> systemAgentInfoMapper.selectByExample(weekend));
	}

	@Override
	public SystemAgentInfo saveOrUpdate(SystemAgentInfo param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			systemAgentInfoMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setCreator(null);
			param.setWorkUnit(null);
			param.setBelongGroup(null);
			systemAgentInfoMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		systemAgentInfoMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 不同模块下的智能体-列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<SystemAgentInfo> getSystemAgentModuleList(SystemAgentModuleParam param) {
		param.getCreator();
		TblStaffOracle staff = new TblStaffOracle();
		staff.setStaffId(param.getCreator());
		TblStaffOracle staffOracle = tblStaffOracleMapper.selectOne(staff);
		String roleIdsTrs = staffOracle.getRoleIdsTrs();
		//角色集合
		List<String> roleIdsTrsList = Arrays.asList(roleIdsTrs.split(","));
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> systemAgentInfoMapper.findSystemAgentModuleList(param, roleIdsTrsList));
	}

	@Override
	public SystemAgentInfo findById(Long id) {
		SystemAgentInfo model = systemAgentInfoMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = systemAgentInfoMapper.selectCount(SystemAgentInfo.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
