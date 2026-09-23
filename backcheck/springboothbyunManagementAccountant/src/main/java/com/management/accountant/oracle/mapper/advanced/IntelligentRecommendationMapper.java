package com.management.accountant.oracle.mapper.advanced;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.advanced.IntelligentRecommendation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 智能推荐Mapper接口
 * 
 * @description 智能推荐数据访问层
 * @author AI Assistant
 * @date 2026-02-06
 */
@Mapper
public interface IntelligentRecommendationMapper extends BaseMapper<IntelligentRecommendation> {

    /**
     * 根据状态查询推荐列表
     * 
     * @param status 状态
     * @param companyId 公司ID
     * @return 推荐列表
     */
    List<IntelligentRecommendation> selectByStatus(@Param("status") String status, 
                                                    @Param("companyId") String companyId);

    /**
     * 根据公司ID查询推荐列表
     * 
     * @param companyId 公司ID
     * @return 推荐列表
     */
    List<IntelligentRecommendation> selectByCompanyId(@Param("companyId") String companyId);

    /**
     * 更新推荐状态
     * 
     * @param recommendationId 推荐ID
     * @param status 状态
     * @param userId 用户ID
     * @param userName 用户名称
     * @return 更新行数
     */
    int updateStatus(@Param("recommendationId") String recommendationId,
                    @Param("status") String status,
                    @Param("userId") String userId,
                    @Param("userName") String userName);

    /**
     * 获取统计数据
     * 
     * @param companyId 公司ID
     * @return 统计数据
     */
    Map<String, Object> getStatistics(@Param("companyId") String companyId);

    /**
     * 分页查询推荐列表
     * 
     * @param keyword 关键词
     * @param status 状态
     * @param companyId 公司ID
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 推荐列表
     */
    List<IntelligentRecommendation> selectPageList(@Param("keyword") String keyword,
                                                    @Param("status") String status,
                                                    @Param("companyId") String companyId,
                                                    @Param("offset") Integer offset,
                                                    @Param("pageSize") Integer pageSize);

    /**
     * 查询总记录数（自定义，避免与 BaseMapper.selectCount 冲突）
     *
     * @param keyword 关键词
     * @param status 状态
     * @param companyId 公司ID
     * @return 总记录数
     */
    Integer countByCondition(@Param("keyword") String keyword,
                             @Param("status") String status,
                             @Param("companyId") String companyId);

    /**
     * 批量删除推荐
     * 
     * @param ids 推荐ID列表
     * @return 删除行数
     */
    int batchDeleteByIds(@Param("ids") List<String> ids);
}

