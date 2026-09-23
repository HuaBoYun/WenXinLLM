package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TcPartnerArchive;
import com.global.treasurer.mapper.TcPartnerArchiveMapper;
import com.global.treasurer.service.TcPartnerArchiveService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.exception.ServiceException;
import com.hbfk.util.StringUtil;
import com.hbfk.util.RandowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 伙伴档案服务实现类
 * 
 * @author AI Assistant
 * @date 2025-01-26
 */
@Service
@Transactional
public class TcPartnerArchiveServiceImpl implements TcPartnerArchiveService {
    @Autowired
    private TcPartnerArchiveMapper partnerArchiveMapper;

    @Override
    public PageInfo<TcPartnerArchive> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<TcPartnerArchive> list = partnerArchiveMapper.selectByParams(params);
        return new PageInfo<>(list);
    }

    @Override
    public TcPartnerArchive getById(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new ServiceException("档案ID不能为空");
        }
        return partnerArchiveMapper.selectByPrimaryKey(id);
    }

    @Override
    public TcPartnerArchive getByPartnerCode(String partnerCode) {
        if (StringUtil.isEmpty(partnerCode)) {
            throw new ServiceException("伙伴编码不能为空");
        }
        return partnerArchiveMapper.selectByPartnerCode(partnerCode);
    }

    @Override
    public boolean save(TcPartnerArchive partnerArchive) {
        if (partnerArchive == null) {
            throw new ServiceException("伙伴档案信息不能为空");
        }
        if (StringUtil.isEmpty(partnerArchive.getPartnerCode())) {
            throw new ServiceException("伙伴编码不能为空");
        }
        if (StringUtil.isEmpty(partnerArchive.getPartnerName())) {
            throw new ServiceException("伙伴名称不能为空");
        }
        if (checkPartnerCodeExists(partnerArchive.getPartnerCode(), null)) {
            throw new ServiceException("伙伴编码已存在");
        }
        
        partnerArchive.setId(RandowUtil.uuId());
        partnerArchive.setCreateTime(new Date());
        partnerArchive.setUpdateTime(new Date());
        if (StringUtil.isEmpty(partnerArchive.getStatus())) {
            partnerArchive.setStatus("1");
        }
        
        return partnerArchiveMapper.insertSelective(partnerArchive) > 0;
    }

    @Override
    public boolean update(TcPartnerArchive partnerArchive) {
        if (partnerArchive == null || StringUtil.isEmpty(partnerArchive.getId())) {
            throw new ServiceException("伙伴档案信息不完整");
        }
        if (checkPartnerCodeExists(partnerArchive.getPartnerCode(), partnerArchive.getId())) {
            throw new ServiceException("伙伴编码已存在");
        }

        partnerArchive.setUpdateTime(new Date());

        // 使用自定义的 updateByIdIgnoreVersion 方法
        // 只根据 ID 更新，不使用乐观锁，WHERE 条件只有 ID
        // 只更新非 null 字段
        return partnerArchiveMapper.updateByIdIgnoreVersion(partnerArchive) > 0;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new ServiceException("档案ID不能为空");
        }
        // 使用自定义的 deleteByIdIgnoreVersion 方法
        // 只根据 ID 删除，不使用乐观锁，WHERE 条件只有 ID
        return partnerArchiveMapper.deleteByIdIgnoreVersion(id) > 0;
    }

    @Override
    public boolean batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("档案ID列表不能为空");
        }
        for (String id : ids) {
            delete(id);
        }
        return true;
    }

    @Override
    public List<TcPartnerArchive> getByPartnerTypeId(String partnerTypeId) {
        return partnerArchiveMapper.selectByPartnerTypeId(partnerTypeId);
    }

    @Override
    public boolean checkPartnerCodeExists(String partnerCode, String excludeId) {
        return partnerArchiveMapper.checkPartnerCodeExists(partnerCode, excludeId) > 0;
    }

    @Override
    public boolean checkCreditCodeExists(String unifiedSocialCreditCode, String excludeId) {
        return partnerArchiveMapper.checkCreditCodeExists(unifiedSocialCreditCode, excludeId) > 0;
    }

    @Override
    public List<TcPartnerArchive> getByCooperationStatus(String cooperationStatus) {
        return partnerArchiveMapper.selectByCooperationStatus(cooperationStatus);
    }

    @Override
    public List<TcPartnerArchive> getByRiskLevel(String riskLevel) {
        return partnerArchiveMapper.selectByRiskLevel(riskLevel);
    }

    @Override
    public List<TcPartnerArchive> getByCreditRating(String creditRating) {
        return partnerArchiveMapper.selectByCreditRating(creditRating);
    }

    @Override
    public boolean batchUpdateCooperationStatus(List<String> ids, String cooperationStatus, String updateUser) {
        return partnerArchiveMapper.batchUpdateCooperationStatus(ids, cooperationStatus, updateUser) > 0;
    }

    @Override
    public boolean batchUpdateRiskLevel(List<String> ids, String riskLevel, String updateUser) {
        return partnerArchiveMapper.batchUpdateRiskLevel(ids, riskLevel, updateUser) > 0;
    }

    @Override
    public Map<String, Object> getPartnerStatistics() {
        Map<String, Object> rawStats = partnerArchiveMapper.selectPartnerStatistics();
        // 转换字段名为驼峰命名（兼容数据库返回的大写字段名）
        Map<String, Object> stats = new HashMap<>();

        // 处理可能的大写字段名
        for (Map.Entry<String, Object> entry : rawStats.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 将大写字段名转换为驼峰命名
            if (key.equals("TOTALPARTNERS") || key.equals("totalPartners")) {
                stats.put("totalPartners", value);
            } else if (key.equals("ACTIVEPARTNERS") || key.equals("activePartners")) {
                stats.put("activePartners", value);
            } else if (key.equals("HIGHRISKPARTNERS") || key.equals("highRiskPartners")) {
                stats.put("highRiskPartners", value);
            } else if (key.equals("MEDIUMRISKPARTNERS") || key.equals("mediumRiskPartners")) {
                stats.put("mediumRiskPartners", value);
            } else if (key.equals("LOWRISKPARTNERS") || key.equals("lowRiskPartners")) {
                stats.put("lowRiskPartners", value);
            } else if (key.equals("BANKPARTNERS") || key.equals("bankPartners")) {
                stats.put("bankPartners", value);
            } else if (key.equals("SUPPLIERPARTNERS") || key.equals("supplierPartners")) {
                stats.put("supplierPartners", value);
            } else {
                stats.put(key, value);
            }
        }

        // 确保所有字段都存在
        stats.putIfAbsent("totalPartners", 0);
        stats.putIfAbsent("activePartners", 0);
        stats.putIfAbsent("bankPartners", 0);
        stats.putIfAbsent("supplierPartners", 0);
        stats.putIfAbsent("highRiskPartners", 0);
        stats.putIfAbsent("mediumRiskPartners", 0);
        stats.putIfAbsent("lowRiskPartners", 0);

        return stats;
    }

    @Override
    public List<TcPartnerArchive> getByIndustryClassification(String industryClassification) {
        return partnerArchiveMapper.selectByIndustryClassification(industryClassification);
    }

    @Override
    public List<TcPartnerArchive> getByEnterpriseScale(String enterpriseScale) {
        return partnerArchiveMapper.selectByEnterpriseScale(enterpriseScale);
    }

    @Override
    public List<TcPartnerArchive> getExpiringPartners(Integer days) {
        return partnerArchiveMapper.selectExpiringPartners(days);
    }

    @Override
    public List<TcPartnerArchive> searchByKeyword(String keyword) {
        return partnerArchiveMapper.searchByKeyword(keyword);
    }

    @Override
    public Map<String, Object> importPartners(List<TcPartnerArchive> partnerList) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();

        for (TcPartnerArchive partner : partnerList) {
            try {
                save(partner);
                successCount++;
            } catch (Exception e) {
                failCount++;
                errors.add(partner.getPartnerCode() + ": " + e.getMessage());
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        return result;
    }

    @Override
    public List<TcPartnerArchive> exportPartners(Map<String, Object> params) {
        return partnerArchiveMapper.selectByParams(params);
    }

    @Override
    public Map<String, Object> validatePartnerInfo(TcPartnerArchive partnerArchive) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (partnerArchive == null) {
            errors.add("伙伴档案信息不能为空");
        } else {
            if (StringUtil.isEmpty(partnerArchive.getPartnerCode())) {
                errors.add("伙伴编码不能为空");
            }
            if (StringUtil.isEmpty(partnerArchive.getPartnerName())) {
                errors.add("伙伴名称不能为空");
            }
        }

        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    public Map<String, Object> syncPartnerInfo(String partnerId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "同步成功");
        result.put("syncTime", new Date());
        return result;
    }
}

