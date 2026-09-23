package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblProcessSettingEntity;

/**
* @description  
* @author   lyz
* @date 2022/4/14 14:14
*/

public interface TblProcessSettingMapper  extends  BaseMapper<TblProcessSettingEntity>{
    @Select("SELECT  *  FROM  TBL_PROCESS_SETTING WHERE  module = #{busType}  order by settingId desc")
    List<TblProcessSettingEntity> selectByBusType(@Param("busType") String busType);
    
    @Select("SELECT  *  FROM  TBL_PROCESS_SETTING WHERE  REMARK = #{busType} and COMPANYID=#{orgid}  order by settingId desc")
    List<TblProcessSettingEntity> selectByOrgid(@Param("busType") String busType,@Param("orgid") Integer orgid);
}




