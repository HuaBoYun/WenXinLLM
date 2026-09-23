package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RepAtt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-18
 */
public interface IRepAttService extends IService<RepAtt> {

    List<BigDecimal> getAttIdsByReportId(String id);
}
