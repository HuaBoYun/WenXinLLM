package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.entity.TblProcessSettingEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @description  
* @author   lyz
* @date 2022/4/14 14:14
*/

public interface TblProcessSettingMapper extends  BaseMapper<TblProcessSettingEntity>{
    @Select("SELECT  *  FROM  TBL_PROCESS_SETTING WHERE  module = #{busType}  order by settingId desc")
    List<TblProcessSettingEntity> selectByBusType(@Param("busType") String busType);
}




