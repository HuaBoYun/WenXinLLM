package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.BiddingProject;
import com.huabo.contract.vo.BiddingProjectQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 招投标项目表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface BiddingProjectService extends IService<BiddingProject> {

    /**
     * 分页查询招投标项目列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<BiddingProject> getBiddingProjectList(BiddingProjectQueryParam param);

    /**
     * 保存招投标项目（新增或修改）
     * 
     * @param biddingProject 招投标项目
     * @return 保存结果
     */
    boolean saveBiddingProject(BiddingProject biddingProject);

    /**
     * 根据ID获取招投标项目详情
     * 
     * @param id 主键ID
     * @return 招投标项目详情
     */
    BiddingProject getBiddingProjectById(Long id);

    /**
     * 根据项目编号获取招投标项目
     * 
     * @param projectNo 项目编号
     * @return 招投标项目
     */
    BiddingProject getBiddingProjectByProjectNo(String projectNo);

    /**
     * 删除招投标项目
     * 
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteBiddingProject(Long id);

    /**
     * 批量删除招投标项目
     * 
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteBiddingProject(List<Long> ids);

    /**
     * 生成招标编号
     *
     * @return 招标编号
     */
    String generateBiddingNo();

    /**
     * 检查招标编号是否存在
     *
     * @param biddingNo 招标编号
     * @param excludeId 排除的ID（用于修改时排除自己）
     * @return 存在返回true，不存在返回false
     */
    boolean existsBiddingNo(String biddingNo, Long excludeId);

    /**
     * 更新项目状态
     * 
     * @param id 主键ID
     * @param projectStatus 项目状态
     * @return 更新结果
     */
    boolean updateProjectStatus(Long id, Integer projectStatus);

    /**
     * 批量更新项目状态
     * 
     * @param ids 主键ID列表
     * @param projectStatus 项目状态
     * @return 更新结果
     */
    boolean batchUpdateProjectStatus(List<Long> ids, Integer projectStatus);

    /**
     * 更新参与状态
     * 
     * @param id 主键ID
     * @param isParticipate 是否参与投标
     * @param participateStatus 参与状态
     * @return 更新结果
     */
    boolean updateParticipateStatus(Long id, Integer isParticipate, Integer participateStatus);

    /**
     * 批量更新参与状态
     * 
     * @param ids 主键ID列表
     * @param isParticipate 是否参与投标
     * @param participateStatus 参与状态
     * @return 更新结果
     */
    boolean batchUpdateParticipateStatus(List<Long> ids, Integer isParticipate, Integer participateStatus);

    /**
     * 获取可投标的项目列表
     * 
     * @return 可投标的项目列表
     */
    List<BiddingProject> getCanBidProjects();

    /**
     * 获取已过期的项目列表
     * 
     * @return 已过期的项目列表
     */
    List<BiddingProject> getExpiredProjects();

    /**
     * 获取重点项目列表
     * 
     * @param minBudgetAmount 最小预算金额
     * @return 重点项目列表
     */
    List<BiddingProject> getKeyProjects(java.math.BigDecimal minBudgetAmount);

    /**
     * 获取紧急项目列表
     * 
     * @return 紧急项目列表
     */
    List<BiddingProject> getUrgentProjects();

    /**
     * 获取我参与的项目列表
     * 
     * @param userId 用户ID
     * @return 我参与的项目列表
     */
    List<BiddingProject> getMyParticipateProjects(Long userId);

    /**
     * 获取中标项目列表
     * 
     * @param userId 用户ID（可选）
     * @return 中标项目列表
     */
    List<BiddingProject> getWinningProjects(Long userId);

    /**
     * 根据招标方ID查询项目列表
     * 
     * @param tendererId 招标方ID
     * @return 项目列表
     */
    List<BiddingProject> getBiddingProjectByTendererId(Long tendererId);

    /**
     * 统计招投标项目数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getBiddingProjectStatistics(BiddingProjectQueryParam param);

    /**
     * 获取项目类型分布统计
     * 
     * @param param 查询参数
     * @return 项目类型分布
     */
    List<Map<String, Object>> getProjectTypeDistribution(BiddingProjectQueryParam param);

    /**
     * 获取项目状态分布统计
     * 
     * @param param 查询参数
     * @return 项目状态分布
     */
    List<Map<String, Object>> getProjectStatusDistribution(BiddingProjectQueryParam param);

    /**
     * 获取参与状态分布统计
     * 
     * @param param 查询参数
     * @return 参与状态分布
     */
    List<Map<String, Object>> getParticipateStatusDistribution(BiddingProjectQueryParam param);

    /**
     * 获取月度项目趋势
     * 
     * @param param 查询参数
     * @return 月度项目趋势
     */
    List<Map<String, Object>> getMonthlyProjectTrend(BiddingProjectQueryParam param);

    /**
     * 获取预算金额分布统计
     * 
     * @param param 查询参数
     * @return 预算金额分布
     */
    List<Map<String, Object>> getBudgetAmountDistribution(BiddingProjectQueryParam param);

    /**
     * 获取招标方分布统计
     * 
     * @param param 查询参数
     * @return 招标方分布
     */
    List<Map<String, Object>> getTendererDistribution(BiddingProjectQueryParam param);

    /**
     * 获取地区分布统计
     * 
     * @param param 查询参数
     * @return 地区分布
     */
    List<Map<String, Object>> getRegionDistribution(BiddingProjectQueryParam param);

    /**
     * 获取行业分布统计
     * 
     * @param param 查询参数
     * @return 行业分布
     */
    List<Map<String, Object>> getIndustryDistribution(BiddingProjectQueryParam param);

    /**
     * 模糊搜索招投标项目
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 招投标项目列表
     */
    List<BiddingProject> searchBiddingProjects(String keyword, Integer limit);

    /**
     * 获取即将截止的项目列表
     * 
     * @param days 天数
     * @return 即将截止的项目列表
     */
    List<BiddingProject> getProjectsExpiringSoon(Integer days);

    /**
     * 获取今日开标的项目列表
     * 
     * @return 今日开标的项目列表
     */
    List<BiddingProject> getTodayBidOpeningProjects();

    /**
     * 获取本周开标的项目列表
     * 
     * @return 本周开标的项目列表
     */
    List<BiddingProject> getThisWeekBidOpeningProjects();

    /**
     * 参与投标
     * 
     * @param id 项目ID
     * @return 参与结果
     */
    boolean participateBid(Long id);

    /**
     * 取消参与投标
     * 
     * @param id 项目ID
     * @return 取消结果
     */
    boolean cancelParticipate(Long id);

    /**
     * 标记为中标
     * 
     * @param id 项目ID
     * @return 标记结果
     */
    boolean markAsWinning(Long id);

    /**
     * 标记为未中标
     * 
     * @param id 项目ID
     * @return 标记结果
     */
    boolean markAsNotWinning(Long id);

    /**
     * 导入招投标项目
     * 
     * @param projectList 项目列表
     * @return 导入结果
     */
    Map<String, Object> importBiddingProjects(List<BiddingProject> projectList);

    /**
     * 导出招投标项目
     * 
     * @param param 查询参数
     * @return 导出数据
     */
    List<BiddingProject> exportBiddingProjects(BiddingProjectQueryParam param);

    /**
     * 自动更新过期项目状态
     * 
     * @return 更新数量
     */
    int autoUpdateExpiredProjects();

    /**
     * 发送截止提醒
     * 
     * @param days 提前天数
     * @return 提醒数量
     */
    int sendDeadlineReminders(Integer days);
}
