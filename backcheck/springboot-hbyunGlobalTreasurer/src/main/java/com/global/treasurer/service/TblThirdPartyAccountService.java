package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblThirdPartyAccount;
import com.global.treasurer.vo.param.TblThirdPartyAccountQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 第三方账户管理Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
public interface TblThirdPartyAccountService extends IService<TblThirdPartyAccount> {

    /**
     * 分页查询第三方账户
     *
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<TblThirdPartyAccount> selectPage(TblThirdPartyAccountQueryParam param);

    /**
     * 根据ID查询
     *
     * @param id 主键
     * @return 实体对象
     */
    TblThirdPartyAccount selectById(String id);

    /**
     * 查询列表
     *
     * @param param 查询参数
     * @return 列表结果
     */
    List<TblThirdPartyAccount> selectList(TblThirdPartyAccountQueryParam param);

    /**
     * 插入
     *
     * @param account 实体对象
     * @return 影响行数
     */
    int insert(TblThirdPartyAccount account);

    /**
     * 更新
     *
     * @param account 实体对象
     * @return 影响行数
     */
    int update(TblThirdPartyAccount account);

    /**
     * 删除
     *
     * @param id 主键
     * @return 影响行数
     */
    int delete(String id);

    /**
     * 同步状态
     *
     * @param id 主键
     * @return 影响行数
     */
    int syncStatus(String id);

    /**
     * 批量同步
     *
     * @param ids 主键列表
     * @return 影响行数
     */
    int batchSync(List<String> ids);

    /**
     * 测试连接
     *
     * @param id 主键
     * @return 是否连接成功
     */
    boolean testConnection(String id);

    /**
     * 获取第三方系统选项
     *
     * @return 系统选项列表
     */
    List<Map<String, String>> getSystems();

    /**
     * 获取账户类型选项
     *
     * @return 账户类型选项列表
     */
    List<Map<String, String>> getAccountTypes();

    /**
     * 分页查询第三方账户（BasicConfigController使用）
     *
     * @param page 页码
     * @param limit 每页数量
     * @param accountCode 账户编码
     * @param accountName 账户名称
     * @param thirdPartySystem 第三方系统
     * @param accountType 账户类型
     * @param connectionStatus 连接状态
     * @param isEnabled 是否启用
     * @return 分页结果
     */
    IPage<TblThirdPartyAccount> getThirdPartyAccountPage(Integer page, Integer limit, String accountCode, String accountName, String thirdPartySystem, String accountType, String connectionStatus, Integer isEnabled);
}