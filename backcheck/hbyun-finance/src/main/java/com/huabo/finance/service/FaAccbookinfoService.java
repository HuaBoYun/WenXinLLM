package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.FaAccbookinfo;
import com.huabo.finance.vo.FaAccbookinfoVo;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 账簿信息 服务类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
public interface FaAccbookinfoService extends IService<FaAccbookinfo> {

	JsonBean getList(HttpServletRequest request, TblStaffUtil staff, FaAccbookinfoVo vo) throws Exception;

	JsonBean save(TblStaffUtil staff, FaAccbookinfo fab) throws Exception;

	JsonBean del(TblStaffUtil staff, String pkAccbookinfo) throws Exception;

	JsonBean detail(TblStaffUtil staff, String pkAccbookinfo) throws Exception;

}
