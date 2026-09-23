package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayExp;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaCancelHolidayExpMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaCancelHolidayExpService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TblCeaCancelHolidayExpServiceImpl implements TblCeaCancelHolidayExpService {

	@Resource
	private TblCeaCancelHolidayExpMapper tblCeaCancelHolidayExpMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	@Override
	public PageInfo<TblCeaCancelHolidayExp> getList(TblCeaCancelHolidayQueryParam param) {
		if (Objects.nonNull(param.getFillFormTimeEnd())) {
			param.setFillFormTimeEnd(DateUtil.addDays(param.getFillFormTimeEnd(), 1));
		}
		if (Objects.nonNull(param.getBelongGroup())) {
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(param.getBelongGroup());
			param.setTblOrganizationAll(tblOrganizationAll);
		}
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaCancelHolidayExpMapper.getList(param));
	}

	@Override
	public TblCeaCancelHolidayExp saveOrUpdate(TblCeaCancelHolidayExp param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaCancelHolidayExpMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaCancelHolidayExpMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaCancelHolidayExpMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaCancelHolidayExp findById(Long id) {
		TblCeaCancelHolidayExp model = tblCeaCancelHolidayExpMapper.selectByPrimaryKey(id);
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
		int count = tblCeaCancelHolidayExpMapper.selectCount(TblCeaCancelHolidayExp.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
