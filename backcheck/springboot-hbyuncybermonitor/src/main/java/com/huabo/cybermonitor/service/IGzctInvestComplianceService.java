package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctInvestCompliance;
import com.huabo.cybermonitor.util.PageResult;
import java.util.Map;

public interface IGzctInvestComplianceService extends IService<GzctInvestCompliance> {
    PageResult<GzctInvestCompliance> selectByPage(Map<String, Object> params);
    Map<String, Object> getChainDetail(String complianceId);
    boolean dispatch(String complianceId);
    boolean addCompliance(GzctInvestCompliance compliance);
    boolean updateCompliance(GzctInvestCompliance compliance);
    boolean deleteCompliance(String complianceId);
}
