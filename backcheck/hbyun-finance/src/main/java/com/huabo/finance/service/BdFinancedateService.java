package com.huabo.finance.service;

import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.vo.BdFinancedateRecordVo;
import com.huabo.finance.vo.BdFinancedateVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 财务数据采集配置信息表 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-11
 */
public interface BdFinancedateService extends IService<BdFinancedate> {

	JsonBean findAllList(TblStaffUtil staff, BdFinancedateVo vo) throws Exception;

	JsonBean save(TblStaffUtil staff, BdFinancedate fd) throws Exception;

	JsonBean detail(String fid) throws Exception;

	JsonBean remove(String fid) throws Exception;

	JsonBean testCon(BdFinancedate fd) throws Exception;

}
