package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.vo.BdFinanceplanVo;

/**
 * <p>
 * 公司采集配置方案信息表 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
public interface BdFinanceplanService extends IService<BdFinanceplan> {

	JsonBean save(TblStaffUtil staff, BdFinanceplan fd) throws Exception;

	JsonBean detail(String fid) throws Exception;

	JsonBean remove(String fid) throws Exception;

	JsonBean findAllList(TblStaffUtil staff, BdFinanceplanVo vo) throws Exception;

	JsonBean getFianacePlanList(TblStaffUtil staff, String fname) throws Exception;

	JsonBean modifyStatus(String fid, Integer fstatus) throws Exception;

}
