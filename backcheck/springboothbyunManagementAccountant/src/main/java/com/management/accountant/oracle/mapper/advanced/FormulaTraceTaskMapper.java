package com.management.accountant.oracle.mapper.advanced;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.advanced.FormulaTraceTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 公式追踪任务Mapper接口
 * 
 * @author AI Agent
 * @date 2026-02-06
 */
@Mapper
public interface FormulaTraceTaskMapper extends BaseMapper<FormulaTraceTask> {

    /**
     * 根据状态查询任务列表
     * 
     * @param status 状态
     * @param companyId 公司ID
     * @return 任务列表
     */
    List<FormulaTraceTask> selectByStatus(@Param("status") String status, @Param("companyId") String companyId);

    /**
     * 根据公司ID查询任务列表
     * 
     * @param companyId 公司ID
     * @return 任务列表
     */
    List<FormulaTraceTask> selectByCompanyId(@Param("companyId") String companyId);

    /**
     * 批量删除任务
     * 
     * @param taskIds 任务ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("taskIds") List<String> taskIds);

    /**
     * 更新任务状态
     * 
     * @param taskId 任务ID
     * @param status 状态
     * @return 更新数量
     */
    int updateStatus(@Param("taskId") String taskId, @Param("status") String status);

    /**
     * 获取统计数据
     * 
     * @param companyId 公司ID
     * @return 统计数据
     */
    Map<String, Object> getStatistics(@Param("companyId") String companyId);

    /**
     * 分页查询任务列表
     * 
     * @param keyword 关键字
     * @param status 状态
     * @param companyId 公司ID
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 任务列表
     */
    List<FormulaTraceTask> selectPageList(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("companyId") String companyId,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询总数
     * 
     * @param keyword 关键字
     * @param status 状态
     * @param companyId 公司ID
     * @return 总数
     */
    int selectCount(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("companyId") String companyId
    );
}

