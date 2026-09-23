package com.global.treasurer.service;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtDirectConnectAuth;

import java.util.Map;

/**
 * 全球司库-直联授权Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtDirectConnectAuthService extends IService<TblGtDirectConnectAuth> {

    /**
     * 分页查询直联授权列表
     *
     * @param page 分页对象
     * @param accountNumber 账户号码
     * @param authType 授权类型
     * @param authStatus 授权状态
     * @return 分页结果
     */
    IPage<TblGtDirectConnectAuth> getPageList(Page<TblGtDirectConnectAuth> page,
                                              String accountNumber,
                                              String authType,
                                              String authStatus);

    /**
     * 新增直联授权
     *
     * @param entity 直联授权实体
     * @return 是否成功
     */
    boolean saveDirectConnectAuth(TblGtDirectConnectAuth entity);

    /**
     * 更新直联授权
     *
     * @param entity 直联授权实体
     * @return 是否成功
     */
    boolean updateDirectConnectAuth(TblGtDirectConnectAuth entity);

    /**
     * 删除直联授权
     *
     * @param authId 授权ID
     * @return 是否成功
     */
    boolean deleteDirectConnectAuth(Long authId);

    /**
     * 审批直联授权
     *
     * @param authId 授权ID
     * @param approverId 审批人ID
     * @param approvalOpinion 审批意见
     * @param authStatus 审批状态
     * @return 是否成功
     */
    boolean approveDirectConnectAuth(Long authId, Long approverId,
                                      String approvalOpinion, String authStatus);

    /**
     * 测试银行连接
     *
     * @param authId 授权ID
     * @return 连接测试结果
     */
    Map<String, Object> testBankConnection(Long authId);

    /**
     * 同步连接状态
     *
     * @param authId 授权ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean syncConnectionStatus(Long authId, Long operatorId);
}
