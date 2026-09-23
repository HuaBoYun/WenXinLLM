package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctInvestNonMainBiz;
import com.huabo.cybermonitor.util.PageResult;
import java.util.List;
import java.util.Map;

public interface IGzctInvestNonMainBizService extends IService<GzctInvestNonMainBiz> {
    PageResult<GzctInvestNonMainBiz> selectByPage(Map<String, Object> params);
    Map<String, Object> getStats(String companyId);
    List<Map<String, Object>> getTrend(String companyId, Integer year);
    boolean addRecord(GzctInvestNonMainBiz record);
    boolean updateRecord(GzctInvestNonMainBiz record);
    boolean deleteRecord(String id);
}
