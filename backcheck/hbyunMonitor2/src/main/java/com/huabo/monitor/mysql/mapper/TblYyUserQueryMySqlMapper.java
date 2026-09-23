package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblYyUserQueryMySql;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblYyUserQueryMySqlMapper extends BaseMapper<TblYyUserQueryMySql> {


    @Select("SELECT * FROM TBL_YY_USER_QUERY WHERE RECORDID = #{recordId}")
    TblYyUserQueryMySql selectByrecordId(Integer recordId);

}
