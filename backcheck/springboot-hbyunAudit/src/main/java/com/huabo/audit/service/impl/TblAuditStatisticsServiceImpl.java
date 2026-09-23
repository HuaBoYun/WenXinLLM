package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblAuditStatisticsEntity;
import com.huabo.audit.oracle.mapper.TblAuditStatisticsMapper;
import com.huabo.audit.service.TblAuditStatisticsService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TblAuditStatisticsServiceImpl extends ServiceImpl<TblAuditStatisticsMapper, TblAuditStatisticsEntity> implements TblAuditStatisticsService {

    @Autowired
    private TblAuditStatisticsMapper tblAuditStatisticsMapper;

    @Override
    public PageResult<TblAuditStatisticsEntity> page(Integer pageNumber, Integer pageSize, TblAuditStatisticsEntity entity) {
        //分页查询
        PageInfo<TblAuditStatisticsEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.list(entity));
        return new PageResult<TblAuditStatisticsEntity>().build(pageInfo);
    }


    /**
     * 按照条件查询列表
     * @param entity
     * @return
     */
    @Override
    public List<TblAuditStatisticsEntity> list(TblAuditStatisticsEntity entity) {
        //设置查询条件
        LambdaQueryWrapper<TblAuditStatisticsEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(entity.getUnitName())) {
            //单位名称
            lambdaQueryWrapper.like(TblAuditStatisticsEntity::getUnitName,entity.getUnitName());
        }
        if (StringUtils.isNotBlank(entity.getAuditDepartmentName())) {
            //审计机构名称
            lambdaQueryWrapper.like(TblAuditStatisticsEntity::getAuditDepartmentName,entity.getAuditDepartmentName());
        }
        if (StringUtils.isNotBlank(entity.getHasBoardOfDirectors())) {
            //是否设立董事会
            lambdaQueryWrapper.eq(TblAuditStatisticsEntity::getHasBoardOfDirectors,entity.getHasBoardOfDirectors());
        }
        if (StringUtils.isNotBlank(entity.getHasAuditCommittee())) {
            //是否设立审计委员会
            lambdaQueryWrapper.eq(TblAuditStatisticsEntity::getHasAuditCommittee,entity.getHasAuditCommittee());
        }
        //创建时间倒序
        lambdaQueryWrapper.orderByDesc(TblAuditStatisticsEntity::getCreateTime);

        return tblAuditStatisticsMapper.selectList(lambdaQueryWrapper);
    }



}
