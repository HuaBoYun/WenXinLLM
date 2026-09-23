package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.BugInnerrule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Innerrule;

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
public interface IBugInnerruleService extends IService<BugInnerrule> {

    List<BugInnerrule> isIfFlowInner(String flowid, String innerid);

    void deleteByBudId(String selectProjectid);

    List<Innerrule> getInnerRulesByBugId(String id);


}
