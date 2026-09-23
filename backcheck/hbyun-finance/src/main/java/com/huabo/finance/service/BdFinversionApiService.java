package com.huabo.finance.service;

import com.huabo.finance.entity.BdFinversionApi;
import com.huabo.finance.vo.BdFinversionApiVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 财务系统API配置 服务类
 * </p>
 *
 * @author Augment Code
 * @since 2026-08-09
 */
public interface BdFinversionApiService extends IService<BdFinversionApi> {

	JsonBean findAllList(TblStaffUtil staff, BdFinversionApiVo vo) throws Exception;

	JsonBean save(TblStaffUtil staff, BdFinversionApi fv) throws Exception;

	JsonBean getOne(String fid) throws Exception;

	JsonBean remove(String fid) throws Exception;

	JsonBean getParentList(TblStaffUtil staff) throws Exception;

	JsonBean testApiConnection(TblStaffUtil staff, BdFinversionApi api) throws Exception;

}
