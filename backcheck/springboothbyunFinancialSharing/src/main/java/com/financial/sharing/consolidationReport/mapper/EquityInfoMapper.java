package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.EquityInfoQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEquityInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 股权信息Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EquityInfoMapper extends BaseMapper<TblEquityInfo> {

    /**
     * 查询股权信息列表
     * 
     * @param param 查询参数
     * @return 股权信息列表
     */
    List<TblEquityInfo> selectEquityList(@Param("param") EquityInfoQueryParam param);

    /**
     * 检查股权关系是否已存在
     * 
     * @param modelId 模型ID
     * @param parentOrgId 母公司ID
     * @param subsidiaryOrgId 子公司ID
     * @param excludeEquityId 排除的股权信息ID(用于编辑时排除自己)
     * @return 数量
     */
    int checkEquityExists(@Param("modelId") String modelId,
                         @Param("parentOrgId") String parentOrgId,
                         @Param("subsidiaryOrgId") String subsidiaryOrgId,
                         @Param("excludeEquityId") String excludeEquityId);

    /**
     * 根据模型ID查询股权信息列表(用于股权结构计算)
     * 
     * @param modelId 模型ID
     * @return 股权信息列表
     */
    List<TblEquityInfo> selectByModelId(@Param("modelId") String modelId);

    /**
     * 根据模型ID删除股权信息
     * 
     * @param modelId 模型ID
     * @return 删除数量
     */
    int deleteByModelId(@Param("modelId") String modelId);
}

