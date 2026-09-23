package com.huabo.audit.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblNbsjAuditDatumEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditDatumMapper;
import com.huabo.audit.service.TblNbsjAuditDatumService;
@Service
public class TblNbsjAuditDatumServiceImpl extends ServiceImpl<TblNbsjAuditDatumMapper, TblNbsjAuditDatumEntity> implements TblNbsjAuditDatumService {

	@Override
	public void deleteByExperId(BigDecimal experId) {
		baseMapper.deleteByExperId(experId+"");

	}

	@Override
	public void merge(TblNbsjAuditDatumEntity auditDatum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(TblNbsjAuditDatumEntity datum) {
		BigDecimal datumid = datum.getDatumId();
		baseMapper.delete(datumid+"");

	}

}
