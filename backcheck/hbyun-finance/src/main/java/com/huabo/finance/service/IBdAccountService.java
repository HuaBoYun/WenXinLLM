package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.BdAccount;
import com.huabo.finance.vo.BdAccountVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 会计科目基本信息 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
public interface IBdAccountService extends IService<BdAccount> {

	JsonBean getFinanceDataList(TblStaffUtil staff, BdAccountVo vo) throws Exception;

}
