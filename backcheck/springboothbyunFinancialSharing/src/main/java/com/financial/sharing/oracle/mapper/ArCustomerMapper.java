package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArCustomerEntity;
import com.financial.sharing.vo.param.ArCustomerQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 客户档案Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArCustomerMapper extends BaseMapper<ArCustomerEntity> {

    /**
     * 分页查询客户档案
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<ArCustomerEntity> selectCustomerPage(Page<ArCustomerEntity> page, @Param("param") ArCustomerQueryParam param);

    /**
     * 查询客户档案列表（配合PageHelper使用）
     * @param param 查询参数
     * @return 客户列表
     */
    List<ArCustomerEntity> selectCustomerList(@Param("param") ArCustomerQueryParam param);

    /**
     * 根据客户编码查询
     * @param customerCode 客户编码
     * @param tenantId 租户ID
     * @return 客户实体
     */
    ArCustomerEntity selectByCustomerCode(@Param("customerCode") String customerCode, @Param("tenantId") Long tenantId);

    /**
     * 检查客户编码是否存在
     * @param customerCode 客户编码
     * @param customerId 客户ID（排除自身）
     * @param tenantId 租户ID
     * @return 数量
     */
    int checkCustomerCodeExists(@Param("customerCode") String customerCode, 
                                @Param("customerId") String customerId, 
                                @Param("tenantId") Long tenantId);

    /**
     * 查询客户应收统计
     * @param customerId 客户ID
     * @return 统计结果
     */
    Map<String, Object> selectCustomerReceivableStats(@Param("customerId") String customerId);

    /**
     * 批量更新客户状态
     * @param customerIds 客户ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("customerIds") List<String> customerIds, @Param("status") Integer status);
}

