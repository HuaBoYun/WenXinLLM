package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.BusinessSystemRegister;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 业务系统注册Mapper
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Mapper
public interface BusinessSystemRegisterMapper extends BaseMapper<BusinessSystemRegister> {

    /**
     * 分页查询业务系统列表
     *
     * @param param 查询参数
     * @return 业务系统列表
     */
    List<BusinessSystemRegister> selectSystemPage(@Param("param") Map<String, Object> param);

    /**
     * 批量删除业务系统（软删除）
     *
     * @param ids 系统ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 测试连接
     *
     * @param id 系统ID
     * @return 测试结果
     */
    Map<String, Object> testConnection(@Param("id") Long id);
}
