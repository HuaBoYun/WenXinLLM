package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblCreditApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 授信申请Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface CreditApplicationMapper extends BaseMapper<TblCreditApplication> {

    /**
     * 分页查询授信申请列表
     *
     * @param params 查询参数
     * @return 授信申请列表
     */
    List<TblCreditApplication> selectApplicationList(Map<String, Object> params);

    /**
     * 根据ID查询授信申请详情
     *
     * @param applicationId 申请ID
     * @return 授信申请
     */
    TblCreditApplication selectApplicationById(@Param("applicationId") Long applicationId);

    /**
     * 根据申请编号查询
     *
     * @param applicationNo 申请编号
     * @return 授信申请
     */
    TblCreditApplication selectByApplicationNo(@Param("applicationNo") String applicationNo);

    /**
     * 更新申请状态
     *
     * @param applicationId 申请ID
     * @param status 状态
     * @return 影响行数
     */
    int updateApplicationStatus(@Param("applicationId") Long applicationId, @Param("status") String status);

    /**
     * 批量删除授信申请（逻辑删除）
     *
     * @param applicationIds 申请ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("applicationIds") List<Long> applicationIds);

    /**
     * 查询授信申请汇总
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> selectApplicationSummary(@Param("companyId") Long companyId);
}

