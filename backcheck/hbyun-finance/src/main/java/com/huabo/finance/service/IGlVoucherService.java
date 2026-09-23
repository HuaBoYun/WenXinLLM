package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.vo.GlVoucherVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 凭证库表 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-19
 */
public interface IGlVoucherService extends IService<GlVoucher> {

	JsonBean getFinanceDataList(TblStaffUtil staff, GlVoucherVo vo) throws Exception;

}
