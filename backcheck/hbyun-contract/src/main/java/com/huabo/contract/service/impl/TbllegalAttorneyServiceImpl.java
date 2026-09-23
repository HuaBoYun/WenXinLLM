package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TbllegalAttorney;
import com.huabo.contract.mapper.TbllegalAttorneyMapper;
import com.huabo.contract.service.TbllegalAttorneyService;

@Service
public class TbllegalAttorneyServiceImpl implements TbllegalAttorneyService{
	@Resource
    private TbllegalAttorneyMapper tbllegalAttorneyMapper;
	
	@Override
	public void addlegalAttorney(TbllegalAttorney tla) {
		BigDecimal id = tla.getId();
		if(null == id) {
			//新增
			tla.setId(RandomUtil.uuBigDecimalId());
			this.tbllegalAttorneyMapper.addEntity(tla);
		}else {
			//修改
			this.tbllegalAttorneyMapper.updateEntity(tla);
		}
	}

	@Override
	public TbllegalAttorney getlegalAttorneyById(BigDecimal id) {
		return this.tbllegalAttorneyMapper.findById(id);
	}

	@Override
	public void deletelegalAttorneyById(BigDecimal id) {
		this.tbllegalAttorneyMapper.deleteEntityById(id);
		
	}

	@Override
	public List<TbllegalAttorney> getlegalAttorneyByArbitrationid(BigDecimal arbitrationid) {
		return this.tbllegalAttorneyMapper.findByArbitrationid(arbitrationid);
	}

	@Override
	public List<TbllegalAttorney> getlegalAttorneyByLawsuitid(BigDecimal lawsuitid) {
		return this.tbllegalAttorneyMapper.findByLawsuitid(lawsuitid);
	}

	@Override
	public List<TbllegalAttorney> getlegalAttorneyByNegotiationid(BigDecimal negotiationid) {
		return this.tbllegalAttorneyMapper.findByNegotiationid(negotiationid);
	}

	@Override
	public List<TbllegalAttorney> getlegalAttorneyByDisputeid(BigDecimal disputeid) {
		return this.tbllegalAttorneyMapper.findByDisputeid(disputeid);
	}
}