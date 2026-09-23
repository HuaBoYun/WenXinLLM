package com.huabo.fxgl.service;

import com.huabo.fxgl.entity.FlowBussiness;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
public interface IFlowBussinessService extends IService<FlowBussiness> {

    FlowBussiness findUniqueByFlowId(BigDecimal flowid) throws Exception;
    BigDecimal newBissinessId();

    List<FlowBussiness> getByRiskIds(List<BigDecimal> riskIdslist);
}
