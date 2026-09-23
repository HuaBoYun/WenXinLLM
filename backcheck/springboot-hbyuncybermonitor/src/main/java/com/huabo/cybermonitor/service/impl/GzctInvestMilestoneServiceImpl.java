package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctInvestMilestone;
import com.huabo.cybermonitor.mapper.GzctInvestMilestoneMapper;
import com.huabo.cybermonitor.service.IGzctInvestMilestoneService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GzctInvestMilestoneServiceImpl extends ServiceImpl<GzctInvestMilestoneMapper, GzctInvestMilestone>
        implements IGzctInvestMilestoneService {

    @Override
    public List<GzctInvestMilestone> listByProjectId(String projectId) {
        LambdaQueryWrapper<GzctInvestMilestone> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(projectId)) {
            wrapper.eq(GzctInvestMilestone::getProjectId, projectId);
        }
        wrapper.orderByAsc(GzctInvestMilestone::getPlanDate);
        return this.list(wrapper);
    }

    @Override
    public boolean addMilestone(GzctInvestMilestone milestone) {
        milestone.setCreateTime(LocalDateTime.now());
        milestone.setUpdateTime(LocalDateTime.now());
        return this.save(milestone);
    }

    @Override
    public boolean updateMilestone(GzctInvestMilestone milestone) {
        milestone.setUpdateTime(LocalDateTime.now());
        return this.updateById(milestone);
    }

    @Override
    public boolean deleteMilestone(String milestoneId) {
        return this.removeById(milestoneId);
    }
}
