package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TcCloudConnectionContract;
import com.global.treasurer.mapper.TcCloudConnectionContractMapper;
import com.global.treasurer.service.TcCloudConnectionContractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.exception.ServiceException;
import com.hbfk.util.StringUtil;
import com.hbfk.util.RandowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class TcCloudConnectionContractServiceImpl implements TcCloudConnectionContractService {
    @Autowired
    private TcCloudConnectionContractMapper contractMapper;

    @Override
    public PageInfo<TcCloudConnectionContract> list(Integer pageNum, Integer pageSize, Map<String, Object> params) {
        PageHelper.startPage(pageNum, pageSize);
        List<TcCloudConnectionContract> list = contractMapper.selectByParams(params);
        return new PageInfo<>(list);
    }

    @Override
    public TcCloudConnectionContract getById(String id) {
        if (StringUtil.isEmpty(id)) throw new ServiceException("合同ID不能为空");
        return contractMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean save(TcCloudConnectionContract contract) {
        if (contract == null) throw new ServiceException("合同信息不能为空");
        contract.setId(RandowUtil.uuId());
        contract.setCreateTime(new Date());
        contract.setUpdateTime(new Date());
        return contractMapper.insertSelective(contract) > 0;
    }

    @Override
    public boolean update(TcCloudConnectionContract contract) {
        if (contract == null || StringUtil.isEmpty(contract.getId())) 
            throw new ServiceException("合同信息不完整");
        contract.setUpdateTime(new Date());
        return contractMapper.updateByPrimaryKeySelective(contract) > 0;
    }

    @Override
    public boolean delete(String id) {
        if (StringUtil.isEmpty(id)) throw new ServiceException("合同ID不能为空");
        return contractMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public boolean batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) throw new ServiceException("合同ID列表不能为空");
        for (String id : ids) delete(id);
        return true;
    }

    @Override
    public Map<String, Object> getStatistics() {
        return contractMapper.selectStatistics();
    }

    @Override
    public List<TcCloudConnectionContract> getRenewalAlerts() {
        return contractMapper.selectRenewalAlerts();
    }

    @Override
    public List<TcCloudConnectionContract> getRenewalAlerts(Integer days) {
        return contractMapper.selectRenewalAlertsByDays(days != null ? days : 30);
    }

    @Override
    public List<Map<String, String>> getServiceProviders() {
        List<Map<String, String>> providers = new ArrayList<>();
        providers.add(createOption("ICBC", "工商银行"));
        providers.add(createOption("CCB", "建设银行"));
        providers.add(createOption("ABC", "农业银行"));
        providers.add(createOption("BOC", "中国银行"));
        providers.add(createOption("BOCOM", "交通银行"));
        return providers;
    }

    @Override
    public List<Map<String, String>> getServiceTypes() {
        List<Map<String, String>> types = new ArrayList<>();
        types.add(createOption("BALANCE_QUERY", "余额查询"));
        types.add(createOption("TRANSACTION_DETAIL", "交易明细"));
        types.add(createOption("PAYMENT", "支付服务"));
        types.add(createOption("COLLECTION", "收款服务"));
        types.add(createOption("RECONCILIATION", "对账服务"));
        return types;
    }

    @Override
    public List<TcCloudConnectionContract> exportContracts(Map<String, Object> params) {
        return contractMapper.selectByParams(params);
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
}

