package com.huabo.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.TempCollectionPlan;
import com.huabo.finance.mapper.TempCollectionPlanMapper;
import com.huabo.finance.service.TempCollectionPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 临时采集方案服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Service
public class TempCollectionPlanServiceImpl extends ServiceImpl<TempCollectionPlanMapper, TempCollectionPlan> 
        implements TempCollectionPlanService {

    @Resource
    private TempCollectionPlanMapper tempCollectionPlanMapper;

    @Override
    public JsonBean saveTempPlan(TblStaffUtil staff, TempCollectionPlan plan) throws Exception {
        if (StringUtils.isNotBlank(plan.getPlanId())) {
            // 更新
            plan.setModifier(staff.getStaffid());
            plan.setModifyTime(new Date());
            tempCollectionPlanMapper.updateById(plan);
        } else {
            // 新增
            plan.setPlanId(RandomUtil.uuStringId());
            plan.setCreator(staff.getStaffid());
            plan.setCreateTime(new Date());
            plan.setLinkOrgId(staff.getCurrentOrg().getOrgid());
            plan.setLinkDeptId(staff.getLinkDetp().getOrgid());
            plan.setIsTemporary(1); // 标记为临时方案
            tempCollectionPlanMapper.insert(plan);
        }
        return ResponseFormat.retParam(1, "保存成功", plan);
    }

    @Override
    public JsonBean getTempPlanByTaskId(String taskId) throws Exception {
        TempCollectionPlan plan = tempCollectionPlanMapper.selectByTaskId(taskId);
        return ResponseFormat.retParam(1, 200, plan);
    }

    @Override
    public JsonBean deleteTempPlan(String planId) throws Exception {
        tempCollectionPlanMapper.deleteById(planId);
        return ResponseFormat.retParam(1, "删除成功", null);
    }

    @Override
    public JsonBean getKeepPlanList(TblStaffUtil staff) throws Exception {
        List<TempCollectionPlan> planList = tempCollectionPlanMapper.selectKeepList();
        return ResponseFormat.retParam(1, 200, planList);
    }
}

