package com.huabo.contract.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalFrozenaccount;
import com.huabo.contract.mapper.TblLegalFrozenaccountMapper;
import com.huabo.contract.service.TblLegalFrozenaccountService;

@Service
public class TblLegalFrozenaccountServiceImpl implements TblLegalFrozenaccountService {

    @Resource
    private TblLegalFrozenaccountMapper tblLegalFrozenaccountMapper;

    @Override
    public void findListByPageInfo(PageInfo<TblLegalFrozenaccount> pageInfo, TblLegalFrozenaccount frozenAccount,BigDecimal disputeid) {
    	IPage<TblLegalFrozenaccount> page = new Page<TblLegalFrozenaccount>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
    	IPage<TblLegalFrozenaccount> pageList = tblLegalFrozenaccountMapper.findListByPageInfo(page, frozenAccount, disputeid);
    	pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
    }

    @Override
    public TblLegalFrozenaccount findById(BigDecimal inforId) {
    	return tblLegalFrozenaccountMapper.findByInforid(inforId);
    }

    @Override
    public void saveFrozenAccount(TblLegalFrozenaccount frozen) {
    	tblLegalFrozenaccountMapper.saveFrozenAccount(frozen);
    }

    @Override
    public void updateModifyFrozenAccount(TblLegalFrozenaccount oldFrozen) {
    	tblLegalFrozenaccountMapper.updateModifyFrozenAccount(oldFrozen);
    }

    @Override
    public void removeFrozenAccount(BigDecimal inforId) {
    	this.tblLegalFrozenaccountMapper.removeFrozenAccount(inforId);
    }

    @Override
    public TblLegalFrozenaccount findInforid(BigDecimal inforId) {
    	return tblLegalFrozenaccountMapper.findInforid(inforId);
    }
}
