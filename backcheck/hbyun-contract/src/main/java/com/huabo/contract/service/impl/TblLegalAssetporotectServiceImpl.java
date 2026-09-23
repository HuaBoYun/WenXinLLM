package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblLegalAssetporotect;
import com.huabo.contract.mapper.TblLegalAssetporotectMapper;
import com.huabo.contract.service.TblLegalAssetporotectService;

@Service
public class TblLegalAssetporotectServiceImpl implements TblLegalAssetporotectService{
	
	@Resource
    private TblLegalAssetporotectMapper tblLegalAssetporotectMapper;

	@Override
	public void findListByPage(PageInfo<TblLegalAssetporotect> pageInfo, BigDecimal litigationid, BigDecimal arbitraid) {
		IPage<TblLegalAssetporotect> page = new Page<TblLegalAssetporotect>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		IPage<TblLegalAssetporotect> pageList = tblLegalAssetporotectMapper.findListByPage(page, litigationid, arbitraid);
		pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public void addLegalAssetporotectService(TblLegalAssetporotect tla) {
		BigDecimal id = tla.getId();
		if(null == id) {
			//新增
			tla.setId(RandomUtil.uuBigDecimalId());
			this.tblLegalAssetporotectMapper.addEntity(tla);
		}else {
			//修改
			this.tblLegalAssetporotectMapper.updateEntity(tla);
		}
	}

	@Override
	public TblLegalAssetporotect getLegalAssetporotectById(BigDecimal id) {
		return this.tblLegalAssetporotectMapper.findById(id);
	}

	@Override
	public void deleteLegalAssetporotectById(BigDecimal id) {
		this.tblLegalAssetporotectMapper.deleteEntityById(id);
		
	}

}
