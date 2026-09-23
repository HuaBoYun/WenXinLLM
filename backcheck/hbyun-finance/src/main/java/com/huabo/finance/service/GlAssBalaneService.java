package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.caiji.GlAssBalane;
import com.huabo.finance.vo.GlAssBalaneVo;

/**
 * <p>
 * 辅助账余额 服务类
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
public interface GlAssBalaneService extends IService<GlAssBalane> {

	JsonBean getFinanceAccAssBalanceList(TblStaffUtil staff, GlAssBalaneVo vo) throws Exception;

	JsonBean getFinanceAccAssGeneralLedgerList(TblStaffUtil staff, GlAssBalaneVo vo) throws Exception;

}
