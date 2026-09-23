package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.BdAccassitem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 会计辅助核算项目 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
public interface IBdAccassitemService extends IService<BdAccassitem> {

	JsonBean getAccAssTreeList(TblStaffUtil staff) throws Exception;

}
