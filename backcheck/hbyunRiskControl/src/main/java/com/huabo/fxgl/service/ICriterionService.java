package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.Criterion;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-12
 */
public interface ICriterionService extends IService<Criterion> {

    List<Criterion> getByOrgId(BigDecimal id);
}
