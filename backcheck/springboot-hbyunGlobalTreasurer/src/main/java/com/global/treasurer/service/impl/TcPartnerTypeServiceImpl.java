package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.global.treasurer.entity.TcPartnerType;
import com.global.treasurer.mapper.TcPartnerTypeMapper;
import com.global.treasurer.service.TcPartnerTypeService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * 合作伙伴类型 Service 实现类
 */
@Service
public class TcPartnerTypeServiceImpl implements TcPartnerTypeService {
    
    @Resource
    private TcPartnerTypeMapper partnerTypeMapper;
    
    @Override
    public List<TcPartnerType> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        return partnerTypeMapper.listPartnerType(params);
    }
    
    @Override
    public int count(Map<String, Object> params) {
        return partnerTypeMapper.countPartnerType(params);
    }
    
    @Override
    public TcPartnerType getById(Long partnerTypeId) {
        return partnerTypeMapper.getById(partnerTypeId);
    }
    
    @Override
    public boolean save(TcPartnerType partnerType) {
        // 检查编码是否已存在
        TcPartnerType existing = partnerTypeMapper.getByCode(partnerType.getTypeCode());
        if (existing != null) {
            throw new RuntimeException("类型编码已存在");
        }
        partnerType.setCreateTime(new Date());
        partnerType.setUpdateTime(new Date());
        return partnerTypeMapper.insert(partnerType) > 0;
    }
    
    @Override
    public boolean update(TcPartnerType partnerType) {
        partnerType.setUpdateTime(new Date());
        return partnerTypeMapper.update(partnerType) > 0;
    }
    
    @Override
    public boolean delete(Long partnerTypeId) {
        return partnerTypeMapper.delete(partnerTypeId) > 0;
    }
    
    @Override
    public boolean batchDelete(List<Long> ids) {
        return partnerTypeMapper.batchDelete(ids) > 0;
    }
    
    @Override
    public boolean updateSortOrder(List<Map<String, Object>> sortList) {
        return partnerTypeMapper.updateSortOrder(sortList) > 0;
    }
    
    @Override
    public boolean toggleStatus(Long partnerTypeId, Integer isEnabled) {
        return partnerTypeMapper.toggleStatus(partnerTypeId, isEnabled) > 0;
    }
    
    @Override
    public List<TcPartnerType> getAllEnabled() {
        return partnerTypeMapper.getAllEnabled();
    }
    
    @Override
    public List<TcPartnerType> exportPartnerType(Map<String, Object> params) {
        return partnerTypeMapper.exportPartnerType(params);
    }
    
    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总类型数
        Map<String, Object> countParams = new HashMap<>();
        int totalTypes = partnerTypeMapper.countPartnerType(countParams);
        stats.put("totalPartnerTypes", totalTypes);
        
        // 金融机构类型数
        countParams.put("category", "FINANCIAL");
        int financialTypes = partnerTypeMapper.countPartnerType(countParams);
        stats.put("financialTypes", financialTypes);
        
        // 商业伙伴类型数
        countParams.put("category", "BUSINESS");
        int businessTypes = partnerTypeMapper.countPartnerType(countParams);
        stats.put("businessTypes", businessTypes);
        
        // 启用类型数
        countParams.clear();
        countParams.put("isEnabled", 1);
        int activeTypes = partnerTypeMapper.countPartnerType(countParams);
        stats.put("activeTypes", activeTypes);
        
        return stats;
    }
}
