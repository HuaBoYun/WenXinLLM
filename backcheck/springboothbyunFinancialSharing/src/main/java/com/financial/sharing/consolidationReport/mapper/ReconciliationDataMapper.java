package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.ReconciliationDataQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对账数据Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReconciliationDataMapper extends BaseMapper<TblReconciliationData> {

    /**
     * 查询对账数据列表
     * 
     * @param param 查询参数
     * @return 对账数据列表
     */
    List<TblReconciliationData> selectReconciliationList(@Param("param") ReconciliationDataQueryParam param);

    /**
     * 根据模型ID和期间删除对账数据
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 删除数量
     */
    int deleteByModelIdAndPeriod(@Param("modelId") String modelId, @Param("period") String period);

    /**
     * 批量插入对账数据
     * 
     * @param list 对账数据列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TblReconciliationData> list);

    /**
     * 根据模型ID和期间统计对账数据
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 统计结果Map(status -> count)
     */
    List<java.util.Map<String, Object>> countByStatus(@Param("modelId") String modelId, @Param("period") String period);
}

