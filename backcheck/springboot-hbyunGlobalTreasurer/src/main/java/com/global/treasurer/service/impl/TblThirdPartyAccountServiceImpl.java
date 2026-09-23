package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblThirdPartyAccount;
import com.global.treasurer.mapper.TblThirdPartyAccountMapper;
import com.global.treasurer.service.TblThirdPartyAccountService;
import com.global.treasurer.vo.param.TblThirdPartyAccountQueryParam;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方账户Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
@Service
public class TblThirdPartyAccountServiceImpl extends ServiceImpl<TblThirdPartyAccountMapper, TblThirdPartyAccount>
        implements TblThirdPartyAccountService {
    @Override
    public IPage<TblThirdPartyAccount> getThirdPartyAccountPage(Integer page, Integer limit,
                                                              String accountCode, String accountName,
                                                              String thirdPartySystem, String accountType,
                                                              String connectionStatus, Integer isEnabled) {
        Page<TblThirdPartyAccount> pageParam = new Page<>(page, limit);
        QueryWrapper<TblThirdPartyAccount> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(accountCode)) {
            queryWrapper.like("ACCOUNT_CODE", accountCode);
        }
        if (StringUtils.hasText(accountName)) {
            queryWrapper.like("ACCOUNT_NAME", accountName);
        }
        if (StringUtils.hasText(thirdPartySystem)) {
            queryWrapper.eq("THIRD_PARTY_SYSTEM", thirdPartySystem);
        }
        if (StringUtils.hasText(accountType)) {
            queryWrapper.eq("ACCOUNT_TYPE", accountType);
        }
        if (StringUtils.hasText(connectionStatus)) {
            queryWrapper.eq("CONNECTION_STATUS", connectionStatus);
        }
        if (isEnabled != null) {
            queryWrapper.eq("IS_ENABLED", isEnabled);
        }

        queryWrapper.orderByDesc("CREATE_TIME");
        return this.page(pageParam, queryWrapper);
    }

    @Override
    public List<Map<String, String>> getAccountTypes() {
        List<Map<String, String>> types = new ArrayList<>();

        // 添加第三方平台类型
        Map<String, String> wechat = new HashMap<>();
        wechat.put("value", "WECHAT");
        wechat.put("label", "微信支付");
        types.add(wechat);

        Map<String, String> alipay = new HashMap<>();
        alipay.put("value", "ALIPAY");
        alipay.put("label", "支付宝");
        types.add(alipay);

        Map<String, String> unionpay = new HashMap<>();
        unionpay.put("value", "UNIONPAY");
        unionpay.put("label", "银联支付");
        types.add(unionpay);

        Map<String, String> paypal = new HashMap<>();
        paypal.put("value", "PAYPAL");
        paypal.put("label", "PayPal");
        types.add(paypal);

        Map<String, String> other = new HashMap<>();
        other.put("value", "OTHER");
        other.put("label", "其他");
        types.add(other);

        return types;
    }

    @Override
    public List<Map<String, String>> getSystems() {
        List<Map<String, String>> systems = new ArrayList<>();

        // 添加第三方系统类型
        Map<String, String> erp = new HashMap<>();
        erp.put("value", "ERP");
        erp.put("label", "ERP系统");
        systems.add(erp);

        Map<String, String> crm = new HashMap<>();
        crm.put("value", "CRM");
        crm.put("label", "CRM系统");
        systems.add(crm);

        Map<String, String> hr = new HashMap<>();
        hr.put("value", "HR");
        hr.put("label", "HR系统");
        systems.add(hr);

        Map<String, String> oa = new HashMap<>();
        oa.put("value", "OA");
        oa.put("label", "OA系统");
        systems.add(oa);

        Map<String, String> finance = new HashMap<>();
        finance.put("value", "FINANCE");
        finance.put("label", "财务系统");
        systems.add(finance);

        Map<String, String> other = new HashMap<>();
        other.put("value", "OTHER");
        other.put("label", "其他系统");
        systems.add(other);

        return systems;
    }

    @Override
    public boolean testConnection(String id) {
        TblThirdPartyAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        // TODO: 实现第三方账户连接测试逻辑
        // 这里应该根据账户配置测试实际的连接
        // 例如: 测试API接口连接、验证账号有效性等

        // 临时返回true,实际应该测试真实的连接
        return true;
    }

    @Override
    public int batchSync(List<String> ids) {
        int count = 0;
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                try {
                    TblThirdPartyAccount account = this.getById(id);
                    if (account != null) {
                        // TODO: 实现实际的同步逻辑
                        // 例如: 从第三方系统同步账户信息、交易记录等
                        account.setUpdateTime(new Date());
                        this.updateById(account);
                        count++;
                    }
                } catch (Exception e) {
                    // 记录错误但继续处理其他账户
                    e.printStackTrace();
                }
            }
        }
        return count;
    }

    @Override
    public int syncStatus(String id) {
        TblThirdPartyAccount account = this.getById(id);
        if (account == null) {
            throw new RuntimeException("账户不存在: " + id);
        }

        // TODO: 实现实际的状态同步逻辑
        // 例如: 查询第三方系统获取最新状态
        account.setUpdateTime(new Date());
        this.updateById(account);
        return 1;
    }

    @Override
    public int delete(String id) {
        return Integer.parseInt(this.removeById(id) + "");
    }

    @Override
    public int update(TblThirdPartyAccount account) {
        return Integer.parseInt(this.updateById(account) + "");
    }

    @Override
    public int insert(TblThirdPartyAccount account) {
        return Integer.parseInt(this.save(account) + "");
    }

    @Override
    public List<TblThirdPartyAccount> selectList(TblThirdPartyAccountQueryParam param) {
        QueryWrapper<TblThirdPartyAccount> queryWrapper = new QueryWrapper<>();

        if (param != null) {
            if (StringUtils.hasText(param.getAccountCode())) {
                queryWrapper.like("ACCOUNT_CODE", param.getAccountCode());
            }
            if (StringUtils.hasText(param.getAccountName())) {
                queryWrapper.like("ACCOUNT_NAME", param.getAccountName());
            }
            if (StringUtils.hasText(param.getThirdPartySystem())) {
                queryWrapper.eq("THIRD_PARTY_SYSTEM", param.getThirdPartySystem());
            }
            if (StringUtils.hasText(param.getAccountType())) {
                queryWrapper.eq("ACCOUNT_TYPE", param.getAccountType());
            }
            if (StringUtils.hasText(param.getConnectionStatus())) {
                queryWrapper.eq("CONNECTION_STATUS", param.getConnectionStatus());
            }
        }

        queryWrapper.orderByDesc("CREATE_TIME");
        return this.list(queryWrapper);
    }

    @Override
    public TblThirdPartyAccount selectById(String id) {
        return this.getById(id);
    }

    @Override
    public IPage<TblThirdPartyAccount> selectPage(TblThirdPartyAccountQueryParam param) {
        Page<TblThirdPartyAccount> page = new Page<>(param.getPageNo(), param.getPageSize());
        QueryWrapper<TblThirdPartyAccount> queryWrapper = new QueryWrapper<>();

        if (param != null) {
            if (StringUtils.hasText(param.getAccountCode())) {
                queryWrapper.like("ACCOUNT_CODE", param.getAccountCode());
            }
            if (StringUtils.hasText(param.getAccountName())) {
                queryWrapper.like("ACCOUNT_NAME", param.getAccountName());
            }
            if (StringUtils.hasText(param.getThirdPartySystem())) {
                queryWrapper.eq("THIRD_PARTY_SYSTEM", param.getThirdPartySystem());
            }
            if (StringUtils.hasText(param.getAccountType())) {
                queryWrapper.eq("ACCOUNT_TYPE", param.getAccountType());
            }
            if (StringUtils.hasText(param.getConnectionStatus())) {
                queryWrapper.eq("CONNECTION_STATUS", param.getConnectionStatus());
            }
        }

        queryWrapper.orderByDesc("CREATE_TIME");
        return this.page(page, queryWrapper);
    }
}
