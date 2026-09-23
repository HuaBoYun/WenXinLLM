package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.GlBalance;
import com.huabo.finance.vo.GlBalanceVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 凭证余额 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
public interface IGlBalanceService extends IService<GlBalance> {

	JsonBean getFinanceDataList(TblStaffUtil staff, GlBalanceVo vo) throws Exception;

	JsonBean getFinanceDataSumTotalList(TblStaffUtil staff, GlBalanceVo vo) throws Exception;

}
