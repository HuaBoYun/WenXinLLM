package com.huabo.finance.service;

import com.huabo.finance.entity.caiji.FaAccbookUser;
import com.huabo.finance.vo.FaAccbookinfoVo;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

/**
 * <p>
 * 用户选择默认账簿 服务类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
public interface FaAccbookUserService extends IService<FaAccbookUser> {

	JsonBean getBookList(HttpServletRequest request, TblStaffUtil staff, FaAccbookinfoVo vo) throws Exception;

	JsonBean selected(TblStaffUtil staff, String pkAccbookinfo) throws Exception;

}
