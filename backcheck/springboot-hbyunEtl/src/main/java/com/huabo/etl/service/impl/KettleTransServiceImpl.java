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
import com.huabo.etl.domain.KettleTrans;
import com.huabo.etl.domain.KettleTransLog;
import com.huabo.etl.domain.QuartzDTO;
import com.huabo.etl.domain.XRepository;
import com.huabo.etl.mapper.KettleTransLogMapper;
import com.huabo.etl.mapper.KettleTransMapper;
import com.huabo.etl.mapper.XRepositoryMapper;
import com.huabo.etl.service.IKettleTransService;
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
 * 转换Service业务层处理
 *
 * @author zhibo.cao
 * @date 2022-12-01
 */
@Service("kettleTransServiceImpl")
public class KettleTransServiceImpl implements IKettleTransService {

    private static final Logger log = LoggerFactory.getLogger(KettleTransServiceImpl.class);
    @Autowired
    private KettleTransMapper kettleTransMapper;
    @Autowired
    private XRepositoryMapper repositoryMapper;

    @Autowired
    private IkettleService ikettleService;

    @Autowired
    private KettleTransLogMapper kettleTransLogMapper;

    /**
     * 查询转换
     *
     * @param id 转换ID
     * @return 转换
     */
    @Override
    public KettleTrans selectKettleTransById(Long id) {
        return kettleTransMapper.selectKettleTransById(id);
    }

    /**
     * 查询转换列表
     *
     * @param kettleTrans 转换
     * @return 转换
     */
    @Override
    public List<KettleTrans> selectKettleTransList(KettleTrans kettleTrans) {
        return kettleTransMapper.selectKettleTransList(kettleTrans);
    }

    /**
     * 新增转换
     *
     * @param kettleTrans 转换
     * @return 结果
     */
    @Override
    public AjaxResult insertKettleTrans(KettleTrans kettleTrans) {
        LambdaQueryWrapper<KettleTrans> query = Wrappers.<KettleTrans>lambdaQuery();
        query.eq(KettleTrans::getTransName, kettleTrans.getTransName()).eq(KettleTrans::getTransRepositoryId, kettleTrans.getTransRepositoryId());
        query.eq(KettleTrans::getIsDel, 0);
        List<KettleTrans> trans = kettleTransMapper.selectList(query);
        if (CollUtil.isNotEmpty(trans)) {
            return AjaxResult.error("该资源库下已存在同名转换");
        }
        kettleTrans.setTransType("File");
        return AjaxResult.success(kettleTransMapper.insert(kettleTrans));
    }

    /**
     * 修改转换
     *
     * @param kettleTrans 转换
     * @return 结果
     */
    @Override
    public int updateKettleTrans(KettleTrans kettleTrans) {
        kettleTrans.setUpdateTime(DateUtils.getNowDate());
        kettleTrans.setTransType("File");
        return kettleTransMapper.updateKettleTrans(kettleTrans);
    }

    /**
     * 删除转换对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKettleTransByIds(List<Long> ids) {
        return kettleTransMapper.deleteKettleTransByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除转换信息
     *
     * @param id 转换ID
     * @return 结果
     */
    @Override
    public int deleteKettleTransById(Long id) {
        return kettleTransMapper.deleteKettleTransById(id);
    }


    /**
     * 立即执行一次转换,
     *
     * @param trans :
     * @return
     */
    @Override
    public AjaxResult runOne(KettleTrans trans) {
        Long id = trans.getId();
        KettleTrans kettleTrans = kettleTransMapper.selectKettleTransById(id);
        if (kettleTrans == null || kettleTrans.getId() == null) {
            return AjaxResult.error("转换不存在!");
        }
        XRepository repository = repositoryMapper.selectXRepositoryById(kettleTrans.getTransRepositoryId());
        if (repository == null) {
            return AjaxResult.error("资源库不存在!");
        }
        File file = new File(repository.getBaseDir() + kettleTrans.getTransPath() + kettleTrans.getTransName() + ".ktr");
        if (!file.exists()) {
            return AjaxResult.error(file.getPath() + "未找到文件!");
        }
        runTranSync(kettleTrans, repository);
        //更新一下状态
        trans.setTransStatus("等待中");
        kettleTransMapper.updateKettleTrans(trans);
        return AjaxResult.success("正在异步执行,请等待运行结果通知!");
    }

    @Override
    public AjaxResult runByName(KettleTrans trans) {
        String transName = trans.getTransName();
        List<KettleTrans> kettleTransList = kettleTransMapper.selectKettleTransByName(transName);
        if (kettleTransList == null || kettleTransList.isEmpty()) {
            return AjaxResult.error("转换不存在!");
        }
        if (kettleTransList.size() > 1) {
            return AjaxResult.error("[" + transName + "]找到两个同名的转换,请联系管理员!");
        }
        KettleTrans kettleTrans = kettleTransList.get(0);
        XRepository repository = repositoryMapper.selectXRepositoryById(kettleTrans.getTransRepositoryId());
        if (repository == null) {
            return AjaxResult.error("资源库不存在!");
        }
        File file = new File(repository.getBaseDir() + kettleTrans.getTransPath() + kettleTrans.getTransName() + ".ktr");
        if (!file.exists()) {
            return AjaxResult.error(file.getPath() + "未找到文件!");
        }
        runTranSync(kettleTrans, repository);
        //更新一下状态
        trans.setTransStatus("等待中");
        kettleTransMapper.updateKettleTrans(trans);
        return AjaxResult.success("正在异步执行,请等待运行结果通知!");
    }

    @Override
    public Map<String, Object> count() {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<KettleTrans> query = Wrappers.<KettleTrans>lambdaQuery();
        query.ne(KettleTrans::getIsDel, 1).isNotNull(KettleTrans::getIsDel);
        // total
        Long totalTran = kettleTransMapper.selectCount(query);
        result.put("totalTran", totalTran);

        // runtotal
        query.eq(KettleTrans::getCronStatus, "运行中");
        Long runTran = kettleTransMapper.selectCount(query);
        result.put("runTran", runTran);

        DateTime beforeDate = DateUtil.offsetDay(new Date(), -7);
        // 成功日志
        LambdaQueryWrapper<KettleTransLog> logQuery = Wrappers.<KettleTransLog>lambdaQuery();
        logQuery.between(KettleTransLog::getEnddate, DateUtil.beginOfDay(beforeDate), DateUtil.endOfDay(new Date()));
        logQuery.eq(KettleTransLog::getStatus, "end");
        List<KettleTransLog> kettleTransLogs = kettleTransLogMapper.selectList(logQuery);
        if (CollUtil.isNotEmpty(kettleTransLogs)) {
            Map<String, Long> logCount = kettleTransLogs.stream().collect(Collectors.groupingBy(KettleTransLog::getEnddateStr, Collectors.counting()));
            result.put("success", getAllCount(logCount));
        } else {
            result.put("success", getAllCount(null));
        }

        // 失败日志
        LambdaQueryWrapper<KettleTransLog> logQuery2 = Wrappers.<KettleTransLog>lambdaQuery();
        logQuery2.between(KettleTransLog::getEnddate, DateUtil.beginOfDay(beforeDate), DateUtil.endOfDay(new Date()));
        logQuery2.ne(KettleTransLog::getStatus, "end");
        List<KettleTransLog> kettleTransLogs2 = kettleTransLogMapper.selectList(logQuery2);
        if (CollUtil.isNotEmpty(kettleTransLogs2)) {
            Map<String, Long> logFailCount = kettleTransLogs2.stream().collect(Collectors.groupingBy(KettleTransLog::getEnddateStr, Collectors.counting()));
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

    /**
     * 通过名称查询转换
     *
     * @param tranName
     * @return
     */
    @Override
    public KettleTrans selectKettleTransByName(String tranName) {
        List<KettleTrans> kettleTrans = kettleTransMapper.selectKettleTransByName(tranName);
        if (CollUtil.isNotEmpty(kettleTrans)) {
            return kettleTrans.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<KettleTrans> selectKettleTransByRepoIds(List<Long> ids) {
        LambdaQueryWrapper<KettleTrans> query = Wrappers.<KettleTrans>lambdaQuery();
        query.in(KettleTrans::getTransRepositoryId, ids);
        return kettleTransMapper.selectList(query);
    }

    /**
     * 立即执行
     *
     * @param id
     */
    @Override
    public void runTransRightNow(Long id) {
        KettleTrans kettleTrans = kettleTransMapper.selectKettleTransById(id);
        if (kettleTrans == null || kettleTrans.getId() == null) {
            log.error("转换不存在!:" + id);
            return;
        }
        XRepository repository = repositoryMapper.selectXRepositoryById(kettleTrans.getTransRepositoryId());
        if (repository == null) {
            log.error("资源库不存在!");
            return;
        }
        //更新状态未运行中
        kettleTrans.setTransStatus("运行中");
        kettleTransMapper.updateKettleTrans(kettleTrans);
        StringBuilder title = new StringBuilder(kettleTrans.getTransName()).append(".ktr 执行结果:");
        StringBuilder msg = new StringBuilder(kettleTrans.getTransName()).append(":描述:").append(kettleTrans.getTransDescription());
        try {
            ikettleService.callTrans(kettleTrans, repository, null, null);
            kettleTrans.setTransStatus("成功");
            kettleTrans.setLastSucceedTime(DateUtils.getNowDate());
            kettleTransMapper.updateKettleTrans(kettleTrans);
            title.append("成功!");
        } catch (Exception e) {
            kettleTrans.setTransStatus("异常");
            msg.append(":" + e.getMessage());
            kettleTransMapper.updateKettleTrans(kettleTrans);
            title.append("异常!");
            log.error(kettleTrans.getId() + "的trans执行失败:" + e.getMessage());
        }
    }

    /**
     * 异步执行
     */
    @Async
    public void runTranSync(KettleTrans kettleTrans, XRepository repository) {
        //更新状态未运行中
        kettleTrans.setTransStatus("运行中");
        kettleTransMapper.updateKettleTrans(kettleTrans);
        StringBuilder title = new StringBuilder(kettleTrans.getTransName()).append(".ktr 执行结果:");
        StringBuilder msg = new StringBuilder(kettleTrans.getTransName()).append(":描述:").append(kettleTrans.getTransDescription());
        try {
            ikettleService.callTrans(kettleTrans, repository, null, null);
            kettleTrans.setTransStatus("成功");
            kettleTrans.setLastSucceedTime(DateUtils.getNowDate());
            kettleTransMapper.updateKettleTrans(kettleTrans);
            title.append("成功!");
        } catch (Exception e) {
            kettleTrans.setTransStatus("异常");
            msg.append(":" + e.getMessage());
            kettleTransMapper.updateKettleTrans(kettleTrans);
            title.append("异常!");
            log.error(kettleTrans.getId() + "的trans执行失败:" + e.getMessage());
        }
    }


    /**
     * 查询抓换执行日志
     *
     * @param kettleTrans
     * @return
     */
    @Override
    public List<String> queryTransLog(KettleTrans kettleTrans) {
        List<String> transLogs = kettleTransMapper.queryTransLog(kettleTrans.getTransName());
        return transLogs;
    }

    /**
     * 设置定时执行转换
     *
     * @param id
     * @param transName
     * @return
     */
    @Override
    public AjaxResult runStopTransQuartz(String id, String transName) {
        KettleTrans kettleTrans = kettleTransMapper.selectKettleTransById(Long.valueOf(id));
        QuartzDTO quartzDTO = new QuartzDTO();
        quartzDTO.setJobName(kettleTrans.getTransName() + "@" + kettleTrans.getId());
        quartzDTO.setJobGroupName(kettleTrans.getTransName() + "@" + "jobGroup" + "@" + kettleTrans.getId());
        quartzDTO.setTriggerName(kettleTrans.getTransName() + "@" + kettleTrans.getId());
        quartzDTO.setTriggerGroupName(kettleTrans.getTransName() + "@" + "trigerGroup" + "@" + kettleTrans.getId());
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("runType", "tran");
        jobDataMap.put("id", id);
        quartzDTO.setJobDataMap(jobDataMap);
        quartzDTO.setCron(kettleTrans.getCron());
        quartzDTO.setJobClass(ScriptQuartz.class);

        if (StrUtil.isEmpty(kettleTrans.getCronStatus()) || kettleTrans.getCronStatus().equals("未运行")) {
            kettleTrans.setCronStatus("运行中");
            kettleTransMapper.updateKettleTrans(kettleTrans);
            QuartzManage.addCronJob(quartzDTO);
            return AjaxResult.success("已启动定时任务");
        } else {
            kettleTrans.setCronStatus("未运行");
            kettleTransMapper.updateKettleTrans(kettleTrans);

            QuartzManage.removeJob(quartzDTO);
            return AjaxResult.success("已停止定时任务");
        }
    }


    /**
     * 检查该转换是否设置了定时任务
     *
     * @param checkStr
     * @return
     */
    @Override
    public Long checkQuartzExist(String checkStr) {
        return kettleTransMapper.checkQuartzExist(checkStr);
    }

}
