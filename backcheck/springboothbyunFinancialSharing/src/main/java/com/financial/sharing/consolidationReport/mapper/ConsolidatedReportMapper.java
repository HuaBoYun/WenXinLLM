package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.ConsolidatedReportQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidatedReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合并报表Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ConsolidatedReportMapper extends BaseMapper<TblConsolidatedReport> {

    /**
     * 查询合并报表列表
     * 
     * @param param 查询参数
     * @return 合并报表列表
     */
    List<TblConsolidatedReport> selectReportList(@Param("param") ConsolidatedReportQueryParam param);

    /**
     * 根据模型ID、期间和报表类型删除合并报表
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @param reportType 报表类型
     * @return 删除数量
     */
    int deleteByModelIdAndPeriodAndType(@Param("modelId") String modelId, 
                                        @Param("period") String period, 
                                        @Param("reportType") String reportType);

    /**
     * 批量插入合并报表
     * 
     * @param list 合并报表列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TblConsolidatedReport> list);

    /**
     * 根据模型ID和期间查询报表类型列表
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 报表类型列表
     */
    List<String> selectReportTypeList(@Param("modelId") String modelId, @Param("period") String period);
}

