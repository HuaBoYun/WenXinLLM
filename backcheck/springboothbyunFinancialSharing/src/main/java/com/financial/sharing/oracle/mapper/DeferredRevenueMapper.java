package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.DeferredRevenueEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 递延收入 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2024-12-19
 */
@Component("oracleDeferredRevenueMapper")
public interface DeferredRevenueMapper extends BaseMapper<DeferredRevenueEntity> {

    /**
     * 查询递延收入列表（返回Map，用于手动分页）
     *
     * @param param 查询参数
     * @return 递延收入列表
     */
    List<Map<String, Object>> selectDeferredRevenueMapList(@Param("param") Map<String, Object> param);

    /**
     * 查询递延收入列表（不分页）
     *
     * @param param 查询参数
     * @return 递延收入列表
     */
    List<DeferredRevenueEntity> selectDeferredRevenueList(@Param("param") Map<String, Object> param);

    /**
     * 查询递延收入统计信息
     *
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> selectDeferredRevenueStats(@Param("param") Map<String, Object> param);
}

