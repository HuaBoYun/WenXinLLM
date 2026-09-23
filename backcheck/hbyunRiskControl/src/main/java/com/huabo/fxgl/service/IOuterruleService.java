package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.BugOuterrule;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IOuterruleService extends IService<Outerrule> {
    IPage<Outerrule> findOuterRuleByBugPageBean(IPage page, String bugid, String orgid);

    IPage<Outerrule> findOuterRuleByFlow(IPage page, String flowid);


}
