package com.global.treasurer.service.impl;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblGtAccountInfo;
import com.global.treasurer.mapper.TblGtAccountInfoMapper;
import com.global.treasurer.service.TblGtAccountInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 全球司库-账户信息Service实现类
 *
 * @author AI Developer
 * @since 2026-01-15
 */
@Service
public class TblGtAccountInfoServiceImpl extends ServiceImpl<TblGtAccountInfoMapper, TblGtAccountInfo>
        implements TblGtAccountInfoService {
    private static final Logger log = LoggerFactory.getLogger(TblGtAccountInfoServiceImpl.class);

    @Resource
    private TblGtAccountInfoMapper tblGtAccountInfoMapper;

    @Override
    public IPage<TblGtAccountInfo> selectAccountPage(Page<TblGtAccountInfo> page,
                                                      BigDecimal orgId,
                                                      String accountNumber,
                                                      String accountName,
                                                      String accountType,
                                                      String bankCode,
                                                      String currencyCode,
                                                      String accountStatus) {
        // 使用PageHelper分页
        PageHelper.startPage((int)page.getCurrent(), (int)page.getSize());

        // 调用Mapper的自定义查询方法
        List<TblGtAccountInfo> list = tblGtAccountInfoMapper.selectAccountList(
                orgId, accountNumber, accountName, accountType,
                bankCode, currencyCode, accountStatus
        );

        // 使用PageInfo包装结果
        PageInfo<TblGtAccountInfo> pageInfo = new PageInfo<>(list);

        // 转换为MyBatis-Plus的IPage对象
        Page<TblGtAccountInfo> result = new Page<>(page.getCurrent(), page.getSize(), pageInfo.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> getAccountStats(BigDecimal orgId) {
        return tblGtAccountInfoMapper.selectAccountStats(orgId);
    }

    @Override
    public Map<String, Object> getBalanceOverview(BigDecimal orgId) {
        return tblGtAccountInfoMapper.selectBalanceOverview(orgId);
    }

    @Override
    public Map<String, Object> getStatusStats(BigDecimal orgId) {
        return tblGtAccountInfoMapper.selectStatusStats(orgId);
    }

    @Override
    public List<Map<String, Object>> getBankDistribution(BigDecimal orgId) {
        return tblGtAccountInfoMapper.selectBankDistribution(orgId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateAccount(BigDecimal accountId, BigDecimal updateUser) {
        TblGtAccountInfo accountInfo = this.getById(accountId);
        if (accountInfo == null) {
            log.error("账户不存在, accountId={}", accountId);
            return false;
        }

        accountInfo.setAccountStatus("ACTIVE");
        accountInfo.setUpdateUser(updateUser);
        accountInfo.setUpdateTime(LocalDateTime.now());

        return this.updateById(accountInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean freezeAccount(BigDecimal accountId, BigDecimal updateUser) {
        TblGtAccountInfo accountInfo = this.getById(accountId);
        if (accountInfo == null) {
            log.error("账户不存在, accountId={}", accountId);
            return false;
        }

        accountInfo.setAccountStatus("FROZEN");
        accountInfo.setUpdateUser(updateUser);
        accountInfo.setUpdateTime(LocalDateTime.now());

        return this.updateById(accountInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfreezeAccount(BigDecimal accountId, BigDecimal updateUser) {
        TblGtAccountInfo accountInfo = this.getById(accountId);
        if (accountInfo == null) {
            log.error("账户不存在, accountId={}", accountId);
            return false;
        }

        accountInfo.setAccountStatus("ACTIVE");
        accountInfo.setUpdateUser(updateUser);
        accountInfo.setUpdateTime(LocalDateTime.now());

        return this.updateById(accountInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeAccount(BigDecimal accountId, BigDecimal updateUser) {
        TblGtAccountInfo accountInfo = this.getById(accountId);
        if (accountInfo == null) {
            log.error("账户不存在, accountId={}", accountId);
            return false;
        }

        accountInfo.setAccountStatus("CLOSED");
        accountInfo.setCloseDate(java.time.LocalDate.now());
        accountInfo.setUpdateUser(updateUser);
        accountInfo.setUpdateTime(LocalDateTime.now());

        return this.updateById(accountInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultAccount(BigDecimal accountId, String currencyCode, BigDecimal orgId, BigDecimal updateUser) {
        // 先清除该币种的默认账户标记
        tblGtAccountInfoMapper.clearDefaultAccount(orgId, currencyCode);

        // 设置新的默认账户
        return tblGtAccountInfoMapper.setDefaultAccount(accountId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteAccounts(List<BigDecimal> accountIds, BigDecimal userId) {
        for (BigDecimal accountId : accountIds) {
            TblGtAccountInfo accountInfo = this.getById(accountId);
            if (accountInfo != null) {
                accountInfo.setDeleteFlag(1);
                accountInfo.setUpdateUser(userId);
                accountInfo.setUpdateTime(LocalDateTime.now());
                this.updateById(accountInfo);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createAccount(TblGtAccountInfo accountInfo) {
        accountInfo.setCreateTime(LocalDateTime.now());
        accountInfo.setUpdateTime(LocalDateTime.now());
        accountInfo.setDeleteFlag(0);

        // 设置默认值
        if (accountInfo.getBalance() == null) {
            accountInfo.setBalance(java.math.BigDecimal.ZERO);
        }
        if (accountInfo.getAvailableBalance() == null) {
            accountInfo.setAvailableBalance(java.math.BigDecimal.ZERO);
        }
        if (accountInfo.getFrozenBalance() == null) {
            accountInfo.setFrozenBalance(java.math.BigDecimal.ZERO);
        }
        if (accountInfo.getIsDefault() == null) {
            accountInfo.setIsDefault(0);
        }
        if (accountInfo.getIsDirectConnect() == null) {
            accountInfo.setIsDirectConnect(0);
        }
        if (accountInfo.getAccountStatus() == null) {
            accountInfo.setAccountStatus("ACTIVE");
        }

        return this.save(accountInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAccountInfo(TblGtAccountInfo accountInfo) {
        accountInfo.setUpdateTime(LocalDateTime.now());
        return this.updateById(accountInfo);
    }

    @Override
    public boolean checkAccountNumberUnique(String accountNumber, BigDecimal orgId, BigDecimal excludeId) {
        int count = tblGtAccountInfoMapper.countByAccountNumber(accountNumber, orgId, excludeId);
        return count == 0;
    }

    @Override
    public List<TblGtAccountInfo> selectAccountList(BigDecimal orgId, String accountNumber, String accountName,
                                                    String accountType, String bankCode, String currencyCode,
                                                    String accountStatus) {
        return tblGtAccountInfoMapper.selectAccountList(orgId, accountNumber, accountName,
                accountType, bankCode, currencyCode, accountStatus);
    }
}
