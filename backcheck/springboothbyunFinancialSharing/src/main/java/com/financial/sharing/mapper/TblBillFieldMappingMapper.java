package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblBillFieldMapping;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 字段映射Mapper
 */
public interface TblBillFieldMappingMapper extends BaseMapper<TblBillFieldMapping> {

    /**
     * 根据配置ID查询
     */
    List<TblBillFieldMapping> selectByConfigId(@Param("configId") String configId);

    /**
     * 根据配置ID查询，按排序号排序
     */
    List<TblBillFieldMapping> selectByConfigIdOrderBySort(@Param("configId") String configId);

    /**
     * 根据源字段查询
     */
    List<TblBillFieldMapping> selectBySourceField(@Param("sourceField") String sourceField);

    /**
     * 根据目标字段查询
     */
    List<TblBillFieldMapping> selectByTargetField(@Param("targetField") String targetField);

    /**
     * 根据配置ID删除映射
     */
    int deleteByConfigId(@Param("configId") String configId);
}
