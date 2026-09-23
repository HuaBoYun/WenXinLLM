package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcPartnerArchive;
import com.global.treasurer.entity.TcPartnerRiskAssessment;
import com.global.treasurer.exception.ServiceException;
import com.global.treasurer.mapper.TcPartnerArchiveMapper;
import com.global.treasurer.mapper.TcPartnerRiskAssessmentMapper;
import com.global.treasurer.service.TcPartnerRiskAssessmentService;
import com.hbfk.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 合作伙伴风险评估服务实现类
 * 
 * @author AI Assistant
 * @date 2026-03-05
 */
@Service
@Transactional
public class TcPartnerRiskAssessmentServiceImpl implements TcPartnerRiskAssessmentService {

    @Autowired
    private TcPartnerRiskAssessmentMapper assessmentMapper;

    @Autowired
    private TcPartnerArchiveMapper partnerArchiveMapper;

    @Override
    public PageInfo<TcPartnerRiskAssessment> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<TcPartnerRiskAssessment> list = assessmentMapper.selectByParams(params);
        return new PageInfo<>(list);
    }

    @Override
    public TcPartnerRiskAssessment getById(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new ServiceException("风险评估ID不能为空");
        }
        return assessmentMapper.selectById(id);
    }

    @Override
    public boolean save(TcPartnerRiskAssessment assessment) {
        if (assessment == null) {
            throw new ServiceException("风险评估信息不能为空");
        }
        if (StringUtil.isEmpty(assessment.getPartnerId())) {
            throw new ServiceException("合作伙伴ID不能为空");
        }

        // 生成ID - 使用数据库序列或UUID
        if (StringUtil.isEmpty(assessment.getId())) {
            assessment.setId(String.valueOf(System.currentTimeMillis()));
        }
        // 设置默认值
        if (assessment.getIsEnabled() == null) {
            assessment.setIsEnabled(1);
        }
        if (assessment.getVersionNo() == null) {
            assessment.setVersionNo(0);
        }
        
        Date now = new Date();
        assessment.setCreateTime(now);
        assessment.setUpdateTime(now);
        assessment.setLastAssessmentDate(now);
        
        return assessmentMapper.insert(assessment) > 0;
    }

    @Override
    public boolean update(TcPartnerRiskAssessment assessment) {
        if (assessment == null || StringUtil.isEmpty(assessment.getId())) {
            throw new ServiceException("风险评估ID不能为空");
        }
        
        assessment.setUpdateTime(new Date());
        return assessmentMapper.updateById(assessment) > 0;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new ServiceException("风险评估ID不能为空");
        }
        return assessmentMapper.deleteById(id) > 0;
    }

    @Override
    public boolean batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("风险评估ID列表不能为空");
        }
        for (String id : ids) {
            delete(id);
        }
        return true;
    }

    @Override
    public List<TcPartnerRiskAssessment> getByPartnerId(String partnerId) {
        return assessmentMapper.selectByPartnerId(partnerId);
    }

    @Override
    public List<TcPartnerRiskAssessment> getByRiskLevel(String riskLevel) {
        return assessmentMapper.selectByRiskLevel(riskLevel);
    }

    @Override
    public List<TcPartnerRiskAssessment> getByRiskStatus(String riskStatus) {
        return assessmentMapper.selectByRiskStatus(riskStatus);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> dbResult = assessmentMapper.selectStatistics();
        // 达梦数据库返回的字段名是大写的，需要转换为小驼峰
        Map<String, Object> result = new HashMap<>();
        result.put("totalAssessments", dbResult.get("TOTALASSESSMENTS"));
        result.put("highRiskCount", dbResult.get("HIGHRISKCOUNT"));
        result.put("mediumRiskCount", dbResult.get("MEDIUMRISKCOUNT"));
        result.put("lowRiskCount", dbResult.get("LOWRISKCOUNT"));
        return result;
    }

    @Override
    public List<Map<String, Object>> getRiskLevels() {
        List<Map<String, Object>> levels = new ArrayList<>();
        levels.add(createOption("AAA", "AAA级"));
        levels.add(createOption("AA", "AA级"));
        levels.add(createOption("A", "A级"));
        levels.add(createOption("BBB", "BBB级"));
        levels.add(createOption("BB", "BB级"));
        levels.add(createOption("B", "B级"));
        levels.add(createOption("C", "C级"));
        return levels;
    }

    @Override
    public List<Map<String, Object>> getAssessmentTypes() {
        List<Map<String, Object>> types = new ArrayList<>();
        types.add(createOption("INITIAL", "初始评估"));
        types.add(createOption("PERIODIC", "定期评估"));
        types.add(createOption("TEMPORARY", "临时评估"));
        types.add(createOption("ANNUAL", "年度评估"));
        return types;
    }

    @Override
    public List<Map<String, Object>> getPartners() {
        // 使用自定义查询，只查询需要的字段
        Map<String, Object> params = new HashMap<>();
        List<TcPartnerArchive> partners = partnerArchiveMapper.selectByParams(params);
        List<Map<String, Object>> result = new ArrayList<>();
        for (TcPartnerArchive partner : partners) {
            Map<String, Object> map = new HashMap<>();
            map.put("partnerId", partner.getId());
            map.put("partnerCode", partner.getPartnerCode());
            map.put("partnerName", partner.getPartnerName());
            result.add(map);
        }
        return result.isEmpty() ? new ArrayList<>() : result;
    }

    @Override
    public boolean updateRiskStatus(String id, String riskStatus) {
        if (StringUtil.isEmpty(id)) {
            throw new ServiceException("风险评估ID不能为空");
        }
        TcPartnerRiskAssessment assessment = new TcPartnerRiskAssessment();
        assessment.setId(id);
        assessment.setRiskStatus(riskStatus);
        assessment.setUpdateTime(new Date());
        return assessmentMapper.updateById(assessment) > 0;
    }

    @Override
    public List<TcPartnerRiskAssessment> exportReport(Map<String, Object> params) {
        return assessmentMapper.selectByParams(params);
    }

    private Map<String, Object> createOption(String value, String label) {
        Map<String, Object> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
}

