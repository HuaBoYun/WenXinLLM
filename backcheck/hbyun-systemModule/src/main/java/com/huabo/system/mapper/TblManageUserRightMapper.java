package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblManageUserRight;

import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-05-10
 */
public interface TblManageUserRightMapper extends BaseMapper<TblManageUserRight> {

    @Insert("insert into TBL_MANAGE_USER_RIGHT (RIGHTID,STAFFID) values (#{rightid},#{staffid})")
    void inserUserRight(TblManageUserRight userRight);
}
