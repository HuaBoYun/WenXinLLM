package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.BankReconciliation;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 银行对账Mapper
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface BankReconciliationMapper extends BaseMapper<BankReconciliation> {

    /**
     * 分页查询列表
     *
     * @param param 查询参数
     * @return 列表
     */
    List<BankReconciliation> selectPage(@Param("param") Map<String, Object> param);

    /**
     * 批量删除（软删除）
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);
}
