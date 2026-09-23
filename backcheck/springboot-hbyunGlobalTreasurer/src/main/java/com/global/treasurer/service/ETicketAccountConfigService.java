package com.global.treasurer.service;

import com.global.treasurer.entity.ETicketAccountConfig;
import java.util.List;
import java.util.Map;

/**
 * 电票账户配置服务接口
 *
 * @author AI Developer
 * @date 2025-02-26
 */
public interface ETicketAccountConfigService {

    /**
     * 分页查询电票账户列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Map<String, Object> getAccountPage(Map<String, Object> param);

    /**
     * 根据ID查询电票账户
     */
    ETicketAccountConfig getAccountById(String id);

    /**
     * 创建电票账户
     */
    int createAccount(ETicketAccountConfig account);

    /**
     * 更新电票账户
     */
    int updateAccount(ETicketAccountConfig account);

    /**
     * 批量删除电票账户
     */
    int batchDelete(List<String> ids);

    /**
     * 同步账户状态
     */
    Map<String, Object> syncAccountStatus(String id);

    /**
     * 获取账户统计信息
     */
    Map<String, Object> getAccountStatistics();
}
