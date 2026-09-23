package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskeventAtt;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskeventAttService extends IService<RiskeventAtt> {

    List<BigDecimal> getAttIdsByEventId(String eventid);
}
