package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblMobileSetting;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 移动设置Mapper
 */
public interface TblMobileSettingMapper extends BaseMapper<TblMobileSetting> {

    /**
     * 根据设置编码查询
     */
    TblMobileSetting selectBySettingCode(@Param("settingCode") String settingCode);

    /**
     * 根据设置类型查询
     */
    List<TblMobileSetting> selectBySettingType(@Param("settingType") String settingType);

    /**
     * 根据平台查询
     */
    List<TblMobileSetting> selectByPlatform(@Param("platform") String platform);

    /**
     * 根据版本查询
     */
    List<TblMobileSetting> selectByVersion(@Param("version") String version);

    /**
     * 根据是否启用查询
     */
    List<TblMobileSetting> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);
}
