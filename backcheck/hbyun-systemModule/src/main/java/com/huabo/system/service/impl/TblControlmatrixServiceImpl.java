package com.huabo.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.huabo.system.entity.TblControlmatrix;
import com.huabo.system.mapper.TblControlmatrixMapper;
import com.huabo.system.service.TblControlmatrixService;

@Service
public class TblControlmatrixServiceImpl implements TblControlmatrixService {

    @Resource
    private TblControlmatrixMapper tblControlmatrixMapper;

    @Override
    public TblControlmatrix getControlmatrix(String tcmId) {
        List<TblControlmatrix> list = this.tblControlmatrixMapper.findBysql(tcmId);
        return list != null && list.size() > 0 ? (TblControlmatrix) list.get(0) : null;
    }
    
    @Override
    public void updateMatrix(TblControlmatrix com) {
        tblControlmatrixMapper.updateById(com);
    }

    @Override
    public void modify(TblControlmatrix tblControlmatrix) {
        tblControlmatrixMapper.insert(tblControlmatrix);
    }

	@Override
	public void insertMatrix(TblControlmatrix com) {
		tblControlmatrixMapper.insert(com);
	}


}
