package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblNbsjPlanproject;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblNbsjPlanprojectMapper extends BaseMapper<TblNbsjPlanproject> {

    @Select("SELECT * FROM TBL_NBSJ_PLANPROJECT WHERE PLANPROJECTID =#{planid} ")
    List<TblNbsjPlanproject> findByPlanId(String planid);
}
