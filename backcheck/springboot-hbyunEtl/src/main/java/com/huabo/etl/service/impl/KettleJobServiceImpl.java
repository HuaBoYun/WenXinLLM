package com.huabo.etl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huabo.es.domain.*;
import com.huabo.etl.domain.KettleJob;
import com.huabo.etl.domain.KettleJobLog;
import com.huabo.etl.domain.QuartzDTO;
import com.huabo.etl.domain.XRepository;
import com.huabo.etl.mapper.KettleJobLogMapper;
import com.huabo.etl.mapper.KettleJobMapper;
import com.huabo.etl.mapper.XRepositoryMapper;
import com.huabo.etl.service.IKettleJobService;
import com.huabo.etl.service.IkettleService;
import com.huabo.etl.utils.AjaxResult;
import com.huabo.etl.utils.DateUtils;
import com.huabo.etl.utils.quartz.QuartzManage;
import com.huabo.etl.utils.quartz.ScriptQuartz;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 作业调度Service业务层处理
 *
 * @author zhibo.cao
 * @date 2021-07-22
 */
@Service("kettleJobServiceImpl")
public class KettleJobServiceImpl implements IKettleJobService {
    private static final Logger log = LoggerFactory.getLogger(KettleJobServiceImpl.class);
    @Autowired
    private KettleJobMapper kettleJobMapper;
    @Autowired
    private XRepositoryMapper repositoryMapper;

    @Autowired
    private IkettleService ikettleService;

    @Autowired
    private KettleJobLogMapper kettleJobLogMapper;

    /**
     * 查询作业调度
     *
     * @param id 作业调度ID
     * @return 作业调度
     */
    @Override
    public KettleJob selectKettleJobById(Long id) {
        return kettleJobMapper.selectKettleJobById(id);
    }

    /**
     * 查询作业调度列表
     *
     * @param kettleJob 作业调度
     * @return 作业调度
     */
    @Override
    public List<KettleJob> selectKettleJobList(KettleJob kettleJob) {
        return kettleJobMapper.selectKettleJobList(kettleJob);
    }

    /**
     * 新增作业调度
     *
     * @param kettleJob 作业调度
     * @return 结果
     */
    @Override
    public AjaxResult insertKettleJob(KettleJob kettleJob) {
        LambdaQueryWrapper<KettleJob> query = Wrappers.<KettleJob>lambdaQuery();
        query.eq(KettleJob::getJobName, kettleJob.getJobName()).eq(KettleJob::getJobRepositoryId, kettleJob.getJobRepositoryId());
        query.eq(KettleJob::getIsDel, 0);
        List<KettleJob> kettleJobs = kettleJobMapper.selectList(query);
        if (CollUtil.isNotEmpty(kettleJobs)) {
            return AjaxResult.error("该资源库下已存在同名作业");
        }
        kettleJob.setJobType("File");
        return AjaxResult.success(kettleJobMapper.insert(kettleJob));
    }

    /**
     * 修改作业调度
     *
     * @param kettleJob 作业调度
     * @return 结果
     */
    @Override
    public int updateKettleJob(KettleJob kettleJob) {
        kettleJob.setUpdateTime(DateUtils.getNowDate());
        kettleJob.setJobType("File");
        return kettleJobMapper.updateKettleJob(kettleJob);
    }

    /**
     * 删除作业调度对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKettleJobByIds(List<Long> ids) {
        return kettleJobMapper.deleteKettleJobByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除作业调度信息
     *
     * @param id 作业调度ID
     * @return 结果
     */
    @Override
    public int deleteKettleJobById(Long id) {
        return kettleJobMapper.deleteKettleJobById(id);
    }

    /**
     * 立即执行一次
     *
     * @param job
     * @return
     */
    @Override
    public AjaxResult runOne(KettleJob job) {
        Long id = job.getId();
        KettleJob kettleJob = kettleJobMapper.selectKettleJobById(id);
        if (kettleJob == null) {
            return AjaxResult.error("作业不存在!");
        }
        XRepository repository = repositoryMapper.selectXRepositoryById(kettleJob.getJobRepositoryId());
        if (repository == null) {
            return AjaxResult.error("资源库不存在!");
        }
        File file = new File(repository.getBaseDir() + kettleJob.getJobPath() + kettleJob.getJobName() + ".kjb");
        if (!file.exists()) {
            return AjaxResult.error(file.getPath() + "未找到文件!");
        }
        // 运行
        runJob(kettleJob, repository);
        //更新一下状态
        kettleJob.setJobStatus("等待中");
        kettleJobMapper.updateKettleJob(kettleJob);
        return AjaxResult.success("正在异步执行,请等待运行结果通知!");
    }

    /**
     * 通过名称执行job
     *
     * @param job
     * @return
     */
    @Override
    public AjaxResult runByName(KettleJob job) {
        String jobName = job.getJobName();
        List<KettleJob> kettleJobList = kettleJobMapper.selectKettleJobByName(jobName);

        if (kettleJobList == null || kettleJobList.isEmpty()) {
            return AjaxResult.error("作业不存在!");
        }
        if (kettleJobList.size() > 1) {
            return AjaxResult.error("[" + jobName + "]找到两个同名的作业,请联系管理员!");
        }
        KettleJob kettleJob = kettleJobList.get(0);
        XRepository repository = repositoryMapper.selectXRepositoryById(kettleJob.getJobRepositoryId());
        if (repository == null) {
            return AjaxResult.error("资源库不存在!");
        }
        File file = new File(repository.getBaseDir() + kettleJob.getJobPath() + kettleJob.getJobName() + ".kjb");
        if (!file.exists()) {
            return AjaxResult.error(file.getPath() + "未找到文件!");
        }
        // 运行
        runJob(kettleJob, repository);
        //更新一下状态
        kettleJob.setJobStatus("等待中");
        kettleJobMapper.updateKettleJob(kettleJob);
        return AjaxResult.success("正在异步执行,请等待运行结果通知!");
    }

    @Override
    public Map<String, Object> count() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<KettleJob> query = Wrappers.<KettleJob>lambdaQuery();
        query.ne(KettleJob::getIsDel, 1).isNotNull(KettleJob::getIsDel);
        // total
        Long totalJob = kettleJobMapper.selectCount(query);
        result.put("totalJob", totalJob);

        // runtotal
        query.eq(KettleJob::getCronStatus, "运行中");
        Long runJob = kettleJobMapper.selectCount(query);
        result.put("runJob", runJob);

        DateTime beforeDate = DateUtil.offsetDay(new Date(), -7);
        // 成功日志
        LambdaQueryWrapper<KettleJobLog> logQuery = Wrappers.<KettleJobLog>lambdaQuery();
        logQuery.between(KettleJobLog::getEnddate, DateUtil.beginOfDay(beforeDate), DateUtil.endOfDay(new Date()));
        logQuery.eq(KettleJobLog::getStatus, "end");
        List<KettleJobLog> kettleJobLogs = kettleJobLogMapper.selectList(logQuery);
        if (CollUtil.isNotEmpty(kettleJobLogs)) {
            Map<String, Long> logCount = kettleJobLogs.stream().collect(Collectors.groupingBy(KettleJobLog::getEnddateStr, Collectors.counting()));
            result.put("success", getAllCount(logCount));
        } else {
            result.put("success", getAllCount(null));
        }


        // 失败日志
        LambdaQueryWrapper<KettleJobLog> logQuery2 = Wrappers.<KettleJobLog>lambdaQuery();
        logQuery2.between(KettleJobLog::getEnddate, DateUtil.beginOfDay(beforeDate), DateUtil.endOfDay(new Date()));
        logQuery2.ne(KettleJobLog::getStatus, "end");
        List<KettleJobLog> kettleJobLogs2 = kettleJobLogMapper.selectList(logQuery2);
        if (CollUtil.isNotEmpty(kettleJobLogs2)) {
            Map<String, Long> logFailCount = kettleJobLogs2.stream().collect(Collectors.groupingBy(KettleJobLog::getEnddateStr, Collectors.counting()));
            result.put("fail", getAllCount(logFailCount));
        } else {
            result.put("fail", getAllCount(null));
        }
        return result;
    }

    private JSONArray getAllCount(Map<String, Long> count) {
        JSONArray jsonArray = new JSONArray();
        if(CollUtil.isNotEmpty(count)){
            count.forEach((k, v) -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", k);
                jsonObject.put("value", v);
                jsonArray.add(jsonObject);
            });
        }
        JSONArray resultArray = new JSONArray();
        for (int i = 0; i < 7; i++) {
            DateTime date = DateUtil.offsetDay(new Date(), -i);
            String formatDate = DateUtil.formatDate(DateUtil.beginOfDay(date));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", formatDate);
            jsonObject.put("value", 0L);
            resultArray.add(jsonObject);
        }
        resultArray.addAll(jsonArray);
        return resultArray;
    }

    @Override
    public KettleJob selectKettleJobByName(String name) {
        List<KettleJob> kettleJobs = kettleJobMapper.selectKettleJobByName(name);
        if (CollUtil.isNotEmpty(kettleJobs)) {
            return kettleJobs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<KettleJob> selectKettleJobByRepoIds(List<Long> ids) {
        LambdaQueryWrapper<KettleJob> query = Wrappers.<KettleJob>lambdaQuery();
        query.in(KettleJob::getJobRepositoryId, ids);
        return kettleJobMapper.selectList(query);
    }

    /**
     * 定时任务执行job
     *
     * @param id
     */
    @Override
    public void runJobRightNow(Long id) {
        KettleJob kettleJob = kettleJobMapper.selectKettleJobById(id);
        if (kettleJob == null) {
            log.error("作业不存在!");
            return;
        }
        XRepository repository = repositoryMapper.selectXRepositoryById(kettleJob.getJobRepositoryId());
        if (repository == null) {
            log.error("资源库不存在!");
            return;
        }
        //更新一下状态
        kettleJob.setJobStatus("运行中");
        kettleJobMapper.updateKettleJob(kettleJob);
        StringBuilder title = new StringBuilder(kettleJob.getJobName()).append(".kjb 执行结果:");
        StringBuilder msg = new StringBuilder(kettleJob.getJobName()).append(".kjb 描述:").append(kettleJob.getJobDescription());
        try {
            ikettleService.callJob(kettleJob, repository, null, null);
            kettleJob.setJobStatus("成功");
            kettleJob.setLastSucceedTime(DateUtils.getNowDate());
            kettleJobMapper.updateKettleJob(kettleJob);
            title.append("成功!");
        } catch (Exception e) {
            kettleJob.setJobStatus("异常");
            kettleJobMapper.updateKettleJob(kettleJob);
            title.append("异常!");
            msg.append(":" + e.getMessage());
            log.error(kettleJob.getId() + "的job执行失败:" + e.getMessage());
        }
    }

    /**
     * 异步执行job
     *
     * @param kettleJob
     * @param repository
     */
    @Async
    public void runJob(KettleJob kettleJob, XRepository repository) {
        //更新一下状态
        kettleJob.setJobStatus("运行中");
        kettleJobMapper.updateKettleJob(kettleJob);
        StringBuilder title = new StringBuilder(kettleJob.getJobName()).append(".kjb 执行结果:");
        StringBuilder msg = new StringBuilder(kettleJob.getJobName()).append(".kjb 描述:").append(kettleJob.getJobDescription());
        try {
            ikettleService.callJob(kettleJob, repository, null, null);
            kettleJob.setJobStatus("成功");
            kettleJob.setLastSucceedTime(DateUtils.getNowDate());
            kettleJobMapper.updateKettleJob(kettleJob);
            title.append("成功!");
        } catch (Exception e) {
            kettleJob.setJobStatus("异常");
            kettleJobMapper.updateKettleJob(kettleJob);
            title.append("异常!");
            msg.append(":" + e.getMessage());
            log.error(kettleJob.getId() + "的job执行失败:" + e.getMessage());
        }
    }

    /**
     * 查询job日志
     *
     * @param kettleJob
     * @return
     */
    @Override
    public List<String> queryJobLog(KettleJob kettleJob) {
        List<String> logs = kettleJobMapper.queryJobLog(kettleJob.getJobName());
        return logs;
    }

    /**
     * 检查定时任务是否存在
     *
     * @param checkStr
     * @return
     */
    @Override
    public Long checkQuartzExist(String checkStr) {
        return kettleJobMapper.checkQuartzExist(checkStr);
    }

    /**
     * 定时任务执行
     *
     * @param id
     * @param jobName
     * @return
     */
    @Override
    public AjaxResult runStopJobQuartz(String id, String jobName) {
        KettleJob kettleJob = kettleJobMapper.selectKettleJobById(Long.valueOf(id));
        QuartzDTO quartzDTO = new QuartzDTO();
        quartzDTO.setJobName(kettleJob.getJobName() + "@" + kettleJob.getId());
        quartzDTO.setJobGroupName(kettleJob.getJobName() + "@" + "job");
        quartzDTO.setJobGroupName(kettleJob.getJobName() + "@" + "jobGroup" + "@" + kettleJob.getId());
        quartzDTO.setTriggerName(kettleJob.getJobName() + "@" + kettleJob.getId());
        quartzDTO.setTriggerGroupName(kettleJob.getJobName() + "@" + "trigerGroup" + "@" + kettleJob.getId());
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("runType", "job");
        jobDataMap.put("id", kettleJob.getId());
        quartzDTO.setJobDataMap(jobDataMap);
        quartzDTO.setCron(kettleJob.getCron());
        quartzDTO.setJobClass(ScriptQuartz.class);

        if (StrUtil.isEmpty(kettleJob.getCronStatus()) || kettleJob.getCronStatus().equals("未运行")) {
            QuartzManage.addCronJob(quartzDTO);
            kettleJob.setCronStatus("运行中");
            kettleJobMapper.updateKettleJob(kettleJob);
            return AjaxResult.success("已启动定时任务");
        } else {
            QuartzManage.removeJob(quartzDTO);
            kettleJob.setCronStatus("未运行");
            kettleJobMapper.updateKettleJob(kettleJob);

            return AjaxResult.success("已停止定时任务");
        }
    }

}
