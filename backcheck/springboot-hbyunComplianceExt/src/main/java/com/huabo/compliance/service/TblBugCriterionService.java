package com.huabo.compliance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblBugCriterionEntity;


import java.math.BigDecimal;
import java.util.List;

public interface TblBugCriterionService {
    TblBugCriterionEntity findByTblBugCriterion(String bugid);

    IPage<TblBugCriterionEntity> findAll(Integer pageNumber, String orgId, Integer pageSize);

    List<TblBugCriterionEntity> findAll();

    List<TblBugCriterionEntity> findAll(String orgid);

    TblBugCriterionEntity findByid(String id);

    List<TblBugCriterionEntity> fingByLevel(String orgid, String level);

    void saveEntity(TblBugCriterionEntity entity);

    void update(TblBugCriterionEntity entity);

    void del(BigDecimal bugcriid);
    

}
