package com.huabo.compliance.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huabo.compliance.entity.TblAssEleCategory;
import com.huabo.compliance.entity.TblAssesselement;
import com.huabo.compliance.mapper.TblAssEleCategoryMapper;
import com.huabo.compliance.service.TblAssEleCategoryService;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service("TblAssEleCategoryService")
public class TblAsseleCategoryServiceImpl implements TblAssEleCategoryService {

    @Resource
    TblAssEleCategoryMapper tblAssEleCategoryMapper;

    @Override
    public void save(TblAssEleCategory assEleCategory) throws Exception{
        this.tblAssEleCategoryMapper.insertEle(assEleCategory);
    }

    @Override
    public void saveList(List<TblAssEleCategory> list) throws Exception{
        for (TblAssEleCategory tblAssEleCategory : list) {
            this.tblAssEleCategoryMapper.insertEle(tblAssEleCategory);
        }
    }

    @Override
    public void update(TblAssEleCategory assEleCategory) throws Exception{
        this.tblAssEleCategoryMapper.updateEle(assEleCategory);
    }

    @Override
    public void delete(TblAssEleCategory assEleCategory) {
        this.tblAssEleCategoryMapper.deleteById(assEleCategory.getElementcategoryid());
    }

    @Override
    public TblAssEleCategory get(BigDecimal id) {
        return this.tblAssEleCategoryMapper.get(id);
    }

    @Override
    public List<TblAssEleCategory> getAssesscategoryByNodeId(BigDecimal id) {
        return this.tblAssEleCategoryMapper.getAssesscategoryByNodeId(id);
    }

    @Override
    public List<TblAssEleCategory> getAssesscategoryBytmplId(BigDecimal id) {
        return this.tblAssEleCategoryMapper.getAssesscategoryBytmplId(id);
    }

    @Override
    public List<TblAssEleCategory> getAssesscategoryByMuBanId(BigDecimal id) {
        return this.tblAssEleCategoryMapper.getAssesscategoryByMuBanId(id);
    }

    @Override
    @Transactional
    public void delete(List<TblAssEleCategory> assEleCategorys) {
        for (TblAssEleCategory tblAssEleCategory : assEleCategorys) {
            this.tblAssEleCategoryMapper.deleteById(tblAssEleCategory.getAsseleid());
        }
    }

    @Override
    public List<TblAssEleCategory> getScore(BigDecimal tempId, BigDecimal cateId) {
        return this.tblAssEleCategoryMapper.getScore(tempId, cateId);
    }

	@Override
	public TblAssesselement getAssesselementBycatid(BigDecimal asseleid) {
		// TODO Auto-generated method stub
		return this.tblAssEleCategoryMapper.getAssesselementBycatid(asseleid);
	}

}
