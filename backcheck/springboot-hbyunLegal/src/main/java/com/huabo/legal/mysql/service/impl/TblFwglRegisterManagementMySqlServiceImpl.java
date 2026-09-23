package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglRegisterManagementMySql;
import com.huabo.legal.mysql.mapper.TblFwglRegisterManagementMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglRegisterManagementMySqlService;
import com.huabo.legal.vo.param.TblFwglRegisterManagementQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglRegisterManagementMySqlServiceImpl implements TblFwglRegisterManagementMySqlService {

	@Resource
	private TblFwglRegisterManagementMySqlMapper tblFwglRegisterManagementMySqlMapper;

	/**
	 * 登记管理列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglRegisterManagementMySql> getList(TblFwglRegisterManagementQueryParam param) {
		Example example = new Example(TblFwglRegisterManagementMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getType() != null) {
			criteria.andEqualTo("type", param.getType());
		}
		if (StringUtils.isNotBlank(param.getRegisterName())) {
			criteria.andLike("registerName", "%" + param.getRegisterName() + "%");
		}
		if (param.getRegisterBeginDate() != null) {
			criteria.andGreaterThanOrEqualTo("registerAnnouncementTime", param.getRegisterBeginDate());
		}
		if (param.getRegisterEndDate() != null) {
			criteria.andLessThan("registerAnnouncementTime", DateUtil.addDays(param.getRegisterEndDate(), 1));
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" REGISTERMANAGEMENTID desc");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglRegisterManagementMySqlMapper.selectByExample(example));
	}

	/**
	 * 登记管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglRegisterManagementMySql saveOrUpdate(TblFwglRegisterManagementMySql param) {
		Date now = new Date();
		if (param.getRegisterManagementId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglRegisterManagementMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getRegisterManagementId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglRegisterManagementMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getRegisterManagementId());
	}

	/**
	 * 登记管理详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglRegisterManagementMySql findById(Integer id) {
		TblFwglRegisterManagementMySql registerManagement = tblFwglRegisterManagementMySqlMapper.selectByPrimaryKey(id);
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
	public void delete(Integer id) {
		tblFwglRegisterManagementMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 常年法律服务/专项法律服务 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglRegisterManagementMySqlMapper.selectCount(TblFwglRegisterManagementMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
