package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.BdFinanceAccass;
import com.huabo.finance.vo.BdFinanceAccassVo;
import com.huabo.finance.vo.GlAssBalaneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 会计辅助信息 服务类
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
public interface BdFinanceAccassService extends IService<BdFinanceAccass> {

	JsonBean getFinanceAccAssInfoList(TblStaffUtil staff, BdFinanceAccassVo vo) throws Exception;

}
