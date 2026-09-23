package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpInventory;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaIpInventoryMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaIpInventoryService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpInventoryQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaIpInventoryServiceImpl implements TblCeaIpInventoryService {

	@Resource
	private TblCeaIpInventoryMapper tblCeaIpInventoryMapper;

	@Override
	public PageInfo<TblCeaIpInventory> getList(TblCeaIpInventoryQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaIpInventoryMapper.getList(param));
	}

	@Override
	public TblCeaIpInventory saveOrUpdate(TblCeaIpInventory param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaIpInventoryMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaIpInventoryMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaIpInventoryMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaIpInventory findById(Long id) {
		TblCeaIpInventory model = tblCeaIpInventoryMapper.selectByPrimaryKey(id);
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
		int count = tblCeaIpInventoryMapper.selectCount(TblCeaIpInventory.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
