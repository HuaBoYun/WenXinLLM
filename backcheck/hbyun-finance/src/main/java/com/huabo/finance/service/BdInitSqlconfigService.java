package com.huabo.finance.service;

import com.huabo.finance.entity.BdInitSqlconfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 财务采方案sql初始化表 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-13
 */
public interface BdInitSqlconfigService extends IService<BdInitSqlconfig> {

	JsonBean findAllList(TblStaffUtil staff, String fid) throws Exception;

}
