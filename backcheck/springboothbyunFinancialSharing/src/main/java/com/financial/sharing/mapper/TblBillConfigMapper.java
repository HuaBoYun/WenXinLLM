package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblBillConfig;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 单据配置Mapper
 */
public interface TblBillConfigMapper extends BaseMapper<TblBillConfig> {

    /**
     * 根据配置编码查询
     */
    TblBillConfig selectByConfigCode(@Param("configCode") String configCode);

    /**
     * 根据账单类型查询
     */
    List<TblBillConfig> selectByBillType(@Param("billType") String billType);

    /**
     * 根据组织ID查询
     */
    List<TblBillConfig> selectByOrgId(@Param("orgId") String orgId);

    /**
     * 根据是否启用查询
     */
    List<TblBillConfig> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据账单类型和组织ID查询
     */
    List<TblBillConfig> selectByBillTypeAndOrgId(@Param("billType") String billType, @Param("orgId") String orgId);
}
