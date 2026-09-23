package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.RevenueBatchSettlementEntity;
import com.financial.sharing.vo.param.RevenueBatchSettlementQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 收入批量结账Mapper接口
 *
 * @author AI Agent
 * @since 2025-11-29
 */
@Component("oracleRevenueBatchSettlementMapper")
public interface RevenueBatchSettlementMapper extends BaseMapper<RevenueBatchSettlementEntity> {
    
    /**
     * 分页查询收入批量结账列表
     * 
     * @param page 分页对象
     * @param param 查询参数
     * @return 分页结果
     */
    Page<Map<String, Object>> selectSettlementPage(Page<Map<String, Object>> page, @Param("param") RevenueBatchSettlementQueryParam param);
    
    /**
     * 查询收入批量结账列表
     * 
     * @param param 查询参数
     * @return 结果列表
     */
    List<Map<String, Object>> selectSettlementList(@Param("param") RevenueBatchSettlementQueryParam param);
    
    /**
     * 根据ID查询结账详情
     * 
     * @param settlementId 结账ID
     * @return 结账详情
     */
    Map<String, Object> selectSettlementById(@Param("settlementId") Long settlementId);
}

