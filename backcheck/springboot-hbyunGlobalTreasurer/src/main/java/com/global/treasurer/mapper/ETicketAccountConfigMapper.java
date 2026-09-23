package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.ETicketAccountConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 电票账户配置Mapper
 *
 * @author AI Developer
 * @date 2025-02-26
 */
@Mapper
public interface ETicketAccountConfigMapper extends BaseMapper<ETicketAccountConfig> {

    /**
     * 分页查询电票账户列表
     *
     * @param param 查询参数
     * @return 电票账户列表
     */
    List<ETicketAccountConfig> selectAccountPage(@Param("param") Map<String, Object> param);

    /**
     * 批量删除电票账户（软删除）
     *
     * @param ids 账户ID列表（VARCHAR2主键）
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<String> ids);

    /**
     * 同步账户状态
     *
     * @param id 账户ID（VARCHAR2主键）
     * @return 同步结果
     */
    Map<String, Object> syncAccountStatus(@Param("id") String id);

    /**
     * 查询账户统计信息（正常账户数、总授信额度、今日新增数）
     *
     * @return 统计结果
     */
    Map<String, Object> selectAccountStatistics();
}
