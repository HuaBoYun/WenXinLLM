package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.RiskeventOuterrule;
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
public interface IRiskeventOuterruleService extends IService<RiskeventOuterrule> {

    List<RiskeventOuterrule> getByEventId(String eventid);
}
