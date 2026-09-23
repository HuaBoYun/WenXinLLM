package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblThirdPartyEvaluationEntity;
import com.huabo.audit.oracle.mapper.TblThirdPartyEvaluationMapper;
import com.huabo.audit.service.TblThirdPartyEvaluationService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TblThirdPartyEvaluationServiceImpl extends ServiceImpl<TblThirdPartyEvaluationMapper, TblThirdPartyEvaluationEntity> implements TblThirdPartyEvaluationService {

    @Autowired
    private TblThirdPartyEvaluationMapper tblThirdPartyEvaluationMapper;

    @Override
    public PageResult<TblThirdPartyEvaluationEntity> page(Integer pageNumber, Integer pageSize, TblThirdPartyEvaluationEntity entity) {
        //分页查询
        PageInfo<TblThirdPartyEvaluationEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.list(entity));
        return new PageResult<TblThirdPartyEvaluationEntity>().build(pageInfo);
    }


    /**
     * 按照条件查询列表
     * @param entity
     * @return
     */
    @Override
    public List<TblThirdPartyEvaluationEntity> list(TblThirdPartyEvaluationEntity entity) {
        //设置查询条件
        LambdaQueryWrapper<TblThirdPartyEvaluationEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(entity.getUnitName())) {
            //单位名称
            lambdaQueryWrapper.like(TblThirdPartyEvaluationEntity::getUnitName,entity.getUnitName());
        }
        if (StringUtils.isNotBlank(entity.getAuditProjectName())) {
            //审计项目名称
            lambdaQueryWrapper.like(TblThirdPartyEvaluationEntity::getAuditProjectName,entity.getAuditProjectName());
        }
        if (StringUtils.isNotBlank(entity.getAuditFirmName())) {
            //委托中介机构名称
            lambdaQueryWrapper.like(TblThirdPartyEvaluationEntity::getAuditFirmName,entity.getAuditFirmName());
        }
        if (StringUtils.isNotBlank(entity.getProcurementMethod())) {
            //采购方式
            lambdaQueryWrapper.eq(TblThirdPartyEvaluationEntity::getProcurementMethod,entity.getProcurementMethod());
        }


        if (Objects.nonNull(entity.getContractSigningDateStart()) && Objects.nonNull(entity.getContractSigningDateEnd())) {
            lambdaQueryWrapper.le(TblThirdPartyEvaluationEntity::getContractSigningDate,entity.getContractSigningDateEnd());
            lambdaQueryWrapper.ge(TblThirdPartyEvaluationEntity::getContractSigningDate,entity.getContractSigningDateStart());
        }

        //创建时间倒序
        lambdaQueryWrapper.orderByDesc(TblThirdPartyEvaluationEntity::getCreateTime);

        return tblThirdPartyEvaluationMapper.selectList(lambdaQueryWrapper);
    }
}
