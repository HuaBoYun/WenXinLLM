package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.TblIndicatorControlConfig;

import java.util.List;

public interface IIndicatorControlConfigService extends IService<TblIndicatorControlConfig> {

    IPage<TblIndicatorControlConfig> getConfigPage(int pageNum, int pageSize);

    void saveConfig(TblIndicatorControlConfig config, String currentUser);

    List<TblIndicatorControlConfig> getEnabledConfigs(String rightId, String operationType);

    List<TblIndicatorControlConfig> getEnabledConfigsByExternalKey(String externalPageKey, String operationType);
}
