package com.huabo.audit.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblAuditSituationEntity;
import com.huabo.audit.oracle.entity.TblAuditSituationSubclassEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblAuditSituationMapper;
import com.huabo.audit.oracle.mapper.TblAuditSituationSubclassMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.TblAuditSituationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
public class TblAuditSituationServiceImpl extends ServiceImpl<TblAuditSituationMapper,TblAuditSituationEntity>  implements  TblAuditSituationService {

    @Resource
    private TblAuditSituationMapper tblAuditSituationMapper;
    @Resource
    private TblStaffMapper tblStaffMapper;
    @Resource
    private TblAuditSituationSubclassMapper tblAuditSituationSubclassMapper;

    @Override
    public  Page<TblAuditSituationEntity> querySituationListPage( Integer pageNumber, Integer pageSize)throws Exception {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<TblAuditSituationEntity> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNumber, pageSize);
        //查询创建人
        /*HashMap<BigDecimal, String> hashMapStaff = new HashMap<>();
        List<TblStaff> selectListStaff = tblStaffMapper.selectList(null);
        for (TblStaff staff : selectListStaff) {
            hashMapStaff.put(staff.getStaffid(),staff.getRealname());
        }*/
        QueryWrapper<TblAuditSituationEntity> wrapper = new QueryWrapper<>();
        //返回的数据集
        Page<TblAuditSituationEntity> res = tblAuditSituationMapper.selectPage(page, wrapper);
        for (TblAuditSituationEntity tblAuditSituationEntity:res.getRecords()) {
            TblStaff tblStaff = tblStaffMapper.selectById(tblAuditSituationEntity.getCreateStaffid());
            tblAuditSituationEntity.setCreateStaffName(tblStaff.getRealname());
        }
        return res;

    }

    @Override
    public void SituationAdd(TblAuditSituationEntity situationEntity, String json) {
        tblAuditSituationMapper.insert(situationEntity);
        if (!StringUtils.isEmpty(json)) {
            List<TblAuditSituationSubclassEntity> situationSubclassEntities = JSONObject.parseArray(json, TblAuditSituationSubclassEntity.class);
            for (TblAuditSituationSubclassEntity tblAuditSituationSubclassEntity : situationSubclassEntities) {
                tblAuditSituationSubclassEntity.setSituationId(situationEntity.getId().toString());
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
                tblAuditSituationSubclassEntity.setSituationId(situationEntity.getId().toString());
                tblAuditSituationSubclassMapper.updateById(tblAuditSituationSubclassEntity);
            }
        }

    }

    @Override
    public TblAuditSituationEntity situation_details(Integer situationId) {
        return tblAuditSituationMapper.selectById(situationId);
    }

    @Override
    public void situationDelete(Integer situationId) {
        tblAuditSituationMapper.deleteById(situationId);
        QueryWrapper<TblAuditSituationSubclassEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("SITUATIONID",situationId);
        tblAuditSituationSubclassMapper.delete(queryWrapper);
    }

    @Override
    public Integer situationValidationYear(Integer year) {
        QueryWrapper<TblAuditSituationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("SITUATIONYEAR",year);
        return tblAuditSituationMapper.selectCount(wrapper).intValue();
    }
}
