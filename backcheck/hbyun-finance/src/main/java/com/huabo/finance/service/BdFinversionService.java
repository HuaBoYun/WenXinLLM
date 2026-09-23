package com.huabo.finance.service;

import com.huabo.finance.entity.BdFinversion;
import com.huabo.finance.vo.BdFinversionVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 财务数据版本 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-10
 */
public interface BdFinversionService extends IService<BdFinversion> {

	JsonBean findAllList(TblStaffUtil staff, BdFinversionVo vo) throws Exception;

	JsonBean save(TblStaffUtil staff, BdFinversion fv) throws Exception;

	JsonBean getOne(String fid) throws Exception;

	JsonBean remove(String fid) throws Exception;

	JsonBean getParentList(TblStaffUtil staff) throws Exception;

}
