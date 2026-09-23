package com.global.treasurer.service.impl;

import com.global.treasurer.constant.YesNo;
import com.global.treasurer.exception.ServiceException;
import com.global.treasurer.entity.TcVirtualAccount;
import com.global.treasurer.mapper.TcVirtualAccountMapper;
import com.global.treasurer.service.TcVirtualAccountService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
// // import com.hbfk.util.redis.Random.RandomUtil; // 临时注释掉内部依赖
import java.util.UUID; // 临时注释掉内部依赖
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 虚拟账户管理Service实现
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@Service
public class TcVirtualAccountServiceImpl implements TcVirtualAccountService {
    @Resource
    private TcVirtualAccountMapper tcVirtualAccountMapper;

    @Override
    public PageInfo<TcVirtualAccount> getList(int pageNum, int pageSize, String accountCode, String accountName, 
                                              String accountType, String platformName, String currencyCode, 
                                              String syncStatus, String status) {
        PageMethod.startPage(pageNum, pageSize);
        List<TcVirtualAccount> list = tcVirtualAccountMapper.selectByCondition(accountCode, accountName, 
                                                                               accountType, platformName, 
                                                                               currencyCode, syncStatus, status);
        return new PageInfo<>(list);
    }

    @Override
    public TcVirtualAccount saveOrUpdate(TcVirtualAccount virtualAccount) {
        if (virtualAccount == null) {
            throw new ServiceException("虚拟账户信息不能为空");
        }

        // 校验必填字段
        if (StringUtils.isBlank(virtualAccount.getAccountCode())) {
            throw new ServiceException("账户编码不能为空");
        }
        if (StringUtils.isBlank(virtualAccount.getAccountName())) {
            throw new ServiceException("账户名称不能为空");
        }
        if (StringUtils.isBlank(virtualAccount.getAccountType())) {
            throw new ServiceException("账户类型不能为空");
        }

        Date now = new Date();
        
        if (StringUtils.isBlank(virtualAccount.getId())) {
            // 新增
            virtualAccount.setId(UUID.randomUUID().toString().replace("-", ""));
            
            // 检查账户编码是否已存在
            if (checkAccountCodeExists(virtualAccount.getAccountCode(), null)) {
                throw new ServiceException("账户编码已存在");
            }
            
            virtualAccount.setCreateTime(now);
            virtualAccount.setUpdateTime(now);
            virtualAccount.setVersionNo(0);
            if (StringUtils.isBlank(virtualAccount.getStatus())) {
                virtualAccount.setStatus("1");
            }
            if (StringUtils.isBlank(virtualAccount.getCurrencyCode())) {
                virtualAccount.setCurrencyCode("CNY");
            }
            if (virtualAccount.getBalance() == null) {
                virtualAccount.setBalance(BigDecimal.ZERO);
            }
            if (virtualAccount.getAvailableBalance() == null) {
                virtualAccount.setAvailableBalance(BigDecimal.ZERO);
            }
            if (virtualAccount.getFrozenBalance() == null) {
                virtualAccount.setFrozenBalance(BigDecimal.ZERO);
            }
            if (StringUtils.isBlank(virtualAccount.getSyncStatus())) {
                virtualAccount.setSyncStatus("PENDING");
            }
            
            tcVirtualAccountMapper.insertSelective(virtualAccount);
        } else {
            // 更新
            TcVirtualAccount existAccount = tcVirtualAccountMapper.selectByPrimaryKey(virtualAccount.getId());
            if (existAccount == null) {
                throw new ServiceException("虚拟账户不存在");
            }
            
            // 检查账户编码是否已存在（排除自己）
            if (checkAccountCodeExists(virtualAccount.getAccountCode(), virtualAccount.getId())) {
                throw new ServiceException("账户编码已存在");
            }
            
            virtualAccount.setUpdateTime(now);
            virtualAccount.setVersionNo(existAccount.getVersionNo() + 1);
            
            tcVirtualAccountMapper.updateByPrimaryKeySelective(virtualAccount);
        }
        
        return virtualAccount;
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("账户ID不能为空");
        }
        
        TcVirtualAccount virtualAccount = tcVirtualAccountMapper.selectByPrimaryKey(id);
        if (virtualAccount == null) {
            throw new ServiceException("虚拟账户不存在");
        }
        
        tcVirtualAccountMapper.deleteByPrimaryKey(id);
    }

    @Override
    public TcVirtualAccount findById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return tcVirtualAccountMapper.selectByPrimaryKey(id);
    }

    @Override
    public TcVirtualAccount findByAccountCode(String accountCode) {
        if (StringUtils.isBlank(accountCode)) {
            return null;
        }
        return tcVirtualAccountMapper.selectByAccountCode(accountCode);
    }

    @Override
    public List<TcVirtualAccount> getAccountsByType(String accountType) {
        if (StringUtils.isBlank(accountType)) {
            return null;
        }
        return tcVirtualAccountMapper.selectByAccountType(accountType);
    }

    @Override
    public List<TcVirtualAccount> getAccountsByPlatformName(String platformName) {
        if (StringUtils.isBlank(platformName)) {
            return null;
        }
        return tcVirtualAccountMapper.selectByPlatformName(platformName);
    }

    @Override
    public List<TcVirtualAccount> getAccountsByCurrencyCode(String currencyCode) {
        if (StringUtils.isBlank(currencyCode)) {
            return null;
        }
        return tcVirtualAccountMapper.selectByCurrencyCode(currencyCode);
    }

    @Override
    public List<TcVirtualAccount> getAccountsBySyncStatus(String syncStatus) {
        if (StringUtils.isBlank(syncStatus)) {
            return null;
        }
        return tcVirtualAccountMapper.selectBySyncStatus(syncStatus);
    }

    @Override
    public List<TcVirtualAccount> getAccountsForSync() {
        return tcVirtualAccountMapper.selectAccountsForSync();
    }

    @Override
    public void updateBalance(String id, BigDecimal balance, BigDecimal availableBalance, 
                              BigDecimal frozenBalance, String updateUser) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("账户ID不能为空");
        }
        
        int result = tcVirtualAccountMapper.updateBalance(id, balance, availableBalance, 
                                                          frozenBalance, "SYNCED", updateUser);
        if (result == 0) {
            throw new ServiceException("更新账户余额失败");
        }
    }

    @Override
    public void updateSyncStatus(String id, String syncStatus, String updateUser) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("账户ID不能为空");
        }
        if (StringUtils.isBlank(syncStatus)) {
            throw new ServiceException("同步状态不能为空");
        }
        
        String lastSyncTime = new Date().toString();
        int result = tcVirtualAccountMapper.updateSyncStatus(id, syncStatus, lastSyncTime);
        if (result == 0) {
            throw new ServiceException("更新同步状态失败");
        }
    }

    @Override
    public void batchDelete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException("账户ID列表不能为空");
        }
        
        for (String id : ids) {
            delete(id);
        }
    }

    @Override
    public List<Map<String, Object>> getAccountTypeStatistics() {
        return tcVirtualAccountMapper.countByAccountType();
    }

    @Override
    public List<Map<String, Object>> getSyncStatusStatistics() {
        return tcVirtualAccountMapper.countBySyncStatus();
    }

    @Override
    public boolean checkAccountCodeExists(String accountCode, String excludeId) {
        if (StringUtils.isBlank(accountCode)) {
            return false;
        }
        
        TcVirtualAccount virtualAccount = tcVirtualAccountMapper.selectByAccountCode(accountCode);
        if (virtualAccount == null) {
            return false;
        }
        
        // 如果有排除ID，且查到的记录ID等于排除ID，则认为不存在
        if (StringUtils.isNotBlank(excludeId) && excludeId.equals(virtualAccount.getId())) {
            return false;
        }
        
        return true;
    }

    @Override
    public boolean syncAccountBalance(String id) {
        TcVirtualAccount account = findById(id);
        if (account == null) {
            throw new ServiceException("虚拟账户不存在");
        }
        
        try {
            // 这里应该实现具体的余额同步逻辑
            // 根据账户的API配置调用第三方接口获取余额
            // 暂时返回true，实际项目中需要实现具体的同步逻辑
            
            updateSyncStatus(id, "SYNCED", null);
            return true;
        } catch (Exception e) {
            updateSyncStatus(id, "FAILED", null);
            return false;
        }
    }

    @Override
    public Map<String, Boolean> batchSyncAccountBalance(List<String> ids) {
        Map<String, Boolean> results = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                try {
                    boolean result = syncAccountBalance(id);
                    results.put(id, result);
                } catch (Exception e) {
                    results.put(id, false);
                }
            }
        }
        return results;
    }

    @Override
    public BigDecimal calculateTotalBalance(String accountType, String currencyCode) {
        BigDecimal totalBalance = tcVirtualAccountMapper.sumBalanceByTypeAndCurrency(accountType, currencyCode);
        return totalBalance != null ? totalBalance : BigDecimal.ZERO;
    }

    @Override
    public void freezeBalance(String id, BigDecimal amount, String updateUser) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("账户ID不能为空");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("冻结金额必须大于0");
        }
        
        TcVirtualAccount account = findById(id);
        if (account == null) {
            throw new ServiceException("虚拟账户不存在");
        }
        
        // 检查可用余额是否足够
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new ServiceException("可用余额不足");
        }
        
        // 更新余额
        BigDecimal newAvailableBalance = account.getAvailableBalance().subtract(amount);
        BigDecimal newFrozenBalance = account.getFrozenBalance().add(amount);
        
        updateBalance(id, account.getBalance(), newAvailableBalance, newFrozenBalance, updateUser);
    }

    @Override
    public void unfreezeBalance(String id, BigDecimal amount, String updateUser) {
        if (StringUtils.isBlank(id)) {
            throw new ServiceException("账户ID不能为空");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("解冻金额必须大于0");
        }
        
        TcVirtualAccount account = findById(id);
        if (account == null) {
            throw new ServiceException("虚拟账户不存在");
        }
        
        // 检查冻结余额是否足够
        if (account.getFrozenBalance().compareTo(amount) < 0) {
            throw new ServiceException("冻结余额不足");
        }
        
        // 更新余额
        BigDecimal newAvailableBalance = account.getAvailableBalance().add(amount);
        BigDecimal newFrozenBalance = account.getFrozenBalance().subtract(amount);
        
        updateBalance(id, account.getBalance(), newAvailableBalance, newFrozenBalance, updateUser);
    }

    @Override
    public Map<String, BigDecimal> getAccountBalanceInfo(String id) {
        TcVirtualAccount account = findById(id);
        if (account == null) {
            return null;
        }
        
        Map<String, BigDecimal> balanceInfo = new HashMap<>();
        balanceInfo.put("balance", account.getBalance());
        balanceInfo.put("availableBalance", account.getAvailableBalance());
        balanceInfo.put("frozenBalance", account.getFrozenBalance());
        
        return balanceInfo;
    }
}
