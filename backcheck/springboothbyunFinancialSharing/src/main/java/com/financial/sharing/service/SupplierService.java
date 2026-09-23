package com.financial.sharing.service;

import com.financial.sharing.dto.param.SupplierQueryParam;
import com.financial.sharing.dto.param.SupplierSaveParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.SupplierVO;

import java.util.List;

/**
 * 供应商Service接口
 * @author system
 * @since 2025-01-05
 */
public interface SupplierService {

    /**
     * 分页查询供应商
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<SupplierVO> queryPage(SupplierQueryParam param);

    /**
     * 查询供应商详情
     * @param supplierId 供应商ID
     * @return 供应商详情
     */
    SupplierVO getDetail(String supplierId);

    /**
     * 保存供应商(新增或修改)
     * @param param 保存参数
     * @return 供应商ID
     */
    String save(SupplierSaveParam param);

    /**
     * 删除供应商
     * @param supplierId 供应商ID
     */
    void delete(String supplierId);

    /**
     * 查询所有启用的供应商
     * @return 供应商列表
     */
    List<SupplierVO> queryAllEnabled();

    /**
     * 查询供应商下拉列表
     * @param keyword 关键字
     * @return 供应商列表
     */
    List<SupplierVO> queryDropdownList(String keyword);

    /**
     * 更新供应商状态
     * @param supplierId 供应商ID
     * @param status 状态
     */
    void updateStatus(String supplierId, Integer status);
}

