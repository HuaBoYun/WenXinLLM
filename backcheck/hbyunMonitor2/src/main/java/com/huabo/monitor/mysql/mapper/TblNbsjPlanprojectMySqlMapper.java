package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblNbsjPlanprojectMySql;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblNbsjPlanprojectMySqlMapper extends BaseMapper<TblNbsjPlanprojectMySql> {

    @Select("SELECT * FROM TBL_NBSJ_PLANPROJECT WHERE PLANPROJECTID =#{planid} ")
    List<TblNbsjPlanprojectMySql> findByPlanId(String planid);
}
