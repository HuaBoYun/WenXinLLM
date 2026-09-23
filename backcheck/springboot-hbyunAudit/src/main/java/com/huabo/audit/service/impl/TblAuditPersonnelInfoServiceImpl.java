package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblAuditPersonnelInfoEntity;
import com.huabo.audit.oracle.mapper.TblAuditPersonnelInfoMapper;
import com.huabo.audit.service.TblAuditPersonnelInfoService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class TblAuditPersonnelInfoServiceImpl extends ServiceImpl<TblAuditPersonnelInfoMapper, TblAuditPersonnelInfoEntity> implements TblAuditPersonnelInfoService {

    @Autowired
    private TblAuditPersonnelInfoMapper tblAuditPersonnelInfoMapper;

    @Override
    public PageResult<TblAuditPersonnelInfoEntity> page(Integer pageNumber, Integer pageSize, TblAuditPersonnelInfoEntity entity) {
        //分页查询
        PageInfo<TblAuditPersonnelInfoEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.list(entity));
        return new PageResult<TblAuditPersonnelInfoEntity>().build(pageInfo);
    }


    /**
     * 按照条件查询列表
     * @param entity
     * @return
     */
    @Override
    public List<TblAuditPersonnelInfoEntity> list(TblAuditPersonnelInfoEntity entity) {
        //设置查询条件
        LambdaQueryWrapper<TblAuditPersonnelInfoEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(entity.getUnitName())) {
            //单位名称
            lambdaQueryWrapper.like(TblAuditPersonnelInfoEntity::getUnitName,entity.getUnitName());
        }
        if (StringUtils.isNotBlank(entity.getName())) {
            //姓名
            lambdaQueryWrapper.like(TblAuditPersonnelInfoEntity::getName,entity.getName());
        }
        if (StringUtils.isNotBlank(entity.getNativePlace())) {
            //籍贯
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getNativePlace,entity.getNativePlace());
        }
        if (StringUtils.isNotBlank(entity.getEthnicity())) {
            //民族
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getEthnicity,entity.getEthnicity());
        }
        if (StringUtils.isNotBlank(entity.getPoliticalStatus())) {
            //政治面貌
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getPoliticalStatus,entity.getPoliticalStatus());
        }
        if (StringUtils.isNotBlank(entity.getPosition())) {
            //职务
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getPosition,entity.getPosition());
        }
        if (StringUtils.isNotBlank(entity.getProfessionalQualification())) {
            //职业资格
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getProfessionalQualification,entity.getProfessionalQualification());
        }
        if (StringUtils.isNotBlank(entity.getIsCia())) {
            //是否CIA
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getIsCia,entity.getIsCia());
        }
        if (StringUtils.isNotBlank(entity.getIsPromotedTransfer())) {
            //是否提拔交流人员
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getIsPromotedTransfer,entity.getIsPromotedTransfer());
        }
        if (StringUtils.isNotBlank(entity.getIsHorizontalTransfer())) {
            //是否平职交流人员
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getIsHorizontalTransfer,entity.getIsHorizontalTransfer());
        }
        if (StringUtils.isNotBlank(entity.getEducation())) {
            //学历
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getEducation,entity.getEducation());
        }
        if (StringUtils.isNotBlank(entity.getHighestEducationMajor())) {
            //最高学历
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getHighestEducationMajor,entity.getHighestEducationMajor());
        }
        if (StringUtils.isNotBlank(entity.getOtherEducationMajors())) {
            //其他学历专业
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getOtherEducationMajors,entity.getOtherEducationMajors());
        }
        if (StringUtils.isNotBlank(entity.getUniversity())) {
            //毕业院校
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getUniversity,entity.getUniversity());
        }
        if (StringUtils.isNotBlank(entity.getMobilePhone())) {
            //手机号码
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getMobilePhone,entity.getMobilePhone());
        }
        if (StringUtils.isNotBlank(entity.getIsSpecialInvitedAuditor())) {
            //是否特邀审计员
            lambdaQueryWrapper.eq(TblAuditPersonnelInfoEntity::getIsSpecialInvitedAuditor,entity.getIsSpecialInvitedAuditor());
        }
        if (Objects.nonNull(entity.getGraduationDateStart()) && Objects.nonNull(entity.getGraduationDateEnd())) {
            lambdaQueryWrapper.le(TblAuditPersonnelInfoEntity::getGraduationDate,entity.getGraduationDateEnd());
            lambdaQueryWrapper.ge(TblAuditPersonnelInfoEntity::getGraduationDate,entity.getGraduationDateStart());
        }
        if (Objects.nonNull(entity.getJoinSystemDateStart()) && Objects.nonNull(entity.getJoinSystemDateEnd())) {
            lambdaQueryWrapper.le(TblAuditPersonnelInfoEntity::getJoinSystemDate,entity.getJoinSystemDateEnd());
            lambdaQueryWrapper.ge(TblAuditPersonnelInfoEntity::getJoinSystemDate,entity.getJoinSystemDateStart());
        }
        if (Objects.nonNull(entity.getJoinUnitDateStart()) && Objects.nonNull(entity.getJoinUnitDateEnd())) {
            lambdaQueryWrapper.le(TblAuditPersonnelInfoEntity::getJoinUnitDate,entity.getJoinUnitDateEnd());
            lambdaQueryWrapper.ge(TblAuditPersonnelInfoEntity::getJoinUnitDate,entity.getJoinUnitDateStart());
        }
        if (Objects.nonNull(entity.getWorkStartDateStart()) && Objects.nonNull(entity.getWorkStartDateEnd())) {
            lambdaQueryWrapper.le(TblAuditPersonnelInfoEntity::getWorkStartDate,entity.getWorkStartDateEnd());
            lambdaQueryWrapper.ge(TblAuditPersonnelInfoEntity::getWorkStartDate,entity.getWorkStartDateStart());
        }
        //创建时间倒序
        lambdaQueryWrapper.orderByDesc(TblAuditPersonnelInfoEntity::getCreateTime);

        return tblAuditPersonnelInfoMapper.selectList(lambdaQueryWrapper);
    }
}
