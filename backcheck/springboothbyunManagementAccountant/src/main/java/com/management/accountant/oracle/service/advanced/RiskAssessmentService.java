package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.RiskAssessment;

import java.util.List;
import java.util.Map;

public interface RiskAssessmentService {
    List<RiskAssessment> selectList(Map<String, Object> params);
    Page<RiskAssessment> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);
    RiskAssessment selectById(String assessmentId);
    boolean insert(RiskAssessment assessment);
    boolean update(RiskAssessment assessment);
    boolean deleteById(String assessmentId);
    boolean copyAssessment(String assessmentId);
    Map<String, Object> getStats();
}
