package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Innerrule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IInnerruleService extends IService<Innerrule> {
    IPage<Innerrule> findInnerRuleByBugPageBean(IPage page, String bugid,String orgid);
    IPage<Innerrule> findInnerRuleByFlow(IPage page, String flowid);
}
