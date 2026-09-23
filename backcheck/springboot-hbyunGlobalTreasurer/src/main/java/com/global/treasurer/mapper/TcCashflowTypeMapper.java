package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcCashflowType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * 现金流类型表 Mapper接口
 * @author hbyun-admin
 * @date 2024-01-01
 */
@Mapper
public interface TcCashflowTypeMapper extends BaseMapper<TcCashflowType> {

    /**
     * 根据类型编码查询现金流类型
     * @param typeCode 类型编码
     * @return 现金流类型信息
     */
    TcCashflowType selectByTypeCode(@Param("typeCode") String typeCode);

    /**
     * 根据分类查询现金流类型列表
     * @param category 分类
     * @return 现金流类型列表
     */
    List<TcCashflowType> selectByCategory(@Param("category") String category);

    /**
     * 根据流向查询现金流类型列表
     * @param flowDirection 流向
     * @return 现金流类型列表
     */
    List<TcCashflowType> selectByFlowDirection(@Param("flowDirection") String flowDirection);

    /**
     * 根据流性质查询现金流类型列表
     * @param flowNature 流性质
     * @return 现金流类型列表
     */
    List<TcCashflowType> selectByFlowNature(@Param("flowNature") String flowNature);

    /**
     * 检查类型编码是否存在
     * @param typeCode 类型编码
     * @param excludeId 排除的ID
     * @return 数量
     */
    int checkTypeCodeExists(@Param("typeCode") String typeCode, @Param("excludeId") String excludeId);

    /**
     * 根据适用产品查询现金流类型
     * @param productId 产品ID
     * @return 现金流类型列表
     */
    List<TcCashflowType> selectByApplicableProduct(@Param("productId") String productId);

    /**
     * 查询排序后的现金流类型列表
     * @param status 状态
     * @return 现金流类型列表
     */
    List<TcCashflowType> selectOrderedList(@Param("status") String status);
}
