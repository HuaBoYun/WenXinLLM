package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.financialProductDefinition.entity.TblFinancialProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 金融产品Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface TblFinancialProductMapper extends BaseMapper<TblFinancialProduct> {

    IPage<TblFinancialProduct> selectPageWithCategory(Page<TblFinancialProduct> page, @Param("params") Map<String, Object> params);

    TblFinancialProduct selectDetailById(@Param("productId") Long productId);

    /**
     * 检查产品编码唯一性
     * 使用注解方式定义SQL，避免XML映射文件加载问题
     */
    @Select("<script>" +
            "SELECT COUNT(1) " +
            "FROM TBL_FINANCIAL_PRODUCT " +
            "WHERE PRODUCT_CODE = #{productCode} " +
            "<if test='excludeId != null'>" +
            "AND PRODUCT_ID != #{excludeId} " +
            "</if>" +
            "</script>")
    int checkCodeUnique(@Param("productCode") String productCode, @Param("excludeId") Long excludeId);

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("productStatus") String productStatus, @Param("updateBy") String updateBy);

    int batchUpdateShelfStatus(@Param("ids") List<Long> ids, @Param("shelfStatus") String shelfStatus, @Param("updateBy") String updateBy);

    List<TblFinancialProduct> selectByCategoryId(@Param("categoryId") Long categoryId);

    Map<String, Object> getStatistics(@Param("orgId") Long orgId);
}

