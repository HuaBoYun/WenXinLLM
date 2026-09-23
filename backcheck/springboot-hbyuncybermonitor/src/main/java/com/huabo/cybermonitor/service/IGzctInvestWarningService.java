package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctInvestWarning;
import com.huabo.cybermonitor.util.PageResult;
import java.util.Map;

public interface IGzctInvestWarningService extends IService<GzctInvestWarning> {
    PageResult<GzctInvestWarning> selectByPage(Map<String, Object> params);
    boolean dispatch(String warningId);
    boolean handle(String warningId, String handleResult, String handleUser);
}
