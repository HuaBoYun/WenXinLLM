package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskeventInnerrule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
public interface IRiskeventInnerruleService extends IService<RiskeventInnerrule> {

    List<RiskeventInnerrule> getByEventId(String eventid);
}
