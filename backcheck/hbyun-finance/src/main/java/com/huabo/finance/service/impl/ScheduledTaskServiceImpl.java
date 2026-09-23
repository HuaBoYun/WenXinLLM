package com.huabo.finance.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.entity.TblSysScheduledTask;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.mapper.BdFinanceplanMapper;
import com.huabo.finance.mapper.TblSysScheduledTaskMapper;
import com.huabo.finance.service.ScheduledTaskService;
import com.huabo.finance.thread.ScheduledTaskOperateService;
import com.huabo.finance.vo.TblSysScheduledTaskVo;
import com.huabo.finance.vr.TblSysScheduledTaskVr;


/**
 * <p>
 * 平均汇率 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Service
public class ScheduledTaskServiceImpl extends ServiceImpl<TblSysScheduledTaskMapper, TblSysScheduledTask> implements ScheduledTaskService {

	@Resource
	private UserProvider userProvider;
	
	@Resource
	private TblSysScheduledTaskMapper tblSysScheduledTaskMapper;
	
	@Resource
	private ScheduledTaskOperateService scheduledTaskOperateService;
	
	@Resource
	private BdFinanceplanMapper bdFinanceplanMapper;
	
	@PostConstruct
    public void init() {
		  // 应用启动时加载所有启用的定时任务
        List<TblSysScheduledTask> enabledTasks = tblSysScheduledTaskMapper.findByStatus(1);
        for (TblSysScheduledTask task : enabledTasks) {
        	scheduledTaskOperateService.scheduleTask(task);
		}
    }
	
	@Override
	public JsonBean add(TblSysScheduledTask tst) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		tst.setBeanName(TblSysScheduledTask.FINANCEGATHERBEAN);
		tst.setMethodName(TblSysScheduledTask.FINANCEGATHERMETHOD);
		tst.setCreateStaffId(staff.getStaffid());
		tst.setLinkDeptId(staff.getLinkDetp().getOrgid());
		tst.setLinkOrgId(staff.getLinkOrg().getOrgid());
		tst.setCreateTime(new Date());
		tst.setTaskId(RandomUtil.uuStringId());
		this.tblSysScheduledTaskMapper.insert(tst);
		
		if (tst.getStatus()==1) {
			scheduledTaskOperateService.scheduleTask(tst);
        }
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean modify(TblSysScheduledTask tst) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		tst.setBeanName(TblSysScheduledTask.FINANCEGATHERBEAN);
		tst.setMethodName(TblSysScheduledTask.FINANCEGATHERMETHOD);
		tst.setModifyStaffId(staff.getStaffid());
		tst.setModifyTIme(new Date());
		this.tblSysScheduledTaskMapper.updateById(tst);
		
		if (tst.getStatus()==1) {
			scheduledTaskOperateService.scheduleTask(tst);
        }
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean delete(String taskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		scheduledTaskOperateService.cancelTask(taskId);
		this.tblSysScheduledTaskMapper.deleteById(taskId);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
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
	}

	@Override
	public JsonBean getOne(String taskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblSysScheduledTask task = this.tblSysScheduledTaskMapper.selectById(taskId);
		
		if(StringUtils.isNoneBlank(task.getPlanId())) {
			BdFinanceplan plan = this.bdFinanceplanMapper.selectById(task.getPlanId());
			task.setPlanName(plan.getFname());
		}
		return ResponseFormat.retParam(1, 200, task);
	}

	@Override
	public JsonBean task_status(String taskId, Integer status) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblSysScheduledTask task = this.tblSysScheduledTaskMapper.selectById(taskId);
		task.setStatus(status);
		this.tblSysScheduledTaskMapper.updateById(task);
		
		if (task.getStatus()==1) {
			scheduledTaskOperateService.scheduleTask(task);
        }else {
        	scheduledTaskOperateService.cancelTask(taskId);
        }
		return ResponseFormat.retParam(1, 200, null);
	}


}
