package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.contract.entity.Progress;
import com.huabo.contract.vo.ProgressQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 进度管理Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface ProgressMapper extends BaseMapper<Progress> {

    /**
     * 分页查询进度记录
     */
    IPage<Progress> selectProgressPage(Page<Progress> page, @Param("param") ProgressQueryParam param);

    /**
     * 根据项目ID查询进度记录
     */
    List<Progress> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据进度类型查询进度记录
     */
    List<Progress> selectByProgressType(@Param("progressType") Integer progressType);

    /**
     * 根据父级进度ID查询子进度记录
     */
    List<Progress> selectByParentProgressId(@Param("parentProgressId") Long parentProgressId);

    /**
     * 根据进度状态查询进度记录
     */
    List<Progress> selectByProgressStatus(@Param("progressStatus") Integer progressStatus);

    /**
     * 根据优先级查询进度记录
     */
    List<Progress> selectByPriority(@Param("priority") Integer priority);

    /**
     * 根据负责人ID查询进度记录
     */
    List<Progress> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 根据负责人姓名查询进度记录
     */
    List<Progress> selectByManagerName(@Param("managerName") String managerName);

    /**
     * 查询里程碑进度记录
     */
    List<Progress> selectMilestones(@Param("projectId") Long projectId);

    /**
     * 查询关键路径进度记录
     */
    List<Progress> selectCriticalPath(@Param("projectId") Long projectId);

    /**
     * 根据创建人查询进度记录
     */
    List<Progress> selectByCreateBy(@Param("createBy") Long createBy);

    /**
     * 根据项目ID统计总体进度
     */
    BigDecimal calculateOverallProgress(@Param("projectId") Long projectId);

    /**
     * 根据进度状态统计数量
     */
    Integer countByProgressStatus(@Param("progressStatus") Integer progressStatus);

    /**
     * 根据优先级统计数量
     */
    Integer countByPriority(@Param("priority") Integer priority);

    /**
     * 查询延期的进度记录
     */
    List<Progress> selectDelayedProgress();

    /**
     * 查询提前完成的进度记录
     */
    List<Progress> selectEarlyCompletedProgress();

    /**
     * 查询高优先级进度记录
     */
    List<Progress> selectHighPriorityProgress();

    /**
     * 查询即将到期的进度记录
     */
    List<Progress> selectUpcomingDeadlines(@Param("days") Integer days);

    /**
     * 批量更新进度状态
     */
    Integer batchUpdateProgressStatus(@Param("ids") List<Long> ids, 
                                    @Param("progressStatus") Integer progressStatus, 
                                    @Param("updateBy") Long updateBy);

    /**
     * 批量更新实际进度
     */
    Integer batchUpdateActualProgress(@Param("ids") List<Long> ids, 
                                    @Param("actualProgress") BigDecimal actualProgress, 
                                    @Param("updateBy") Long updateBy);

    /**
     * 根据日期范围查询进度记录
     */
    List<Progress> selectByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据进度范围查询进度记录
     */
    List<Progress> selectByProgressRange(@Param("minProgress") BigDecimal minProgress, @Param("maxProgress") BigDecimal maxProgress);

    /**
     * 查询进度统计信息
     */
    List<Progress> selectProgressStatistics();

    /**
     * 根据关键词搜索进度记录
     */
    List<Progress> searchByKeyword(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 查询项目进度树形结构
     */
    List<Progress> selectProgressTree(@Param("projectId") Long projectId);

    /**
     * 查询根级进度记录
     */
    List<Progress> selectRootProgress(@Param("projectId") Long projectId);
}
