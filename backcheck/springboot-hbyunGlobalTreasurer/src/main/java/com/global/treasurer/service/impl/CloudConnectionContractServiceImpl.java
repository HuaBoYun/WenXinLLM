package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblCloudConnectionContract;
import com.global.treasurer.mapper.TblCloudConnectionContractMapper;
import com.global.treasurer.service.CloudConnectionContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 云连接合同服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
@Service
public class CloudConnectionContractServiceImpl extends ServiceImpl<TblCloudConnectionContractMapper, TblCloudConnectionContract> implements CloudConnectionContractService {
    @Autowired
    private TblCloudConnectionContractMapper contractMapper;

    @Override
    public Map<String, Object> getCloudContractPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数
        Integer page = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) :
                       (params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1));
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblCloudConnectionContract> list = contractMapper.selectCloudContractPage(params);
        int total = contractMapper.countCloudContractList(params);

        // 直接返回实际数据，避免序列化问题
        result.put("rows", list);
        result.put("tlist", list);
        result.put("total", total);
        result.put("totalRecord", total);

        return result;
    }

    @Override
    public TblCloudConnectionContract getCloudContractById(Long contractId) {
        return contractMapper.selectById(contractId);
    }

    @Override
    public int createCloudContract(TblCloudConnectionContract contract) {
        contract.setIsEnabled(1);
        contract.setDeleteFlag(0);
        contract.setCreatedTime(new Date());
        // 设置默认组织ID为1
        if (contract.getOrgId() == null) {
            contract.setOrgId(1L);
        }
        return contractMapper.insert(contract);
    }

    @Override
    public int updateCloudContract(TblCloudConnectionContract contract) {
        contract.setUpdatedTime(new Date());
        return contractMapper.updateById(contract);
    }

    @Override
    public int deleteCloudContract(List<Long> contractIds) {
        if (contractIds == null || contractIds.isEmpty()) return 0;
        int count = 0;
        for (Long contractId : contractIds) {
            TblCloudConnectionContract contract = getCloudContractById(contractId);
            if (contract != null) {
                contract.setDeleteFlag(1);
                contract.setUpdatedTime(new Date());
                count += contractMapper.updateById(contract);
            }
        }
        return count;
    }

    @Override
    public Map<String, Object> getCloudContractStatistics(Long orgId) {
        Map<String, Object> stats = contractMapper.selectCloudContractStatistics(orgId);
        if (stats == null) {
            stats = new HashMap<>();
            stats.put("totalContracts", 0);
            stats.put("activeContracts", 0);
            stats.put("signedContracts", 0);
            stats.put("expiringContracts", 0);
            stats.put("totalAmount", 0);
        }
        return stats;
    }

    @Override
    public List<TblCloudConnectionContract> getExpiringContracts(Integer days) {
        return contractMapper.selectExpiringContracts(days);
    }

    @Override
    public int toggleEnabled(List<Long> contractIds, Integer enabled) {
        if (contractIds == null || contractIds.isEmpty()) return 0;
        int count = 0;
        for (Long contractId : contractIds) {
            TblCloudConnectionContract contract = new TblCloudConnectionContract();
            contract.setContractId(contractId);
            contract.setIsEnabled(enabled);
            contract.setUpdatedTime(new Date());
            count += contractMapper.updateById(contract);
        }
        return count;
    }

    @Override
    public int changeContractStatus(Long contractId, String status, String reason) {
        TblCloudConnectionContract contract = new TblCloudConnectionContract();
        contract.setContractId(contractId);
        contract.setContractStatus(status);
        contract.setRemark(reason);
        contract.setUpdatedTime(new Date());
        return contractMapper.updateById(contract);
    }

    @Override
    public List<TblCloudConnectionContract> getRenewalAlerts() {
        // 获取30天内即将到期的合同
        return contractMapper.selectExpiringContracts(30);
    }

    @Override
    public List<TblCloudConnectionContract> exportContracts(Map<String, Object> params) {
        // 导出时不分页
        params.put("offset", 0);
        params.put("limit", Integer.MAX_VALUE);
        return contractMapper.selectCloudContractPage(params);
    }
}
