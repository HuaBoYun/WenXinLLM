package com.huabo.etl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.etl.domain.KettleJob;
import com.huabo.etl.domain.KettleTrans;
import com.huabo.etl.service.IKettleJobService;
import com.huabo.etl.service.IKettleTransService;
import com.huabo.etl.utils.AjaxResult;
import com.huabo.etl.utils.page.TableDataInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="kettleAPI 接口",description="kettleAPI 接口")
@RestController
@RequestMapping("/api/kettle")
public class KettleApiController extends BaseController {
    @Autowired
    private IKettleJobService kettleJobService;
    @Autowired
    private IKettleTransService kettleTransService;

    @Value("${kettle.weburl}")
    private String webUrl;


    /**
     * 查询作业调度列表
     */
    @Operation(summary = "查询作业调度列表")
    @GetMapping("/job/list")
    public TableDataInfo list(@RequestBody KettleJob kettleJob) {
        startPage();
        List<KettleJob> list = kettleJobService.selectKettleJobList(kettleJob);
        return getDataTable(list);
    }

    /**
     * 查询转换列表
     */
    @Operation(summary = "查询转换列表")
    @GetMapping("/trans/list")
    public TableDataInfo list(@RequestBody KettleTrans kettleTrans) {
        startPage();
        List<KettleTrans> list = kettleTransService.selectKettleTransList(kettleTrans);
        return getDataTable(list);
    }


    /**
     * 新增保存作业调度
     */
    @Operation(summary = "新增保存作业调度")
    @PostMapping("/job/add")
    public AjaxResult addSave(@RequestBody KettleJob kettleJob) {
        return kettleJobService.insertKettleJob(kettleJob);
    }

    /**
     * 新增保存转换
     */
    @Operation(summary = "新增保存转换")
    @PostMapping("/trans/add")
    public AjaxResult addSave(@RequestBody KettleTrans kettleTrans) {
        return kettleTransService.insertKettleTrans(kettleTrans);
    }

    /**
     * 修改保存作业调度
     */
    @Operation(summary = "修改保存作业调度")
    @PostMapping("/job/edit")
    public AjaxResult editSave(@RequestBody KettleJob kettleJob) {
        return toAjax(kettleJobService.updateKettleJob(kettleJob));
    }

    /**
     * 修改保存转换
     */
    @Operation(summary = "修改保存转换")
    @PostMapping("/trans/edit")
    public AjaxResult editSave(@RequestBody KettleTrans kettleTrans) {
        return toAjax(kettleTransService.updateKettleTrans(kettleTrans));
    }


    /**
     * 删除作业调度
     */
    @Operation(summary = "删除作业调度")
    @DeleteMapping("/job/remove/{ids}")
    public AjaxResult remove(@PathVariable List<Long> ids) {
        return toAjax(kettleJobService.deleteKettleJobByIds(ids));
    }

    /**
     * 删除转换
     */
    @Operation(summary = "删除转换")
    @DeleteMapping("/trans/remove/{ids}")
    public AjaxResult removeTrans(@PathVariable List<Long> ids) {
        return toAjax(kettleTransService.deleteKettleTransByIds(ids));
    }

    @Operation(summary = "运行job一次")
    @PostMapping("/job/run")
    public AjaxResult runJobOne(@RequestBody KettleJob job) {
        AjaxResult result = kettleJobService.runOne(job);
        return result;
    }

    @Operation(summary = "通过名称运行job一次")
    @PostMapping("/job/runByName")
    public AjaxResult runJobByName(@RequestBody KettleJob job) {
        AjaxResult result = kettleJobService.runByName(job);
        return result;
    }

    /**
     * 转换立即执行一次
     */
    @Operation(summary = "转换立即执行一次")
    @PostMapping("/trans/run")
    public AjaxResult runTranOne(@RequestBody KettleTrans trans) {
        AjaxResult result = kettleTransService.runOne(trans);
        return result;
    }

    /**
     * 通过转换名称 转换立即执行一次
     */
    @Operation(summary = "通过转换名称 转换立即执行一次")
    @PostMapping("/trans/runByName")
    public AjaxResult runTransByName(@RequestBody KettleTrans trans) {
        AjaxResult result = kettleTransService.runByName(trans);
        return result;
    }

    @Operation(summary = "统计转换")
    @GetMapping("/count")
    public AjaxResult count() {
        Map<String, Object> transMap = kettleTransService.count();
        Map<String, Object> transJob = kettleJobService.count();
        Map<String, Object> map = new HashMap<>();
        if (CollUtil.isNotEmpty(transMap)) {
            map.put("tran", transMap);
        }
        if (CollUtil.isNotEmpty(transJob)) {
            map.put("job", transJob);
        }
        return AjaxResult.success(map);
    }

    @Operation(summary = "获取weburl")
    @GetMapping("/getUrl")
    public AjaxResult getUrl() {
        if (StrUtil.isEmpty(webUrl)) {
            return AjaxResult.error("请联系管理员配置连接");
        }
        return AjaxResult.success("成功", webUrl);
    }
}
