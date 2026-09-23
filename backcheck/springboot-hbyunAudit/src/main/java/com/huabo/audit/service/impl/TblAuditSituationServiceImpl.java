package com.huabo.audit.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAuditSituationEntity;
import com.huabo.audit.oracle.entity.TblAuditSituationSubclassEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblAuditSituationMapper;
import com.huabo.audit.oracle.mapper.TblAuditSituationSubclassMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.TblAuditSituationService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TblAuditSituationServiceImpl extends ServiceImpl<TblAuditSituationMapper,TblAuditSituationEntity>  implements  TblAuditSituationService {

    @Resource
    private TblAuditSituationMapper tblAuditSituationMapper;
    @Resource
    private TblStaffMapper tblStaffMapper;
    @Resource
    private TblAuditSituationSubclassMapper tblAuditSituationSubclassMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public  PageResult<TblAuditSituationEntity> querySituationListPage( String token, Integer pageNumber, Integer pageSize,String year)throws Exception {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<TblAuditSituationEntity> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNumber, pageSize);
        TblStaffUtil loginStaff = userProvider.get();
       
        //返回的数据集
        //Page<TblAuditSituationEntity> res = tblAuditSituationMapper.selectPage(page, wrapper);
        com.github.pagehelper.PageInfo<TblAuditSituationEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> tblAuditSituationMapper.selectPageInfoList(page, year ,loginStaff));
        
        
        PageResult<TblAuditSituationEntity> build = new PageResult<TblAuditSituationEntity>().build(pageInfo);
        for (TblAuditSituationEntity tblAuditSituationEntity:pageInfo.getList()) {
            TblStaff tblStaff = tblStaffMapper.selectById(tblAuditSituationEntity.getCreateStaffid());
            tblAuditSituationEntity.setCreateStaffName(tblStaff.getRealname());
        }
        return build;

    }

    @Override
    public void SituationAdd(TblAuditSituationEntity situationEntity, String json) {
        situationEntity.setId(RandomUtil.uuBigDecimalId());
        tblAuditSituationMapper.insert(situationEntity);
        if (!StringUtils.isEmpty(json)) {
            List<TblAuditSituationSubclassEntity> situationSubclassEntities = JSONObject.parseArray(json, TblAuditSituationSubclassEntity.class);
            for (TblAuditSituationSubclassEntity tblAuditSituationSubclassEntity : situationSubclassEntities) {
                tblAuditSituationSubclassEntity.setSituationId(situationEntity.getId());
                tblAuditSituationSubclassEntity.setId(RandomUtil.uuBigDecimalId());
                tblAuditSituationSubclassMapper.insert(tblAuditSituationSubclassEntity);
            }

        }
    }

    @Override
    public void SituationUpdate(TblAuditSituationEntity situationEntity, String json) {
        tblAuditSituationMapper.updateById(situationEntity);
        if (!StringUtils.isEmpty(json)){
            List<TblAuditSituationSubclassEntity> situationSubclassEntities = JSONObject.parseArray(json, TblAuditSituationSubclassEntity.class);
            for (TblAuditSituationSubclassEntity tblAuditSituationSubclassEntity:situationSubclassEntities) {
                tblAuditSituationSubclassEntity.setSituationId(situationEntity.getId());
                tblAuditSituationSubclassMapper.updateById(tblAuditSituationSubclassEntity);
            }
        }

    }

    @Override
    public TblAuditSituationEntity situation_details(BigDecimal situationId) {
        return tblAuditSituationMapper.selectById(situationId);
    }

    @Override
    public void situationDelete(BigDecimal situationId) {
        tblAuditSituationMapper.deleteById(situationId);
        QueryWrapper<TblAuditSituationSubclassEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SITUATIONID",situationId);
        tblAuditSituationSubclassMapper.delete(queryWrapper);
    }

    @Override
    public Integer situationValidationYear(Integer year) {
        QueryWrapper<TblAuditSituationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("SITUATIONYEAR",year);
        return tblAuditSituationMapper.selectCount(wrapper);
    }
}
