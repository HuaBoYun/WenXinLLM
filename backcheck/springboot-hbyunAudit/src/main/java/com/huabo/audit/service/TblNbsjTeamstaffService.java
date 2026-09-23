package com.huabo.audit.service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjTeamstaffEntity;

import java.util.List;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-19
*/
public interface TblNbsjTeamstaffService extends IService<TblNbsjTeamstaffEntity> {

    /**
    * 条件查询 封装QueryWrapper
    * @param model
    * @return
    */
    LambdaQueryWrapper<TblNbsjTeamstaffEntity> onSelectWhere(TblNbsjTeamstaffEntity model);

    /**
    *  封装保存方法
    * @param model
    * @return
    */
    boolean saveTblNbsjTeamstaff(TblNbsjTeamstaffEntity model);

    /**
    *  封装更新方法
    * @param model
    * @return
    */
    boolean updateTblNbsjTeamstaff(TblNbsjTeamstaffEntity model);

    boolean deleteByTeamId(Integer teamId);

    List<TblNbsjTeamstaffEntity> selectByTeamId(Integer teamId);
    

}
