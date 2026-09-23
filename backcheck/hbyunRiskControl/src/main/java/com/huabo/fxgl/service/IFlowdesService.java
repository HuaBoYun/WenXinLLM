package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.Flowdes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-08
 */
public interface IFlowdesService extends IService<Flowdes> {
    List<Flowdes> returnFlowdesByRiskid(String riskid);
}
