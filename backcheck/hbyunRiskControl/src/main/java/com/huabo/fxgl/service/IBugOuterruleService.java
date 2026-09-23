package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.BugOuterrule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Outerrule;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IBugOuterruleService extends IService<BugOuterrule> {

    void deleteByBudId(String selectProjectid);

    List<Outerrule> getOuterRulesByBugId(String id);
     List<BugOuterrule> isIfFlowInner(String flowid, String innerid);

}
