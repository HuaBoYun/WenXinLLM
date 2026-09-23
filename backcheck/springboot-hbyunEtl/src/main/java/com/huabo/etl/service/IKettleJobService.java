package com.huabo.etl.service;

import com.huabo.etl.domain.KettleJob;
import com.huabo.etl.utils.AjaxResult;

import java.util.List;
import java.util.Map;

/**
 * 作业调度Service接口
 *
 * @author zhibo.cao
 * @date 2021-07-22
 */
public interface IKettleJobService {
    /**
     * 查询作业调度
     *
     * @param id 作业调度ID
     * @return 作业调度
     */
    public KettleJob selectKettleJobById(Long id);

    /**
     * 查询作业调度列表
     *
     * @param kettleJob 作业调度
     * @return 作业调度集合
     */
    public List<KettleJob> selectKettleJobList(KettleJob kettleJob);

    /**
     * 新增作业调度
     *
     * @param kettleJob 作业调度
     * @return 结果
     */
    public AjaxResult insertKettleJob(KettleJob kettleJob);

    /**
     * 修改作业调度
     *
     * @param kettleJob 作业调度
     * @return 结果
     */
    public int updateKettleJob(KettleJob kettleJob);

    /**
     * 批量删除作业调度
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKettleJobByIds(List<Long> ids);

    /**
     * 删除作业调度信息
     *
     * @param id 作业调度ID
     * @return 结果
     */
    public int deleteKettleJobById(Long id);

    /**
     * 执行一次，采用异步
     *
     * @param job
     * @return
     */
    AjaxResult runOne(KettleJob job);

    /**
     * 查询job日志
     *
     * @param kettleJob
     * @return
     */
    List<String> queryJobLog(KettleJob kettleJob);

    /**
     * 定时任务是否存在
     *
     * @param checkStr
     * @return
     */
    Long checkQuartzExist(String checkStr);

    /**
     * 定时任务启动
     *
     * @param id
     * @param jobName
     * @return
     */
    public AjaxResult runStopJobQuartz(String id, String jobName);

    /**
     * 立即执行，不用异步
     *
     * @param valueOf
     */
    void runJobRightNow(Long valueOf);

    /**
     * 通过名称执行
     *
     * @param job
     * @return
     */
    AjaxResult runByName(KettleJob job);

    /**
     * 统计
     *
     * @return
     */
    Map<String, Object> count();

    /**
     * 通过名称查询
     *
     * @param name
     * @return
     */
    KettleJob selectKettleJobByName(String name);

    List<KettleJob> selectKettleJobByRepoIds(List<Long> ids);
}
