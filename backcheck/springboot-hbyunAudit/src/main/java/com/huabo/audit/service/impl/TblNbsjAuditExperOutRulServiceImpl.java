package com.huabo.audit.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperOutRulEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditExperOutRulMapper;
import com.huabo.audit.service.TblNbsjAuditExperOutRulService;
@Service
public class TblNbsjAuditExperOutRulServiceImpl extends ServiceImpl<TblNbsjAuditExperOutRulMapper, TblNbsjAuditExperOutRulEntity> implements TblNbsjAuditExperOutRulService {


	@Override
	public TblNbsjAuditExperOutRulEntity findByExperIdAndOutId(BigDecimal experId, BigDecimal outId) {
		TblNbsjAuditExperOutRulEntity tblNbsjAuditExperOutRulEntity = baseMapper.findByExperIdAndOutId(experId+"", outId+"");
		return tblNbsjAuditExperOutRulEntity;
	}

	@Override
	public void deleteByExperId(BigDecimal experId) {
		baseMapper.deleteByExperId(experId+"");

	}

	@Override
	public void delete(BigDecimal bigDecimal) {
		baseMapper.delete(bigDecimal+"");

	}

}
