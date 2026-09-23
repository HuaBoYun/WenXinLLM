package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblUruleTask;
import com.huabo.audit.oracle.mapper.TblUruleTaskMapper;
import com.huabo.audit.service.ScheduledTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduledTaskServiceImpl extends ServiceImpl<TblUruleTaskMapper, TblUruleTask> implements ScheduledTaskService {

    @Resource
    private UserProvider userProvider;

    @Resource
    private TblUruleTaskMapper tblUruleTaskMapper;

    @Resource
    private ScheduledTaskOperateService scheduledTaskOperateService;

//    @Resource
//    private BdFinanceplanMapper bdFinanceplanMapper;

    @PostConstruct
    public void init() {
        // 应用启动时加载所有启用的定时任务
        List<TblUruleTask> enabledTasks = tblUruleTaskMapper.findByStatus(1);
        for (TblUruleTask task : enabledTasks) {
            scheduledTaskOperateService.scheduleTask(task);
        }
    }

    //在数据库中添加新的定时任务信息，同时启动该定时任务
    @Override
    public JsonBean add(TblUruleTask tst) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        tst.setBeanName(TblUruleTask.FINANCEGATHERBEAN);
        tst.setMethodName(TblUruleTask.FINANCEGATHERMETHOD);
        tst.setCreateStaffId(staff.getStaffid());
        tst.setLinkDeptId(staff.getLinkDetp().getOrgid());
        tst.setLinkOrgId(staff.getLinkOrg().getOrgid());
        tst.setCreateTime(new Date());
        tst.setTaskId(RandomUtil.uuStringId());
        this.tblUruleTaskMapper.insert(tst);

        if (tst.getStatus()==1) {
            scheduledTaskOperateService.scheduleTask(tst);
        }

        return ResponseFormat.retParam(1, 200, null);
    }

    //修改定时任务信息，同时关闭原来的定时任务，并重新启动
    @Override
    public JsonBean modify(TblUruleTask tst) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        tst.setBeanName(TblUruleTask.FINANCEGATHERBEAN);
        tst.setMethodName(TblUruleTask.FINANCEGATHERMETHOD);
        tst.setModifyStaffId(BigDecimal.valueOf(5555));
        tst.setModifyTIme(new Date());
        this.tblUruleTaskMapper.updateById(tst);

        if (tst.getStatus()==1) {
            scheduledTaskOperateService.scheduleTask(tst);
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    //删除定时任务信息，同时关闭定时任务
    @Override
    public JsonBean delete(String taskId) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        scheduledTaskOperateService.cancelTask(taskId);
        this.tblUruleTaskMapper.deleteById(taskId);
        return ResponseFormat.retParam(1, 200, null);
    }

    //获取定时任务列表
    @Override
    public JsonBean getlist(Integer pageNumber, Integer pageSize, String taskName) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        if(pageNumber == null) {
            pageNumber = 1;
        }
        if(pageSize==null) {
            pageSize= 15;
        }

        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        PageHelper.startPage(pageNumber,pageSize);

        // 创建查询条件
        TblUruleTask tblUruleTask = new TblUruleTask();
        if (taskName != null && !taskName.isEmpty()) {
            tblUruleTask.setTaskName(taskName);
        }else {
            taskName = "";
        }
        List<TblUruleTask> list = tblUruleTaskMapper.findByNameList(taskName);

        com.github.pagehelper.PageInfo<TblUruleTask> pageInfo = new com.github.pagehelper.PageInfo<>(list);

        resultMap.put("list", list);
        return  ResponseFormat.retParam(1,200,resultMap);
    }

    //获取定时任务列表
/*    @Override
    public JsonBean getlist(TblSysScheduledTaskVo vo) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        vo.setOrgId(staff.getCurrentOrg().getOrgid());

        Page<TblSysScheduledTaskVr> page = new Page<TblSysScheduledTaskVr>(vo.getPageNumber(), vo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblSysScheduledTaskVr> pageList = this.tblSysScheduledTaskMapper.selectFinanceDataPage(page, vo);

        List<TblSysScheduledTaskVr> list = pageList.getRecords();

        for (TblSysScheduledTaskVr task : list) {
            task.setRunning(scheduledTaskOperateService.containsKey(task.getTaskId()));
            task.setNextTime(scheduledTaskOperateService.getNextExecutionTime(task));
        }

        pageList.setRecords(list);

        return ResponseFormat.retParam(1, 200, pageList);
    }*/

    //获取定时任务详情
    @Override
    public JsonBean getOne(String taskId) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
//        TblSysScheduledTask task = this.tblSysScheduledTaskMapper.selectById(taskId);
        TblUruleTask task = this.tblUruleTaskMapper.selectById(taskId);
//根据采集方案的ID，获取采集方案的名称
//        if(StringUtils.isNoneBlank(task.getPlanId())) {
//            BdFinanceplan plan = this.bdFinanceplanMapper.selectById(task.getPlanId());
//            task.setPlanName(plan.getFname());
//        }
        return ResponseFormat.retParam(1, 200, task);
    }

    //启动、停止定时任务.单独改变任务状态，不改变任务配置信息
    @Override
    public JsonBean task_status(String taskId, Integer status) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblUruleTask task = this.tblUruleTaskMapper.selectById(taskId);
        task.setStatus(status);
        this.tblUruleTaskMapper.updateById(task);

        //1 是 启用 0 停用
        if (task.getStatus()==1) {
            scheduledTaskOperateService.scheduleTask(task);
        }else {
            scheduledTaskOperateService.cancelTask(taskId);
        }
        return ResponseFormat.retParam(1, 200, null);
    }


}

