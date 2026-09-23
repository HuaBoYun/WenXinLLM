package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.entity.TblEquityStructure;

import java.util.List;

/**
 * 股权结构Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EquityStructureService {

    /**
     * 计算股权结构
     * 
     * @param modelId 模型ID
     */
    void calculateEquityStructure(String modelId);

    /**
     * 查询股权结构列表
     * 
     * @param modelId 模型ID
     * @return 股权结构列表
     */
    List<TblEquityStructure> getEquityStructureList(String modelId);

    /**
     * 查询股权结构树
     * 
     * @param modelId 模型ID
     * @return 股权结构树
     */
    List<TblEquityStructure> getEquityStructureTree(String modelId);

    /**
     * 删除股权结构
     * 
     * @param modelId 模型ID
     */
    void deleteEquityStructure(String modelId);
}

