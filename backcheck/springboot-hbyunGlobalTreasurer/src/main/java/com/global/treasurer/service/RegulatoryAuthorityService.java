package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryAuthority;

import java.util.List;
import java.util.Map;

/**
 * 监管机构服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface RegulatoryAuthorityService {

    /**
     * 分页查询监管机构列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblRegulatoryAuthority> getAuthorityList(Map<String, Object> params);

    /**
     * 根据ID查询监管机构详情
     *
     * @param authorityId 机构ID
     * @return 监管机构
     */
    TblRegulatoryAuthority getAuthorityById(String authorityId);

    /**
     * 保存监管机构（新增或更新）
     *
     * @param authority 监管机构
     * @return 保存后的监管机构
     */
    TblRegulatoryAuthority saveAuthority(TblRegulatoryAuthority authority);

    /**
     * 删除监管机构
     *
     * @param authorityId 机构ID
     */
    void deleteAuthority(String authorityId);

    /**
     * 批量删除监管机构
     *
     * @param authorityIds 机构ID列表
     */
    void batchDeleteAuthorities(List<String> authorityIds);

    /**
     * 根据机构类型查询
     *
     * @param authorityType 机构类型
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> getAuthoritiesByType(String authorityType);

    /**
     * 查询活跃的监管机构
     *
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> getActiveAuthorities();

    /**
     * 查询重要监管机构
     *
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> getImportantAuthorities();

    /**
     * 激活/停用监管机构
     *
     * @param authorityId 机构ID
     * @param isActive 是否激活
     */
    void toggleAuthorityStatus(String authorityId, Integer isActive);

    /**
     * 查询导出列表（不分页）
     *
     * @param params 查询参数
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> exportAuthorityList(Map<String, Object> params);
}

