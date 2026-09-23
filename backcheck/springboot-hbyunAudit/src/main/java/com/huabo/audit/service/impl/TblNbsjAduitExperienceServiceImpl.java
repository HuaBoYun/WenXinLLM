package com.huabo.audit.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblNbsjAduitExperienceEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAduitExperienceMapper;
import com.huabo.audit.service.TblNbsjAduitExperienceService;
@Service
public class TblNbsjAduitExperienceServiceImpl extends ServiceImpl<TblNbsjAduitExperienceMapper, TblNbsjAduitExperienceEntity> implements TblNbsjAduitExperienceService {

	@Override
	public void deleteByTargetId(Integer targetId) {
		baseMapper.deleteByTargetId(targetId+"");
	}

	@Override
	public void deleteByTempId(BigDecimal tempId) {
		baseMapper.deleteByTempId(tempId+"");

	}

	@Override
	public void merge(TblNbsjAduitExperienceEntity aduitTarget) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(TblNbsjAduitExperienceEntity tblAduitTarget) {
		BigDecimal programId = tblAduitTarget.getProgramId();
		
		baseMapper.deleteById(programId+"");

	}

}
