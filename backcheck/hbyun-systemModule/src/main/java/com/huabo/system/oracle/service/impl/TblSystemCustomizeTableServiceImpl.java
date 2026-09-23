package com.huabo.system.oracle.service.impl;

import com.huabo.system.entity.TblSystemCustomizeTable;
import com.huabo.system.mapper.TblSystemCustomizeTableMapper;
import com.huabo.system.oracle.service.TblSystemCustomizeTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TblSystemCustomizeTableServiceImpl implements TblSystemCustomizeTableService {

	@Resource
	private TblSystemCustomizeTableMapper tblSystemCustomizeTableMapper;

	@Override
	public List<TblSystemCustomizeTable> getList() {
		return null;
	}

	@Override
	public TblSystemCustomizeTable saveOrUpdate(TblSystemCustomizeTable param) {
		return null;
	}

	@Override
	public TblSystemCustomizeTable findById(Integer id) {
		return null;
	}

	@Override
	public void delete(Integer id) {

	}
}
