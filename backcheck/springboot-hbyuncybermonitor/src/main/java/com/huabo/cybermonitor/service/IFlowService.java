package com.huabo.cybermonitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.cybermonitor.entity.Controlmatrix;
import com.huabo.cybermonitor.entity.Flow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.Riskevent;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kangjx
 * @since 2022-08-11
 */
public interface IFlowService extends IService<Flow> {

    List<Flow> showListWithIndicatorid( BigDecimal id);

    List<Controlmatrix> findAllControlmatrix(BigDecimal indicatorid);

    List<Riskevent> findAllRISKEVENT(BigDecimal indicatorid);

    JsonBean deleteIndicatorById(String id);
}
