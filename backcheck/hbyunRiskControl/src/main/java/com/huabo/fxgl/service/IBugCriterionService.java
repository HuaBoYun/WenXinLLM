package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.BugCriterion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Criterion;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface IBugCriterionService extends IService<BugCriterion> {

    List<Criterion> getCriterionByBugId(String id);
    List<BugCriterion> findTblBugCriterionListByorgid(String orgid);
}
