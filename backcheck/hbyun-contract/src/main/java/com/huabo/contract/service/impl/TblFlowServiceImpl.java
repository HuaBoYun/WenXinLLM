package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblFlow;
import com.huabo.contract.entity.TblFormControllog;
import com.huabo.contract.mapper.TblFlowMapper;
import com.huabo.contract.service.TblFlowService;

@Service
public class TblFlowServiceImpl implements TblFlowService {

    @Resource
    private TblFlowMapper tblFlowMapper;

    @Override
    public TblFlow findById(String flowid) throws Exception {
    	return this.tblFlowMapper.findByFlowid(flowid);
    }

    @Override
    public TblFlow findByFlowid(String flowid) throws Exception {
    	return tblFlowMapper.findByFlow(flowid);
    }

    @Override
    public TblFlow findBy(String flowid) throws Exception {
        return tblFlowMapper.findByFlow(flowid);
    }

    @Override
    public TblFlow findByFlow(BigDecimal flowid) throws Exception {
    	return tblFlowMapper.findFlowid(flowid);
    }

    @Override
    public void saveTfl(TblFormControllog tfl) throws Exception {
    	tfl.setRulelogid(RandomUtil.uuBigDecimalId());
    	tblFlowMapper.saveTfl(tfl);
    }

    @Override
    public void updateTfl(TblFormControllog tfl) throws Exception {
        tblFlowMapper.updateTfl(tfl);
    }

    @Override
    public String findMappingUrl(BigDecimal flowId) throws Exception {
    	return tblFlowMapper.selectFlowid(flowId);
    }

	@Override
	public TblFlow findFlowInfoByNumberOrgId(String flownumber, BigDecimal orgid) throws Exception {
		return this.tblFlowMapper.findFlowInfoByNumberOrgId(flownumber, orgid);
	}
}
