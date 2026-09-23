package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TcPartnerRiskList;
import com.global.treasurer.mapper.TcPartnerRiskListMapper;
import com.global.treasurer.service.TcPartnerRiskService;
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
 * 伙伴风险管理服务实现类
 * 
 * @author AI Assistant
 * @date 2025-01-26
 */
@Service
@Transactional
public class TcPartnerRiskServiceImpl implements TcPartnerRiskService {
    @Autowired
    private TcPartnerRiskListMapper partnerRiskMapper;

    @Override
    public PageInfo<TcPartnerRiskList> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<TcPartnerRiskList> list = partnerRiskMapper.selectByParams(params);
        return new PageInfo<>(list);
    }

    @Override
    public TcPartnerRiskList getById(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new ServiceException("风险评估ID不能为空");
        }
        return partnerRiskMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean save(TcPartnerRiskList riskList) {
        if (riskList == null) {
            throw new ServiceException("风险评估信息不能为空");
        }
        if (StringUtil.isEmpty(riskList.getPartnerId())) {
            throw new ServiceException("伙伴ID不能为空");
        }
        
        riskList.setId(RandowUtil.uuId());
        riskList.setCreateTime(new Date());
        riskList.setUpdateTime(new Date());
        if (StringUtil.isEmpty(riskList.getStatus())) {
            riskList.setStatus("1");
        }
        
        return partnerRiskMapper.insertSelective(riskList) > 0;
    }

    @Override
    public boolean update(TcPartnerRiskList riskList) {
        if (riskList == null || StringUtil.isEmpty(riskList.getId())) {
            throw new ServiceException("风险评估信息不完整");
        }
        riskList.setUpdateTime(new Date());
        return partnerRiskMapper.updateByPrimaryKeySelective(riskList) > 0;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new ServiceException("风险评估ID不能为空");
        }
        return partnerRiskMapper.deleteByPrimaryKey(id) > 0;
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
    public List<TcPartnerRiskList> getByPartnerId(String partnerId) {
        return partnerRiskMapper.selectByPartnerId(partnerId);
    }

    @Override
    public List<TcPartnerRiskList> getByListType(String listType) {
        return partnerRiskMapper.selectByListType(listType);
    }

    @Override
    public List<TcPartnerRiskList> getByRiskLevel(String riskLevel) {
        return partnerRiskMapper.selectByRiskLevel(riskLevel);
    }

    @Override
    public Map<String, Object> getRiskStatistics() {
        return partnerRiskMapper.selectRiskStatistics();
    }

    @Override
    public List<TcPartnerRiskList> getRiskAlerts() {
        return partnerRiskMapper.selectRiskAlerts();
    }

    @Override
    public Map<String, Object> batchAssess(List<String> partnerIds, String assessUser) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        
        for (String partnerId : partnerIds) {
            try {
                TcPartnerRiskList riskList = new TcPartnerRiskList();
                riskList.setPartnerId(partnerId);
                riskList.setListType("GRAY");
                riskList.setRiskLevel("MEDIUM");
                riskList.setCreateUser(assessUser);
                save(riskList);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }

    @Override
    public boolean updateRiskStatus(String id, String status, String updateUser) {
        TcPartnerRiskList riskList = getById(id);
        if (riskList == null) {
            throw new ServiceException("风险评估不存在");
        }
        riskList.setStatus(status);
        riskList.setUpdateUser(updateUser);
        return update(riskList);
    }
}

