package com.huabo.audit.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblBugEntity;
import com.huabo.audit.oracle.mapper.TblBugMapper;
import com.huabo.audit.service.TblBugService;
@Service
public class TblBugServiceImpl extends ServiceImpl<TblBugMapper, TblBugEntity>  implements TblBugService {

	@Override
	public TblBugEntity findById(BigDecimal bigDecimal) {
		TblBugEntity tblBugEntity = baseMapper.findById(bigDecimal+"");
		return tblBugEntity;
	}

}
