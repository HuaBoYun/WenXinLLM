package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务账簿Mapper接口
 */
public interface FinanceBookMapper {
    
    /**
     * 查询账簿列表
     */
    List<Map<String, Object>> selectBookList(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询账簿详情
     */
    Map<String, Object> selectBookById(@Param("bookId") String bookId);
    
    /**
     * 插入账簿
     */
    int insertBook(@Param("book") Map<String, Object> book);
    
    /**
     * 更新账簿
     */
    int updateBook(@Param("book") Map<String, Object> book);
    
    /**
     * 删除账簿
     */
    int deleteBook(@Param("bookId") String bookId);
    
    /**
     * 查询账簿统计
     */
    List<Map<String, Object>> selectBookStatistics(@Param("params") Map<String, Object> params);

    // ==================== 账簿管理相关方法 ====================

    /**
     * 查询账簿管理列表
     */
    List<Map<String, Object>> selectZbglList(@Param("params") Map<String, Object> params);

    /**
     * 统计账簿管理总数
     */
    int countZbglList(@Param("params") Map<String, Object> params);

    /**
     * 查询账簿管理详情
     */
    Map<String, Object> selectZbglDetail(@Param("params") Map<String, Object> params);

    /**
     * 插入账簿管理
     */
    int insertZbgl(@Param("data") Map<String, Object> data);

    /**
     * 删除账簿管理
     */
    int deleteZbgl(@Param("data") Map<String, Object> data);

    // ==================== 账簿类型管理相关方法 ====================

    /**
     * 查询账簿类型列表
     */
    List<Map<String, Object>> selectZblxList(@Param("params") Map<String, Object> params);

    /**
     * 统计账簿类型总数
     */
    int countZblxList(@Param("params") Map<String, Object> params);

    /**
     * 查询账簿类型详情
     */
    Map<String, Object> selectZblxDetail(@Param("params") Map<String, Object> params);

    /**
     * 插入账簿类型
     */
    int insertZblx(@Param("data") Map<String, Object> data);

    /**
     * 删除账簿类型
     */
    int deleteZblx(@Param("data") Map<String, Object> data);

    // ==================== 账簿授权管理相关方法 ====================

    /**
     * 插入账簿授权角色
     */
    int insertZbglAuthRole(@Param("data") Map<String, Object> data);

    /**
     * 查询账簿授权角色列表
     */
    List<Map<String, Object>> selectZbglAuthRoleList(@Param("params") Map<String, Object> params);

    /**
     * 统计账簿授权角色总数
     */
    int countZbglAuthRoleList(@Param("params") Map<String, Object> params);

    /**
     * 删除账簿授权角色
     */
    int deleteZbglAuthRole(@Param("data") Map<String, Object> data);

    /**
     * 查询公司账簿授权角色列表
     */
    List<Map<String, Object>> selectGsZbglAuthRoleList(@Param("params") Map<String, Object> params);

    /**
     * 统计公司账簿授权角色总数
     */
    int countGsZbglAuthRoleList(@Param("params") Map<String, Object> params);

    /**
     * 更新公司账簿选择
     */
    int updateGsZbSelection(@Param("data") Map<String, Object> data);
}