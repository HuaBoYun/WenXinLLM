package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblYyUserQuery;

import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblYyUserQueryMapper extends BaseMapper<TblYyUserQuery> {


    @Select("SELECT * FROM TBL_YY_USER_QUERY WHERE RECORDID = #{recordId}")
    TblYyUserQuery selectByrecordId(Integer recordId);

}
