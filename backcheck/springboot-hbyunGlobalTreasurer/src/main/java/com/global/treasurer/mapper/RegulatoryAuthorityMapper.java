package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblRegulatoryAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 监管机构Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface RegulatoryAuthorityMapper extends BaseMapper<TblRegulatoryAuthority> {

    /**
     * 分页查询监管机构列表
     *
     * @param params 查询参数
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> selectAuthorityList(Map<String, Object> params);

    /**
     * 根据ID查询监管机构详情
     *
     * @param authorityId 机构ID
     * @return 监管机构
     */
    TblRegulatoryAuthority selectAuthorityById(@Param("authorityId") String authorityId);

    /**
     * 根据机构代码查询
     *
     * @param authorityCode 机构代码
     * @return 监管机构
     */
    TblRegulatoryAuthority selectByAuthorityCode(@Param("authorityCode") String authorityCode);

    /**
     * 根据机构类型查询
     *
     * @param authorityType 机构类型
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> selectByAuthorityType(@Param("authorityType") String authorityType);

    /**
     * 查询活跃的监管机构
     *
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> selectActiveAuthorities();

    /**
     * 查询重要监管机构
     *
     * @return 监管机构列表
     */
    List<TblRegulatoryAuthority> selectImportantAuthorities();

    /**
     * 批量删除监管机构（逻辑删除）
     *
     * @param authorityIds 机构ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("authorityIds") List<String> authorityIds);

    /**
     * 更新机构状态
     *
     * @param authorityId 机构ID
     * @param isActive 是否激活
     * @return 影响行数
     */
    int updateAuthorityStatus(@Param("authorityId") String authorityId, @Param("isActive") Integer isActive);
}

