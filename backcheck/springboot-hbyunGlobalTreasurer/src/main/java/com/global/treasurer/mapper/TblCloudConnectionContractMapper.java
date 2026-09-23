package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblCloudConnectionContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 云连接合同Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
@Mapper
public interface TblCloudConnectionContractMapper extends BaseMapper<TblCloudConnectionContract> {

    /**
     * 分页查询云连接合同
     *
     * @param params 查询参数
     * @return 合同列表
     */
    List<TblCloudConnectionContract> selectCloudContractPage(Map<String, Object> params);

    /**
     * 统计云连接合同数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countCloudContractList(Map<String, Object> params);

    /**
     * 获取合同统计数据
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    Map<String, Object> selectCloudContractStatistics(@Param("orgId") Long orgId);

    /**
     * 获取即将到期的合同
     *
     * @param days 天数
     * @return 合同列表
     */
    List<TblCloudConnectionContract> selectExpiringContracts(@Param("days") Integer days);
}
