package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.BiddingProject;
import com.huabo.contract.vo.BiddingProjectQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 招投标项目表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface BiddingProjectMapper extends BaseMapper<BiddingProject> {

    /**
     * 分页查询招投标项目列表
     * 
     * @param param 查询参数
     * @return 招投标项目列表
     */
    List<BiddingProject> selectBiddingProjectList(@Param("param") BiddingProjectQueryParam param);

    /**
     * 根据项目编号查询招投标项目
     * 
     * @param projectNo 项目编号
     * @return 招投标项目
     */
    BiddingProject selectByProjectNo(@Param("projectNo") String projectNo);

    /**
     * 根据招标方ID查询招投标项目列表
     * 
     * @param tendererId 招标方ID
     * @return 招投标项目列表
     */
    List<BiddingProject> selectByTendererId(@Param("tendererId") Long tendererId);

    /**
     * 检查招标编号是否存在
     *
     * @param biddingNo 招标编号
     * @param excludeId 排除的ID
     * @return 存在返回true，不存在返回false
     */
    boolean existsBiddingNo(@Param("biddingNo") String biddingNo, @Param("excludeId") Long excludeId);

    /**
     * 获取指定日期前缀的最大招标编号
     *
     * @param prefix 编号前缀
     * @return 最大招标编号
     */
    String getMaxBiddingNoByDate(@Param("prefix") String prefix);

    /**
     * 获取可投标的项目列表
     * 
     * @return 可投标的项目列表
     */
    List<BiddingProject> selectCanBidProjects();

    /**
     * 获取已过期的项目列表
     * 
     * @return 已过期的项目列表
     */
    List<BiddingProject> selectExpiredProjects();

    /**
     * 获取重点项目列表
     * 
     * @param minBudgetAmount 最小预算金额
     * @return 重点项目列表
     */
    List<BiddingProject> selectKeyProjects(@Param("minBudgetAmount") java.math.BigDecimal minBudgetAmount);

    /**
     * 获取紧急项目列表（距离截止时间<=3天）
     * 
     * @return 紧急项目列表
     */
    List<BiddingProject> selectUrgentProjects();

    /**
     * 获取我参与的项目列表
     * 
     * @param userId 用户ID
     * @return 我参与的项目列表
     */
    List<BiddingProject> selectMyParticipateProjects(@Param("userId") Long userId);

    /**
     * 获取中标项目列表
     * 
     * @param userId 用户ID（可选）
     * @return 中标项目列表
     */
    List<BiddingProject> selectWinningProjects(@Param("userId") Long userId);

    /**
     * 批量更新项目状态
     * 
     * @param ids 项目ID列表
     * @param projectStatus 项目状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateProjectStatus(@Param("ids") List<Long> ids, 
                                @Param("projectStatus") Integer projectStatus, 
                                @Param("updateBy") Long updateBy);

    /**
     * 批量更新参与状态
     * 
     * @param ids 项目ID列表
     * @param isParticipate 是否参与投标
     * @param participateStatus 参与状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateParticipateStatus(@Param("ids") List<Long> ids, 
                                    @Param("isParticipate") Integer isParticipate,
                                    @Param("participateStatus") Integer participateStatus, 
                                    @Param("updateBy") Long updateBy);

    /**
     * 统计招投标项目数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectBiddingProjectStatistics(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取项目类型分布统计
     * 
     * @param param 查询参数
     * @return 项目类型分布
     */
    List<Map<String, Object>> selectProjectTypeDistribution(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取项目状态分布统计
     * 
     * @param param 查询参数
     * @return 项目状态分布
     */
    List<Map<String, Object>> selectProjectStatusDistribution(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取参与状态分布统计
     * 
     * @param param 查询参数
     * @return 参与状态分布
     */
    List<Map<String, Object>> selectParticipateStatusDistribution(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取月度项目趋势
     * 
     * @param param 查询参数
     * @return 月度项目趋势
     */
    List<Map<String, Object>> selectMonthlyProjectTrend(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取预算金额分布统计
     * 
     * @param param 查询参数
     * @return 预算金额分布
     */
    List<Map<String, Object>> selectBudgetAmountDistribution(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取招标方分布统计
     * 
     * @param param 查询参数
     * @return 招标方分布
     */
    List<Map<String, Object>> selectTendererDistribution(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取地区分布统计
     * 
     * @param param 查询参数
     * @return 地区分布
     */
    List<Map<String, Object>> selectRegionDistribution(@Param("param") BiddingProjectQueryParam param);

    /**
     * 获取行业分布统计
     * 
     * @param param 查询参数
     * @return 行业分布
     */
    List<Map<String, Object>> selectIndustryDistribution(@Param("param") BiddingProjectQueryParam param);

    /**
     * 模糊搜索招投标项目
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 招投标项目列表
     */
    List<BiddingProject> searchBiddingProjects(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 获取即将截止的项目列表
     * 
     * @param days 天数
     * @return 即将截止的项目列表
     */
    List<BiddingProject> selectProjectsExpiringSoon(@Param("days") Integer days);

    /**
     * 获取今日开标的项目列表
     * 
     * @return 今日开标的项目列表
     */
    List<BiddingProject> selectTodayBidOpeningProjects();

    /**
     * 获取本周开标的项目列表
     * 
     * @return 本周开标的项目列表
     */
    List<BiddingProject> selectThisWeekBidOpeningProjects();

    /**
     * 更新项目参与状态
     * 
     * @param id 项目ID
     * @param isParticipate 是否参与投标
     * @param participateStatus 参与状态
     * @return 更新数量
     */
    int updateParticipateStatus(@Param("id") Long id, 
                               @Param("isParticipate") Integer isParticipate,
                               @Param("participateStatus") Integer participateStatus);
}
