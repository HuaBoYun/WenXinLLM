package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.DataSubmitTask;
import com.huabo.cybermonitor.vo.DataSubmitTaskQueryVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 数据报送任务服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface IDataSubmitTaskService extends IService<DataSubmitTask> {

    /**
     * 分页查询数据报送任务列表
     *
     * @param queryVO 查询条件
     * @return 分页结果
     */
    IPage<DataSubmitTask> getTaskList(DataSubmitTaskQueryVO queryVO);

    /**
     * 根据任务ID获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    DataSubmitTask getTaskDetail(String taskId);

    /**
     * 新增数据报送任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    boolean addTask(DataSubmitTask task);

    /**
     * 更新数据报送任务
     *
     * @param task 任务信息
     * @return 是否成功
     */
    boolean updateTask(DataSubmitTask task);

    /**
     * 删除数据报送任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean deleteTask(String taskId);

    /**
     * 批量删除数据报送任务
     *
     * @param taskIds 任务ID列表
     * @return 是否成功
     */
    boolean batchDeleteTask(List<String> taskIds);

    /**
     * 发布任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean publishTask(String taskId);

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean cancelTask(String taskId);

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     * @return 是否成功
     */
    boolean completeTask(String taskId);

    /**
     * 根据任务状态查询任务列表
     *
     * @param taskStatus 任务状态
     * @return 任务列表
     */
    List<DataSubmitTask> getTasksByStatus(String taskStatus);

    /**
     * 根据任务类型查询任务列表
     *
     * @param taskType 任务类型
     * @return 任务列表
     */
    List<DataSubmitTask> getTasksByType(String taskType);

    /**
     * 根据报送周期查询任务列表
     *
     * @param submitCycle 报送周期
     * @return 任务列表
     */
    List<DataSubmitTask> getTasksByCycle(String submitCycle);

    /**
     * 查询活跃的任务列表
     *
     * @return 活跃任务列表
     */
    List<DataSubmitTask> getActiveTasks();

    /**
     * 查询即将到期的任务列表
     *
     * @param days 天数
     * @return 即将到期的任务列表
     */
    List<DataSubmitTask> getExpiringTasks(Integer days);

    /**
     * 获取任务统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getTaskStatistics();

    /**
     * 获取任务类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getTaskTypeDistribution();

    /**
     * 获取任务分类分布统计
     *
     * @return 分类分布统计
     */
    List<Map<String, Object>> getTaskCategoryDistribution();

    /**
     * 获取任务周期分布统计
     *
     * @return 周期分布统计
     */
    List<Map<String, Object>> getTaskCycleDistribution();

    /**
     * 验证任务名称是否重复
     *
     * @param taskName  任务名称
     * @param excludeId 排除的任务ID
     * @return 是否重复
     */
    boolean validateTaskName(String taskName, String excludeId);

    /**
     * 导出任务列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportTaskList(DataSubmitTaskQueryVO queryVO, HttpServletResponse response);

    /**
     * 下载任务导入模板
     *
     * @param response HTTP响应
     */
    void downloadTaskTemplate(HttpServletResponse response);

    /**
     * 批量导入任务
     *
     * @param file 导入文件
     * @return 导入结果
     */
    Map<String, Object> importTaskList(MultipartFile file);

    /**
     * 获取任务的报送记录数量
     *
     * @param taskId 任务ID
     * @return 记录数量
     */
    Integer getSubmitRecordCount(String taskId);

    /**
     * 复制任务
     *
     * @param taskId 源任务ID
     * @return 新任务ID
     */
    String copyTask(String taskId);

    /**
     * 获取任务类型标签
     *
     * @param taskType 任务类型
     * @return 类型标签
     */
    String getTaskTypeLabel(String taskType);

    /**
     * 获取任务状态标签
     *
     * @param taskStatus 任务状态
     * @return 状态标签
     */
    String getTaskStatusLabel(String taskStatus);

    /**
     * 获取报送周期标签
     *
     * @param submitCycle 报送周期
     * @return 周期标签
     */
    String getSubmitCycleLabel(String submitCycle);

    /**
     * 获取任务分类标签
     *
     * @param taskCategory 任务分类
     * @return 分类标签
     */
    String getTaskCategoryLabel(String taskCategory);
}
