package com.huabo.finance.service;

import com.huabo.finance.entity.BdPlanSqlconfig;
import com.huabo.finance.vo.BdPlanSqlconfigVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 财务采方案配置sql语句 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-13
 */
public interface BdPlanSqlconfigService extends IService<BdPlanSqlconfig> {

	JsonBean save(TblStaffUtil staff, BdPlanSqlconfig ps) throws Exception;

	JsonBean detail(String fid) throws Exception;

	JsonBean remove(String fid) throws Exception;

	JsonBean findAllList(TblStaffUtil staff, BdPlanSqlconfigVo pc) throws Exception;

	JsonBean testSql(TblStaffUtil staff, BdPlanSqlconfig ps) throws Exception;

	JsonBean excuteSql(TblStaffUtil staff, BdPlanSqlconfig ps) throws Exception;
}
