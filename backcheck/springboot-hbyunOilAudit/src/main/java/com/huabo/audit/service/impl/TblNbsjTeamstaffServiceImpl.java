package com.huabo.audit.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.common.ResultCode;
import com.huabo.audit.exception.CommercialException;
import com.huabo.audit.oracle.entity.TblNbsjTeamstaffEntity;
import com.huabo.audit.oracle.mapper.TblNbsjTeamstaffMapper;
import com.huabo.audit.service.TblNbsjTeamstaffService;

/**
* 描述:实现类
* @author: ziyao
* @date: 2022-04-19
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjTeamstaffServiceImpl extends ServiceImpl<TblNbsjTeamstaffMapper, TblNbsjTeamstaffEntity> implements TblNbsjTeamstaffService {
    @Autowired
    private TblNbsjTeamstaffMapper tblNbsjTeamstaffMapper;

    /**
    * 重写删除方法 为逻辑删除
    * @param id
    * @return
    */
    @Override
    public boolean removeById(Serializable id) {
        return tblNbsjTeamstaffMapper.deleteById(id) > 0;
    }

    /**
    * 条件查询 封装QueryWrapper
    * @param model
    * @return
    */
    @Override
    public LambdaQueryWrapper<TblNbsjTeamstaffEntity> onSelectWhere(TblNbsjTeamstaffEntity model) {
        LambdaQueryWrapper<TblNbsjTeamstaffEntity>  queryWrapper=new QueryWrapper<TblNbsjTeamstaffEntity>().lambda();
        if (model == null) {
            return queryWrapper;
        }

        queryWrapper
                .eq(model.getId()!=null && model.getId()!=0,TblNbsjTeamstaffEntity::getId, model.getId())
                .eq(model.getTeamid()!=null,TblNbsjTeamstaffEntity::getTeamid, model.getTeamid())
                .eq(model.getStaffid()!=null,TblNbsjTeamstaffEntity::getStaffid, model.getStaffid())
                .eq(model.getStafftype()!=null,TblNbsjTeamstaffEntity::getStafftype, model.getStafftype())
        ;

        return queryWrapper;
    }

    /**
    *  封装保存方法
    * @param model
    * @return
    */
    @Override
    public boolean saveTblNbsjTeamstaff(TblNbsjTeamstaffEntity model) {
        //在保存和更新之前的操作 同步字段id、name
        beforSaveandUpdate(model);

        return save(model);
    }

    /**
    *  封装更新方法
    * @param model
    * @return
    */
    @Override
    public boolean updateTblNbsjTeamstaff(TblNbsjTeamstaffEntity model) {
        if(model.getId()==null || model.getId()==0){
            throw new CommercialException(ResultCode.BIZ_ERROR,"Id不能为空！");
        }
        //在保存和更新之前的操作 同步字段id、name
        beforSaveandUpdate(model);

        return updateById(model);
    }

    @Override
    public boolean deleteByTeamId(Integer teamId) {
        return tblNbsjTeamstaffMapper.deleteByTeamId(teamId)>0;
    }

    @Override
    public List<TblNbsjTeamstaffEntity> selectByTeamId(Integer teamId) {
        LambdaQueryWrapper<TblNbsjTeamstaffEntity>  queryWrapper=new QueryWrapper<TblNbsjTeamstaffEntity>().lambda();
        queryWrapper
                .eq(teamId!=null,TblNbsjTeamstaffEntity::getTeamid, teamId);
        List<TblNbsjTeamstaffEntity> list = list(queryWrapper);
        return list;
    }

    /**
    * @description: 在保存和更新之前的操作 同步字段id、name
    * @param model:
    * @return: void
    * @author: ziyao
    * @date: 2022-04-19
    */
    private void beforSaveandUpdate(TblNbsjTeamstaffEntity model){

    }

}
