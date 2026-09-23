package com.financial.sharing.oracle.mapper;

import com.financial.sharing.dto.param.SupplierQueryParam;
import com.financial.sharing.oracle.entity.TblSupplier;
import com.financial.sharing.vo.result.SupplierVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商Mapper
 * @author system
 * @since 2025-01-05
 */
public interface SupplierMapper {

    /**
     * 分页查询供应商
     * @param param 查询参数
     * @return 供应商列表
     */
    List<SupplierVO> selectPageList(@Param("param") SupplierQueryParam param);

    /**
     * 查询供应商详情
     * @param supplierId 供应商ID
     * @return 供应商详情
     */
    SupplierVO selectDetailById(@Param("supplierId") String supplierId);

    /**
     * 根据编码查询供应商
     * @param supplierCode 供应商编码
     * @return 供应商信息
     */
    TblSupplier selectByCode(@Param("supplierCode") String supplierCode);

    /**
     * 根据ID查询供应商
     * @param supplierId 供应商ID
     * @return 供应商信息
     */
    TblSupplier selectById(@Param("supplierId") String supplierId);

    /**
     * 查询所有启用的供应商
     * @return 供应商列表
     */
    List<SupplierVO> selectAllEnabled();

    /**
     * 查询供应商下拉列表
     * @param keyword 关键字
     * @return 供应商列表
     */
    List<SupplierVO> selectDropdownList(@Param("keyword") String keyword);

    /**
     * 插入供应商
     * @param supplier 供应商实体
     * @return 影响行数
     */
    int insertSupplier(TblSupplier supplier);

    /**
     * 更新供应商
     * @param supplier 供应商实体
     * @return 影响行数
     */
    int updateSupplier(TblSupplier supplier);
}

