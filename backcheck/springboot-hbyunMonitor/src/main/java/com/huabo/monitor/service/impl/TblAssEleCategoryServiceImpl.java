package com.huabo.monitor.service.impl;

import com.huabo.monitor.oracle.entity.TblAssEleCategory;
import com.huabo.monitor.oracle.mapper.TblAssEleCategoryMapper;
import com.huabo.monitor.service.TblAssEleCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service("TblAssEleCategoryService")
public class TblAssEleCategoryServiceImpl implements TblAssEleCategoryService {

    public TblAssEleCategoryMapper getTblAssEleCategoryMapper() {
        return tblAssEleCategoryMapper;
    }

    public void setTblAssEleCategoryMapper(TblAssEleCategoryMapper tblAssEleCategoryMapper) {
        this.tblAssEleCategoryMapper = tblAssEleCategoryMapper;
    }

    private TblAssEleCategoryMapper tblAssEleCategoryMapper;

    @Override
    public void save(TblAssEleCategory assEleCategory) {
        this.tblAssEleCategoryMapper.save(assEleCategory);
    }

    @Override
    public void saveList(List<TblAssEleCategory> list) {
        for (TblAssEleCategory tblAssEleCategory : list) {
            this.tblAssEleCategoryMapper.save(tblAssEleCategory);
        }
    }

    @Override
    public void update(TblAssEleCategory assEleCategory) {
        this.tblAssEleCategoryMapper.update(assEleCategory);
    }

    @Override
    public void delete(TblAssEleCategory assEleCategory) {
        this.tblAssEleCategoryMapper.delete(assEleCategory);
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
            this.tblAssEleCategoryMapper.delete(tblAssEleCategory);
        }
    }

    @Override
    public List<TblAssEleCategory> getScore(BigDecimal tempId, BigDecimal cateId) {
        return this.tblAssEleCategoryMapper.getScore(tempId, cateId);
    }

}
