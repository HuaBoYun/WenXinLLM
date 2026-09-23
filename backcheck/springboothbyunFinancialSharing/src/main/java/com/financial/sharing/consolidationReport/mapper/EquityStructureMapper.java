package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.entity.TblEquityStructure;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 股权结构Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EquityStructureMapper extends BaseMapper<TblEquityStructure> {

    /**
     * 根据模型ID查询股权结构列表
     * 
     * @param modelId 模型ID
     * @return 股权结构列表
     */
    List<TblEquityStructure> selectByModelId(@Param("modelId") String modelId);

    /**
     * 根据模型ID删除股权结构
     * 
     * @param modelId 模型ID
     * @return 删除数量
     */
    int deleteByModelId(@Param("modelId") String modelId);

    /**
     * 批量插入股权结构
     * 
     * @param list 股权结构列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TblEquityStructure> list);
}

