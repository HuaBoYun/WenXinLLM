package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblSystemOrgRightMySql;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-05-22
 */
public interface TblSystemOrgRightMySqlMapper extends BaseMapper<TblSystemOrgRightMySql> {

    @UpdateProvider(method = "updateSystemSettingRight", type = TblSystemRightMapperSqlMySqlConfig.class)
    void updateSystemSettingRight(TblSystemOrgRightMySql orgRight) throws Exception;

}
