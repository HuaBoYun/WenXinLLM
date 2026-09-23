package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblLegalCloseSum;
import com.huabo.contract.entity.TblLegalDisputregistration;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.mapper.TblLegalCloseSumMapper;
import com.huabo.contract.mapper.TblLegalDisputregistrationMapper;
import com.huabo.contract.service.TblLegalCloseSumService;

@Service
public class TblLegalCloseSumServiceImpl implements TblLegalCloseSumService{
	@Resource
    private TblLegalCloseSumMapper tblLegalCloseSumMapper;
	
	@Resource
	private TblLegalDisputregistrationMapper tblLegalDisputregistrationMapper;
	
	@Resource
	private TblCyhwUnitMapper tblCyhwUnitMapper;
	
	@Override
	public void addLegalCloseSum(TblLegalCloseSum tla) throws Exception {
			BigDecimal id = tla.getId();
			if(null == id) {
				//新增
				tla.setId(RandomUtil.uuBigDecimalId());
				this.tblLegalCloseSumMapper.addEntity(tla);
				
				if(tla.getLitigationid() != null) {
					TblLegalDisputregistration dispute = this.tblLegalDisputregistrationMapper.findByIdByLitigation(tla.getLitigationid());
					if(dispute.getContractinfo() != null) {
						TblCyhwUnit unit =  this.tblCyhwUnitMapper.selectChangeContractInfo(dispute.getContractinfo());
						 this.tblCyhwUnitMapper.updateModifyContractStatus(new BigDecimal(unit.getContractstatus()), unit.getHistoryStatus(), unit.getContractid());
					}
				}
				if(tla.getArbitraid() != null) {
					TblLegalDisputregistration dispute = this.tblLegalDisputregistrationMapper.findByIdByArbitra(tla.getArbitraid());
					if(dispute.getContractinfo() != null) {
						TblCyhwUnit unit =  this.tblCyhwUnitMapper.selectChangeContractInfo(dispute.getContractinfo());
						 this.tblCyhwUnitMapper.updateModifyContractStatus(new BigDecimal(unit.getContractstatus()), unit.getHistoryStatus(), unit.getContractid());
					}
				}
			}else {
				//修改
				this.tblLegalCloseSumMapper.updateEntity(tla);
			}
	}

	@Override
	public TblLegalCloseSum getLegalCloseSumById(BigDecimal id) {
		return this.tblLegalCloseSumMapper.findById(id);
	}

	@Override
	public void deleteLegalCloseSumById(BigDecimal id) {
		this.tblLegalCloseSumMapper.deleteEntityById(id);
		
	}
	
	@Override
	public TblLegalCloseSum getLegalCloseSumBySSZCId(BigDecimal litigationid,BigDecimal arbitraid) {
		
		if(null != litigationid) {
			return this.tblLegalCloseSumMapper.findByLitigationid(litigationid);
		}
		if(null != arbitraid) {
			return this.tblLegalCloseSumMapper.findByAarbitraid(arbitraid);
		}
		
		return null;
	}

	
}
