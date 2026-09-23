package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.DataMappingConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 数据映射配置Mapper
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Mapper
public interface DataMappingConfigMapper extends BaseMapper<DataMappingConfig> {

    /**
     * 分页查询数据映射列表
     *
     * @param param 查询参数
     * @return 数据映射列表
     */
    List<DataMappingConfig> selectMappingPage(@Param("param") Map<String, Object> param);

    /**
     * 批量删除数据映射（软删除）
     *
     * @param ids 映射ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 测试数据映射
     *
     * @param param 测试参数
     * @return 测试结果
     */
    Map<String, Object> testMapping(@Param("param") Map<String, Object> param);
}
