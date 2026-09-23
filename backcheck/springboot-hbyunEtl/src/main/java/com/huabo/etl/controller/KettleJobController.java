package com.huabo.etl.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huabo.etl.domain.KettleJob;
import com.huabo.etl.domain.XRepository;
import com.huabo.etl.mapper.KettleJobMapper;
import com.huabo.etl.service.IKettleJobService;
import com.huabo.etl.service.IXRepositoryService;
import com.huabo.etl.utils.AjaxResult;
import com.huabo.etl.utils.FileUploadUtils;
import com.huabo.etl.utils.page.TableDataInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 作业调度Controller
 *
 * @author zhibo.cao
 * @date 2021-07-22
 */
@Tag(name="作业接口",description="作业接口")
@RestController
@RequestMapping("/kettle/job")
public class KettleJobController extends BaseController {


    @Autowired
    private IKettleJobService kettleJobService;
    @Autowired
    private IXRepositoryService xRepositoryService;
    @Autowired
    private KettleJobMapper kettleJobMapper;


    /**
     * 查询作业调度列表
     */
    @Operation(summary = "查询作业调度列表")
    @GetMapping("/list")
    public TableDataInfo list(KettleJob kettleJob) {
        startPage();
        List<KettleJob> list = kettleJobService.selectKettleJobList(kettleJob);
        if (CollUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                XRepository xRepository = xRepositoryService.selectXRepositoryById(list.get(i).getJobRepositoryId());
                if (ObjectUtil.isNotEmpty(xRepository)) {
                    list.get(i).setJobRepository(xRepository.getRepoName());
                    list.get(i).setBaseDir(xRepository.getBaseDir());
                }
            }
        }
        return getDataTable(list);
    }


    /**
     * 新增保存作业调度
     */
    @Operation(summary = "新增保存作业调度")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody KettleJob kettleJob) {
        return kettleJobService.insertKettleJob(kettleJob);
    }

    /**
     * 修改保存作业调度
     */
    @Operation(summary = "修改保存作业调度")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody KettleJob kettleJob) {
        if (ObjectUtil.isNull(kettleJob.getId())) {
            return AjaxResult.error("转换id不能为空");
        }
        if (StrUtil.isNotEmpty(kettleJob.getJobName())) {
            return AjaxResult.error("作业名不能修改");
        }
        if (ObjectUtil.isNotNull(kettleJob.getJobRepositoryId())) {
            return AjaxResult.error("不能更改资源库");
        }
        return toAjax(kettleJobService.updateKettleJob(kettleJob));
    }

    /**
     * 删除作业调度
     */
    @Operation(summary = "删除作业调度")
    @DeleteMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable List<Long> ids) {
        return toAjax(kettleJobService.deleteKettleJobByIds(ids));
    }

    /**
     * 通过名称查询转换
     */
    @Operation(summary = "通过名称查询转换")
    @GetMapping("/getByName/{name}")
    public AjaxResult getByName(@PathVariable String name) {
        KettleJob kettleJob = kettleJobService.selectKettleJobByName(name);
        if (kettleJob == null) {
            return AjaxResult.error("没有该作业");
        }
        return AjaxResult.success(kettleJob);
    }


    @Operation(summary = "运行job")
    @PostMapping("/run")
    public AjaxResult run(@RequestBody KettleJob job) {
        AjaxResult result = kettleJobService.runOne(job);
        return result;
    }

    @Operation(summary = "通过名称运行")
    @PostMapping("/runByName")
    public AjaxResult runByName(@RequestBody KettleJob job) {
        AjaxResult result = kettleJobService.runByName(job);
        return result;
    }

    @Operation(summary = "停启转换定时任务")
    @PostMapping("/runStopTransQuartz")
    public AjaxResult runStopTransQuartz(@RequestBody KettleJob job) {
        KettleJob kettleJob = kettleJobService.selectKettleJobById(job.getId());
        if (StrUtil.isEmpty(kettleJob.getCron())) {
            return AjaxResult.error("没有cron表达式");
        }
        AjaxResult result = kettleJobService.runStopJobQuartz(job.getId().toString(), job.getJobName());
        return result;
    }

    /**
     * 上传请求
     */
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("jobFile") MultipartFile file, @RequestParam("jobRepositoryId") Long jobRepositoryId,
                                 @RequestParam("cron") String cron, @RequestParam("jobLogLevel") String jobLogLevel) {
        try {
            // 文件名
            String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));

            // 查询资源库
            XRepository xRepository = xRepositoryService.selectXRepositoryById(jobRepositoryId);

            // 校验文件名

            LambdaQueryWrapper<KettleJob> query = Wrappers.<KettleJob>lambdaQuery();
            query.eq(KettleJob::getJobName, fileName).eq(KettleJob::getJobRepositoryId, jobRepositoryId);
            query.eq(KettleJob::getIsDel, 0);
            List<KettleJob> kettleJobs = kettleJobMapper.selectList(query);
            if (CollUtil.isNotEmpty(kettleJobs)) {
                return AjaxResult.error("已经存在改作业名，请重命名");
            }

            // 上传并返回新文件名称
            FileUploadUtils.upload(xRepository.getBaseDir(), file);

            KettleJob kettleJob = new KettleJob();
            kettleJob.setJobRepositoryId(xRepository.getId());
            kettleJob.setJobName(fileName);
            kettleJob.setCron(cron);
            kettleJob.setCreatedTime(new Date());
            kettleJob.setJobLogLevel(jobLogLevel);
            kettleJob.setJobPath("/");
            kettleJobService.insertKettleJob(kettleJob);
            return AjaxResult.success("作业新增陈宫");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

}
