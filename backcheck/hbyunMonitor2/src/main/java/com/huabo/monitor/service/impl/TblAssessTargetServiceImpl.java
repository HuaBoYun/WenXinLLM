package com.huabo.monitor.service.impl;

import com.huabo.monitor.entity.TblAssessTarget;
import com.huabo.monitor.mapper.TblAssessTargetMapper;
import com.huabo.monitor.service.ITblAssessTargetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service
@Transactional
public class TblAssessTargetServiceImpl extends ServiceImpl<TblAssessTargetMapper, TblAssessTarget> implements ITblAssessTargetService {

	@Resource
	 TblAssessTargetMapper tblAssessTargetMapper;
	
	@Override
	public TblAssessTarget selectTblAssessTarget(BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		return tblAssessTargetMapper.selectTblAssessTarget(id);
	}



}
