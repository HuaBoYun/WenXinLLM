package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblMobileSettingTemplate;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 配置模板Mapper
 */
public interface TblMobileSettingTemplateMapper extends BaseMapper<TblMobileSettingTemplate> {

    /**
     * 根据模板名称查询
     */
    List<TblMobileSettingTemplate> selectByTemplateName(@Param("templateName") String templateName);

    /**
     * 根据平台查询
     */
    List<TblMobileSettingTemplate> selectByPlatform(@Param("platform") String platform);

    /**
     * 根据是否启用查询
     */
    List<TblMobileSettingTemplate> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);
}
