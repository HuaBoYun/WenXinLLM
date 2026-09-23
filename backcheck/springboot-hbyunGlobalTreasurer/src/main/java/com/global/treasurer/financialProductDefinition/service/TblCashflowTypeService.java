package com.global.treasurer.financialProductDefinition.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.financialProductDefinition.entity.TblCashflowType;

import java.util.List;
import java.util.Map;

/**
 * 现金流类型管理Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface TblCashflowTypeService extends IService<TblCashflowType> {

    /**
     * 分页查询现金流类型列表
     */
    IPage<TblCashflowType> getPage(Integer pageNo, Integer pageSize, String cashflowTypeCode,
                                    String cashflowTypeName, String cashflowDirection,
                                    String businessCategory, String impactType, Long orgId);

    /**
     * 根据ID查询详情
     */
    TblCashflowType getDetail(Long id);

    /**
     * 创建现金流类型
     */
    TblCashflowType create(TblCashflowType entity, String createBy);

    /**
     * 更新现金流类型
     */
    boolean update(TblCashflowType entity, String updateBy);

    /**
     * 删除现金流类型
     */
    boolean delete(Long id);

    /**
     * 批量删除
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新状态
     */
    boolean updateStatus(Long id, Integer isEnabled, String updateBy);

    /**
     * 批量更新状态
     */
    boolean batchUpdateStatus(List<Long> ids, Integer isEnabled, String updateBy);

    /**
     * 获取启用的列表
     */
    List<TblCashflowType> getEnabledList(Long orgId);

    /**
     * 检查编码唯一性
     */
    boolean checkCodeUnique(String cashflowTypeCode, Long excludeId);

    /**
     * 获取树形结构
     */
    List<TblCashflowType> getTree(Long orgId);

    /**
     * 根据父级ID查询子级
     */
    List<TblCashflowType> getByParentId(Long parentId, Long orgId);

    /**
     * 排序
     */
    boolean sort(List<TblCashflowType> list);

    /**
     * 复制
     */
    TblCashflowType copy(Long id, String newCode, String newName, String createBy);

    /**
     * 获取统计信息
     */
    Map<String, Object> getStatistics(Long orgId);

    /**
     * 验证是否可以删除
     */
    Map<String, Object> validateDelete(Long id);

    /**
     * 获取使用情况
     */
    Map<String, Object> getUsage(Long id);
}

