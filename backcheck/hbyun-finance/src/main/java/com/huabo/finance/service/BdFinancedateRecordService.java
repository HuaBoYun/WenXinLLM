package com.huabo.finance.service;

import com.huabo.finance.entity.BdFinancedateRecord;
import com.huabo.finance.vo.BdFinancedateRecordVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 财务数据采集记录表 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-17
 */
public interface BdFinancedateRecordService extends IService<BdFinancedateRecord> {

	JsonBean getGoonAcInfoList(BdFinancedateRecordVo vo) throws Exception;

	JsonBean getAllAcInfoList(BdFinancedateRecordVo vo) throws Exception;

	JsonBean getFinanceRecordPageList(TblStaffUtil staff, BdFinancedateRecordVo vo, String tableId, String sqlconfigid,
			String planid, String sqlfinid) throws Exception;

	JsonBean getRecordDetail(TblStaffUtil staff, String recordid) throws Exception;

}
