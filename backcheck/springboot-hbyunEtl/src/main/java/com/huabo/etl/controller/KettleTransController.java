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
import com.huabo.etl.domain.KettleTrans;
import com.huabo.etl.domain.XRepository;
import com.huabo.etl.mapper.KettleTransMapper;
import com.huabo.etl.service.IKettleTransService;
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
 * 转换Controller
 *
 * @author zhibo.cao
 * @date 2022-12-01
 */
@Tag(name="装换api",description="装换api")
@RestController
@RequestMapping("/kettle/trans")
public class KettleTransController extends BaseController {

    @Autowired
    private IKettleTransService kettleTransService;
    @Autowired
    private IXRepositoryService xRepositoryService;

    @Autowired
    private KettleTransMapper kettleTransMapper;


    /**
     * 查询转换列表
     */
    @Operation(summary = "查询转换列表")
    @GetMapping("/list")
    public TableDataInfo list(KettleTrans kettleTrans) {
        startPage();
        List<KettleTrans> list = kettleTransService.selectKettleTransList(kettleTrans);
        if (CollUtil.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                XRepository xRepository = xRepositoryService.selectXRepositoryById(list.get(i).getTransRepositoryId());
                if (ObjectUtil.isNotEmpty(xRepository)) {
                    list.get(i).setTransRepository(xRepository.getRepoName());
                    list.get(i).setBaseDir(xRepository.getBaseDir());
                }
            }
        }
        return getDataTable(list);
    }


    /**
     * 新增保存转换
     */
    @Operation(summary = "新增保存转换")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody KettleTrans kettleTrans) {
        return kettleTransService.insertKettleTrans(kettleTrans);
    }


    /**
     * 修改保存转换
     */
    @Operation(summary = "修改保存转换")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody KettleTrans kettleTrans) {
        if (ObjectUtil.isNull(kettleTrans.getId())) {
            return AjaxResult.error("转换id不能为空");
        }
        if (StrUtil.isNotEmpty(kettleTrans.getTransName())) {
            return AjaxResult.error("转换名不能修改");
        }
        if (ObjectUtil.isNotNull(kettleTrans.getTransRepositoryId())) {
            return AjaxResult.error("不能更改资源库");
        }
        return toAjax(kettleTransService.updateKettleTrans(kettleTrans));
    }

    /**
     * 删除转换
     */
    @Operation(summary = "删除转换")
    @DeleteMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable List<Long> ids) {
        return toAjax(kettleTransService.deleteKettleTransByIds(ids));
    }

    /**
     * 通过名称查询转换
     */
    @Operation(summary = "通过名称查询转换")
    @GetMapping("/getByName/{name}")
    public AjaxResult getByName(@PathVariable String name) {
        KettleTrans kettleTrans = kettleTransService.selectKettleTransByName(name);
        if (kettleTrans == null) {
            return AjaxResult.error("没有该转换");
        }
        return AjaxResult.success(kettleTrans);
    }


    /**
     * 转换立即执行一次
     */
    @Operation(summary = "转换立即执行一次")
    @PostMapping("/run")
    public AjaxResult runToQueue(@RequestBody KettleTrans trans) {
        AjaxResult result = kettleTransService.runOne(trans);
        return result;
    }

    @Operation(summary = "通过名称运行")
    @PostMapping("/runByName")
    public AjaxResult runByName(@RequestBody KettleTrans trans) {
        AjaxResult result = kettleTransService.runByName(trans);
        return result;
    }

    @Operation(summary = "停启转换定时任务")
    @PostMapping("/runStopTransQuartz")
    public AjaxResult runStopTransQuartz(@RequestBody KettleTrans trans) {
        KettleTrans kettleTrans = kettleTransService.selectKettleTransById(trans.getId());
        if (StrUtil.isEmpty(kettleTrans.getCron())) {
            return AjaxResult.error("没有cron表达式");
        }
        AjaxResult result = kettleTransService.runStopTransQuartz(trans.getId().toString(), trans.getTransName());
        return result;
    }

    /**
     * 上传请求
     */
    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("transFile") MultipartFile file, @RequestParam("transRepositoryId") Long transRepositoryId,
                                 @RequestParam("cron") String cron, @RequestParam("transLogLevel") String transLogLevel) {
        try {
            // 文件名
            String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));

            // 查询资源库
            XRepository xRepository = xRepositoryService.selectXRepositoryById(transRepositoryId);

            // 校验文件名
            LambdaQueryWrapper<KettleTrans> query = Wrappers.<KettleTrans>lambdaQuery();
            query.eq(KettleTrans::getTransName, fileName).eq(KettleTrans::getTransRepositoryId, transRepositoryId);
            query.eq(KettleTrans::getIsDel, 0);
            List<KettleTrans> trans = kettleTransMapper.selectList(query);
            if (CollUtil.isNotEmpty(trans)) {
                return AjaxResult.error("已经存在改转换名，请重命名");
            }

            // 上传并返回新文件名称
            FileUploadUtils.upload(xRepository.getBaseDir(), file);
            KettleTrans kettleTrans = new KettleTrans();
            kettleTrans.setTransRepositoryId(xRepository.getId());
            kettleTrans.setTransName(fileName);
            kettleTrans.setCron(cron);
            kettleTrans.setCreatedTime(new Date());
            kettleTrans.setTransPath("/");
            kettleTrans.setTransLogLevel(transLogLevel);
            kettleTransService.insertKettleTrans(kettleTrans);
            return AjaxResult.success("转换成功");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }


}
