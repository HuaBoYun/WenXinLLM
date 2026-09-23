package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblAuditSituationSubclassEntity;
import com.huabo.audit.oracle.mapper.TblAuditSituationSubclassMapper;
import com.huabo.audit.service.TblAuditSituationSubclassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TblAuditSituationSubclassServiceImpl extends ServiceImpl<TblAuditSituationSubclassMapper, TblAuditSituationSubclassEntity> implements TblAuditSituationSubclassService {

    @Resource
    private TblAuditSituationSubclassMapper tblAuditSituationSubclassMapper;

    @Override
    public List<TblAuditSituationSubclassEntity> situationSubclassDetails(BigDecimal situationId) {
        QueryWrapper<TblAuditSituationSubclassEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SITUATIONID",situationId);
       return tblAuditSituationSubclassMapper.selectList(queryWrapper);
    }
}
