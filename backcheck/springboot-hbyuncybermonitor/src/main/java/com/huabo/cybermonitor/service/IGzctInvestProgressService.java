package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctInvestProgress;
import com.huabo.cybermonitor.util.PageResult;
import java.util.Map;

public interface IGzctInvestProgressService extends IService<GzctInvestProgress> {
    PageResult<GzctInvestProgress> selectByPage(Map<String, Object> params);
    boolean addProgress(GzctInvestProgress progress);
    boolean updateProgress(GzctInvestProgress progress);
    boolean deleteProgress(String id);
}
