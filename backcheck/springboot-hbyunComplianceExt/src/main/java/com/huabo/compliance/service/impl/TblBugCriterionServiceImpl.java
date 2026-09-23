package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.compliance.entity.TblBugCriterionEntity;
import com.huabo.compliance.mapper.TblBugCriterionMapper;
import com.huabo.compliance.service.TblBugCriterionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
public class TblBugCriterionServiceImpl extends ServiceImpl<TblBugCriterionMapper, TblBugCriterionEntity> implements TblBugCriterionService {
    public static final int DEFAULT_SIZE = 20;

    @Resource
    TblBugCriterionMapper tblBugCriterionMapper;

    @Override
    public TblBugCriterionEntity findByTblBugCriterion(String bugid) {
        TblBugCriterionEntity tblBugCriterionEntity = baseMapper.findByTblBugCriterion(bugid);
        return tblBugCriterionEntity;
    }

    @Override
    public IPage<TblBugCriterionEntity> findAll(Integer pageNumber, String orgId, Integer pageSize) {
        Page<TblBugCriterionEntity> page = new Page<>(pageNumber, pageSize);
        String sql = " SELECT * FROM TBL_BUGCRITERION where 1=1 and BUGCRIID in ( SELECT max(BUGCRIID) FROM TBL_BUGCRITERION where ORGID=" + orgId + " GROUP BY BUGCRILEVEL )";
        return tblBugCriterionMapper.getSqlPage(page, sql);
    }
     
    @Override
    public List<TblBugCriterionEntity> findAll() {
        List<TblBugCriterionEntity> list = baseMapper.findAll("");
        return list;
    }

    @Override
    public List<TblBugCriterionEntity> findAll(String orgid) {
        List<TblBugCriterionEntity> list = baseMapper.findAll(orgid);
        return list;
    }

    @Override
    public TblBugCriterionEntity findByid(String id) {
        TblBugCriterionEntity tblBugCriterionEntity = baseMapper.findByid(id);
        return tblBugCriterionEntity;
    }

    @Override
    public List<TblBugCriterionEntity> fingByLevel(String orgid, String level) {
        List<TblBugCriterionEntity> list = baseMapper.fingByLevel(orgid, level);
        return list;
    }

    @Override
    public void saveEntity(TblBugCriterionEntity entity) {
        baseMapper.insert(entity);
    }

    @Override
    public void update(TblBugCriterionEntity entity) {
        tblBugCriterionMapper.updateById(entity);
    }

    @Override
    public void del(BigDecimal bugcriid) {
        tblBugCriterionMapper.deleteByCriterionId(bugcriid);
    }

	
}
