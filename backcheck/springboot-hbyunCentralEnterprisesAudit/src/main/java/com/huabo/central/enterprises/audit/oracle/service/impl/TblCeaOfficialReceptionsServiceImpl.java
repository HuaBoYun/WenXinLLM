package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaNetworkAgentOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficialReceptions;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaOfficialReceptionsMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaOfficialReceptionsService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficialReceptionsQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaOfficialReceptionsServiceImpl implements TblCeaOfficialReceptionsService {

	@Resource
	private TblCeaOfficialReceptionsMapper tblCeaOfficialReceptionsMapper;

	@Override
	public PageInfo<TblCeaOfficialReceptions> getList(TblCeaOfficialReceptionsQueryParam param) {
		Example example = new Example(TblCeaOfficialReceptions.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getReceiverType())) {
			criteria.andLike("receiverType", "%" + param.getReceiverType() + "%");
		}
		if (StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" ( creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + "))) ");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaOfficialReceptionsMapper.selectByExample(example));
	}

	@Override
	public TblCeaOfficialReceptions saveOrUpdate(TblCeaOfficialReceptions param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaOfficialReceptionsMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaOfficialReceptionsMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaOfficialReceptionsMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaOfficialReceptions findById(Long id) {
		TblCeaOfficialReceptions model = tblCeaOfficialReceptionsMapper.selectByPrimaryKey(id);
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
		int count = tblCeaOfficialReceptionsMapper.selectCount(TblCeaOfficialReceptions.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
