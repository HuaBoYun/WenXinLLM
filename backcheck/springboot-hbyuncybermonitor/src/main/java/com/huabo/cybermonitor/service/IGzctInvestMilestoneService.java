package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.GzctInvestMilestone;
import java.util.List;
import java.util.Map;

public interface IGzctInvestMilestoneService extends IService<GzctInvestMilestone> {
    List<GzctInvestMilestone> listByProjectId(String projectId);
    boolean addMilestone(GzctInvestMilestone milestone);
    boolean updateMilestone(GzctInvestMilestone milestone);
    boolean deleteMilestone(String milestoneId);
}
