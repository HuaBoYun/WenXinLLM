package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblMobileSettingSyncLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 配置同步日志Mapper
 */
public interface TblMobileSettingSyncLogMapper extends BaseMapper<TblMobileSettingSyncLog> {

    /**
     * 根据设置ID查询
     */
    List<TblMobileSettingSyncLog> selectBySettingId(@Param("settingId") String settingId);

    /**
     * 根据同步状态查询
     */
    List<TblMobileSettingSyncLog> selectBySyncStatus(@Param("syncStatus") String syncStatus);

    /**
     * 根据平台查询
     */
    List<TblMobileSettingSyncLog> selectByPlatform(@Param("platform") String platform);

    /**
     * 根据同步人查询
     */
    List<TblMobileSettingSyncLog> selectBySyncUser(@Param("syncUser") String syncUser);
}
