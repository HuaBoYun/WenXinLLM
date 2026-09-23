package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.NbsjBugcriterion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.entity.Risk;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
public interface INbsjBugcriterionService extends IService<NbsjBugcriterion> {
    NbsjBugcriterion findNbsjBugcriterionByorgid(String bugid);

}
