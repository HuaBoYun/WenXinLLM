package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.eps.EpsBudgetCollaborationTask;

import java.util.List;
import java.util.Map;

/**
 * EPS 预算协同任务服务
 */
public interface EpsBudgetCollaborationService {

	/**
	 * 分页查询协同任务
	 */
	IPage<EpsBudgetCollaborationTask> queryCollaborationTaskPage(Long current, Long size, String taskName,
			String taskStatus, Long systemId, Long assigneeId);

	/**
	 * 创建协同任务
	 */
	boolean createCollaborationTask(EpsBudgetCollaborationTask collaborationTask);

	/**
	 * 更新协同任务
	 */
	boolean updateCollaborationTask(EpsBudgetCollaborationTask collaborationTask);

	/**
	 * 删除协同任务
	 */
	boolean deleteCollaborationTask(Long taskId);

	/**
	 * 根据ID查询协同任务
	 */
	EpsBudgetCollaborationTask getCollaborationTaskById(Long taskId);

	/**
	 * 分配协同任务
	 */
	boolean assignCollaborationTask(Long taskId, Map<String, Object> assignmentData);

	/**
	 * 开始协同任务
	 */
	boolean startCollaborationTask(Long taskId, Long userId);

	/**
	 * 完成协同任务
	 */
	boolean completeCollaborationTask(Long taskId, Map<String, Object> completionData);

	/**
	 * 暂停协同任务
	 */
	boolean pauseCollaborationTask(Long taskId, Long userId, String pauseReason);

	/**
	 * 恢复协同任务
	 */
	boolean resumeCollaborationTask(Long taskId, Long userId);

	/**
	 * 查询任务进度
	 */
	Map<String, Object> getTaskProgress(Long taskId);

	/**
	 * 查询任务参与者
	 */
	List<Map<String, Object>> getTaskParticipants(Long taskId);

	/**
	 * 添加任务参与者
	 */
	boolean addTaskParticipant(Long taskId, Map<String, Object> participantData);

	/**
	 * 移除任务参与者
	 */
	boolean removeTaskParticipant(Long taskId, Long participantId);

	/**
	 * 分页查询任务评论
	 */
	List<Map<String, Object>> getTaskComments(Long taskId, Long current, Long size);

	/**
	 * 添加任务评论
	 */
	boolean addTaskComment(Long taskId, Map<String, Object> commentData);

	/**
	 * 查询任务附件
	 */
	List<Map<String, Object>> getTaskAttachments(Long taskId);

	/**
	 * 上传任务附件
	 */
	boolean uploadTaskAttachment(Long taskId, Map<String, Object> attachmentData);

	/**
	 * 删除任务附件
	 */
	boolean deleteTaskAttachment(Long taskId, Long attachmentId);

	/**
	 * 协同统计
	 */
	Map<String, Object> getCollaborationStatistics(Long systemId, String startDate, String endDate);

	/**
	 * 查询我的任务
	 */
	List<EpsBudgetCollaborationTask> getMyTasks(Long userId, String taskStatus);

	/**
	 * 批量操作任务
	 */
	Map<String, Object> batchOperateTasks(Map<String, Object> batchData);
}
