package com.huabo.compliance.service;

import com.huabo.compliance.entity.TblAssessTarget;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface ITblAssessTargetService extends IService<TblAssessTarget> {

	TblAssessTarget selectTblAssessTarget(BigDecimal id) throws Exception;

}
