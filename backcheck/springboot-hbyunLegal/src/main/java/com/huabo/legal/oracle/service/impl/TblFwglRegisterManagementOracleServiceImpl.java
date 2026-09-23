package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglRegisterManagementOracle;
import com.huabo.legal.oracle.mapper.TblFwglRegisterManagementOracleMapper;
import com.huabo.legal.oracle.service.TblFwglRegisterManagementOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglRegisterManagementQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglRegisterManagementOracleServiceImpl implements TblFwglRegisterManagementOracleService {

	@Resource
	private TblFwglRegisterManagementOracleMapper tblFwglRegisterManagementOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 登记管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglRegisterManagementOracle> getList(TblFwglRegisterManagementQueryParam param) {
		Example example = new Example(TblFwglRegisterManagementOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getType() != null) {
			criteria.andEqualTo("type", param.getType());
		}
		if (StringUtils.isNotBlank(param.getRegisterName())) {
			criteria.andLike("registerName", "%" + param.getRegisterName() + "%");
		}
		if (param.getRegisterBeginDate() != null) {
			criteria.andGreaterThanOrEqualTo("registerTime", param.getRegisterBeginDate());
		}
		if (param.getRegisterEndDate() != null) {
			criteria.andLessThan("registerTime", DateUtil.addDays(param.getRegisterEndDate(), 1));
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
			criteria.andIn("belongGroup", tblOrganizationAll);
			//			criteria.andCondition(" ( belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
			//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+") ");
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" REGISTERMANAGEMENTID desc");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglRegisterManagementOracleMapper.selectByExample(example));
	}

	/**
	 * 登记管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglRegisterManagementOracle saveOrUpdate(TblFwglRegisterManagementOracle param) {
		Date now = new Date();
		if (param.getRegisterManagementId() == null) {
			param.setRegisterManagementId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglRegisterManagementOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getRegisterManagementId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglRegisterManagementOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getRegisterManagementId());
	}

	/**
	 * 登记管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglRegisterManagementOracle findById(Long id) {
		TblFwglRegisterManagementOracle registerManagement = tblFwglRegisterManagementOracleMapper.selectByPrimaryKey(id);
		if (registerManagement == null) {
			throw new ServiceException(400, 50001);
		}
		return registerManagement;
	}

	/**
	 * 登记管理 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglRegisterManagementOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 常年法律服务/专项法律服务 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglRegisterManagementOracleMapper.selectCount(TblFwglRegisterManagementOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
