package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblMobileSettingVersion;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 配置版本Mapper
 */
public interface TblMobileSettingVersionMapper extends BaseMapper<TblMobileSettingVersion> {

    /**
     * 根据设置ID查询
     */
    List<TblMobileSettingVersion> selectBySettingId(@Param("settingId") String settingId);

    /**
     * 根据版本号查询
     */
    List<TblMobileSettingVersion> selectByVersionNumber(@Param("versionNumber") String versionNumber);

    /**
     * 根据设置ID查询最新版本
     */
    TblMobileSettingVersion selectLatestBySettingId(@Param("settingId") String settingId);
}
