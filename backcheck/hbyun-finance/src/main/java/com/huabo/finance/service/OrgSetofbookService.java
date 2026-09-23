package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.OrgSetofbook;
import com.huabo.finance.vo.OrgSetofbookVo;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 账簿类型 服务类
 * </p>
 *
 * @author L
 * @since 2025-03-31
 */
public interface OrgSetofbookService extends IService<OrgSetofbook> {

	JsonBean getList(HttpServletRequest request, TblStaffUtil staff, OrgSetofbookVo vo) throws Exception;

	JsonBean save(TblStaffUtil staff, OrgSetofbook sob) throws Exception;

	JsonBean del(TblStaffUtil staff, String pkSetofbook) throws Exception;

	JsonBean detail(TblStaffUtil staff, String pkSetofbook) throws Exception;

}
