package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblProjectSettlement;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 项目结算Mapper
 */
public interface TblProjectSettlementMapper extends BaseMapper<TblProjectSettlement> {
    
    /**
     * 根据项目ID查询结算记录
     */
    List<TblProjectSettlement> selectByProjectId(@Param("projectId") String projectId);
    
    /**
     * 根据结算状态查询
     */
    List<TblProjectSettlement> selectBySettlementStatus(@Param("settlementStatus") String settlementStatus);
    
    /**
     * 获取项目的最新结算记录
     */
    TblProjectSettlement selectLatestSettlement(@Param("projectId") String projectId);
}

