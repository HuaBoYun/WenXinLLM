package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblManageUserRightMySql;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-05-10
 */
public interface TblManageUserRightMySqlMapper extends BaseMapper<TblManageUserRightMySql> {

    @Insert("insert into TBL_MANAGE_USER_RIGHT (RIGHTID,STAFFID) values (#{rightid},#{staffid})")
    void inserUserRight(TblManageUserRightMySql userRight);
}
